package com.example.chatrooms.chat;

import java.time.Instant;

public record ChatMessage(String sender, String content, Instant sentAt) {
}
