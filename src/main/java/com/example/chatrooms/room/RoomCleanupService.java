package com.example.chatrooms.room;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomCleanupService {
    private final RoomRepository roomRepository;
    private final RoomSessionService roomSessionService;

    private final Map<String, Instant> emptySince = new ConcurrentHashMap<>();

    private static final Duration TIMEOUT = Duration.ofMinutes(5);

    public RoomCleanupService(RoomRepository roomRepository, RoomSessionService roomSessionService){
        this.roomRepository = roomRepository;
        this.roomSessionService = roomSessionService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndClearEmptyRooms(){
        Instant now = Instant.now();
        for(Room room : roomRepository.findAll()){
            String name = room.getName();
            if(roomSessionService.countInRoom(name)>0){
                emptySince.remove(name);
                continue;
            }else{
                emptySince.putIfAbsent(name, now);
                if(Duration.between(emptySince.get(name), now).compareTo(TIMEOUT)>=0){
                    roomRepository.delete(room);
                    emptySince.remove(name);
                }
            }
        }
    }
}
