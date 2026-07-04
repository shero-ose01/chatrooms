package com.example.chatrooms.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record CreateRoomRequest(
        @NotBlank
        @Size(min = 3, max=30)
        @Pattern(regexp = "[A-Za-z0-9_-](?:[A-Za-z0-9 _-]*[A-Za-z0-9_-])?", message = "...")
        String name) {
}
