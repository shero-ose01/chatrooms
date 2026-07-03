package com.example.chatrooms.room;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomSessionService roomSessionService;

    public RoomService(RoomRepository roomRepository, RoomSessionService roomSessionService){
        this.roomRepository = roomRepository;
        this.roomSessionService = roomSessionService;
    }

    public RoomSummary createRoom(String rawName){
        String trimmedName = rawName.trim();

        if(roomRepository.existsByNameIgnoreCase(trimmedName)){
            throw new RoomNameTakenException();
        }
        Room saved = roomRepository.save(new Room(trimmedName));
        return this.toSummary(saved);
    }

    public List<RoomSummary> listRooms(){
        return roomRepository.findAll().stream().map(this::toSummary).toList();
    }

    public RoomSummary getRoom(String name){
        return roomRepository.findByNameIgnoreCase(name.trim()).map(this::toSummary).orElseThrow(RoomNotFoundException::new);
    }

    private RoomSummary toSummary(Room room){
        int count  = (int) roomSessionService.countInRoom(room.getName());
        return new RoomSummary(room.getName(), room.getCreatedAt(),count);
    }
}
