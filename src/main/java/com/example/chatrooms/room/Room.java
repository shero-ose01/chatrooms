package com.example.chatrooms.room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;

@Entity
public class Room {
    @Id
    @Column(length = 30)
    private String name;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected Room(){
    }

    public Room(String name){
       this.name = name;
    }

    public String getName(){
        return name;
    }
    public Instant getCreatedAt(){
        return createdAt;
    }
}
