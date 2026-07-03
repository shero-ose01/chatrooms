package com.example.chatrooms.room;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    public RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomSummary createRoom(@Valid @RequestBody CreateRoomRequest req){
        return roomService.createRoom(req.name());
    }

    @GetMapping
    public List<RoomSummary> listRooms(){
        return roomService.listRooms();
    }

}
