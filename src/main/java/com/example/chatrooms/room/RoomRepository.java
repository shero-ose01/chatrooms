package com.example.chatrooms.room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
    public boolean existsByNameIgnoreCase(String name);
}
