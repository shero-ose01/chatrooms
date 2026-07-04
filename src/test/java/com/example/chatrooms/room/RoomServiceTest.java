package com.example.chatrooms.room;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {
    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomSessionService roomSessionService;

    @InjectMocks
    RoomService roomService;

    @Test
    void createRoomNameTrimmedAndSaved(){
        when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));
        RoomSummary sum = roomService.createRoom("   General    ");

        assertThat(sum.name()).isEqualTo("General");
    }

    @Test
    void createRoomDuplicate(){
        when(roomRepository.existsByNameIgnoreCase("General")).thenReturn(true);
        assertThatThrownBy(()->
            roomService.createRoom("General")
        ).isInstanceOf(RoomNameTakenException.class);
        verify(roomRepository, never()).save(any());
    }

    @Test
    void getRoomUnknown(){
        when(roomRepository.findByNameIgnoreCase("General")).thenReturn(Optional.empty());
        assertThatThrownBy(()->roomService.getRoom("General")).isInstanceOf(RoomNotFoundException.class);
    }

}
