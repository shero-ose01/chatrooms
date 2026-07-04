package com.example.chatrooms.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // roll back DB changes after each test so tests stay isolated
public class RoomApiTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void createRoom_paddedNameBelowMinLength_isRejected() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"a  \"}"))
        .andExpect(status().isBadRequest());
    }

    @Test
    void createRoom_validName_returnsCreatedRoom() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Testroom\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Testroom"))
        .andExpect(jsonPath("$.userCount").value(0));
    }

    @Test
    void createRoom_duplicateNameDifferentCase_isConflict() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Duplicate\"}"))
        .andExpect(status().isCreated());

        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"duplicate\"}"))
        .andExpect(status().isConflict());
    }

    @Test
    void createRoom_illegalCharacters_isRejected() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"bad/name\"}"))
        .andExpect(status().isBadRequest());
    }

    @Test
    void getRoom_unknownName_isNotFound() throws Exception {
        mockMvc.perform(get("/rooms/DoesNotExist"))
        .andExpect(status().isNotFound());
    }

    @Test
    void getRoom_differentCase_returnsCanonicalName() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"CasedRoom\"}"))
        .andExpect(status().isCreated());

        mockMvc.perform(get("/rooms/casedroom"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("CasedRoom"));
    }

    @Test
    void listRooms_containsCreatedRoom() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ListedRoom\"}"))
        .andExpect(status().isCreated());

        mockMvc.perform(get("/rooms"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.name == 'ListedRoom')]").exists());
    }
}
