package com.example.chatrooms.room;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomSessionService {
    public record Session(String roomName, String displayName){}
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public void join(String sessionId, String roomName, String displayName) {
        sessionMap.put(sessionId, new Session(roomName, displayName));
    }

    public Session leave(String sessionId){
        return sessionMap.remove(sessionId);
    }

    public long countInRoom(String roomName){
       return sessionMap.values().stream().filter(s->s.roomName().equals(roomName)).count();
    }
}
