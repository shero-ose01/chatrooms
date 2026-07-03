package com.example.chatrooms.room;

import java.time.Instant;

public record RoomSummary(String name, Instant createdAt, int userCount) {
}