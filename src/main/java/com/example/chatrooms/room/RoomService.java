package com.example.chatrooms.room;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
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

    private RoomSummary toSummary(Room room){
        return new RoomSummary(room.getName(), room.getCreatedAt(),0);
    }
}
