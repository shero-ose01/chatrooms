package com.example.chatrooms.chat;

import com.example.chatrooms.room.RoomNotFoundException;
import com.example.chatrooms.room.RoomRepository;
import com.example.chatrooms.room.RoomSessionService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.time.Instant;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomSessionService roomSessionService;
    private final RoomRepository roomRepository;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, RoomSessionService roomSessionService, RoomRepository roomRepository){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.roomSessionService = roomSessionService;
        this.roomRepository = roomRepository;
    }

    @MessageMapping("/room/{name}/send")
    public void sendMessage(@DestinationVariable String name, SendMessageRequest req){
        ChatMessage message = new ChatMessage(req.sender(),req.content(), Instant.now());
        simpMessagingTemplate.convertAndSend("/rooms/room/" + name, message);
    }

    @MessageMapping("/room/{name}/join")
    public void join(@DestinationVariable String name, JoinRequest req, SimpMessageHeaderAccessor accessor){
        if(!roomRepository.existsByNameIgnoreCase(name)){
            return;
        }
        roomSessionService.join(accessor.getSessionId(), name, req.sender());
        ChatMessage joinMessage = new ChatMessage("System", req.sender() + " joined", Instant.now()); // maybe add config join message later instead of hardcode
        simpMessagingTemplate.convertAndSend("/rooms/room/" + name, joinMessage);
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event){
        RoomSessionService.Session gone = roomSessionService.leave(event.getSessionId());
        if (gone != null){
            ChatMessage disconnectMessage = new ChatMessage("System", gone.displayName() + " left", Instant.now());
            simpMessagingTemplate.convertAndSend("/rooms/room/" + gone.roomName(), disconnectMessage);
        }
    }
}
