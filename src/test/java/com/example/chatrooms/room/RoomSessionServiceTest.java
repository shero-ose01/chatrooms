package com.example.chatrooms.room;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RoomSessionServiceTest {

    @Test
    void countInRoomTest(){
        RoomSessionService service = new RoomSessionService();

        service.join("1","A","p1");
        service.join("2","A","p2");
        service.join("3","B","p3");

        service.leave("1");

        assertThat(service.countInRoom("A")).isEqualTo(1);
        assertThat(service.countInRoom("B")).isEqualTo(1);
    }
}
