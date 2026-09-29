package com.hoehn.game.controller;

import com.hoehn.game.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.hoehn.game.dto.GameResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Test
    void startGame() throws Exception {
        GameResponse gameResponse = new GameResponse(
                "game123",
                "Space Adventure",
                "A test adventure.",
                "Starting Room",
                "A mysterious room.",
                java.util.List.of(),
                false,
                "",
                "Game started.",
                1000,
                0,
                0
        );

        when(gameService.startGame("testUser", "Space"))
                .thenReturn(gameResponse);

        Authentication authentication = org.mockito.Mockito.mock(
                Authentication.class
        );

        when(authentication.getName())
                .thenReturn("testUser");

        mockMvc.perform(post("/games")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "theme": "Space"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("game123"))
                .andExpect(jsonPath("$.gameName").value("Space Adventure"))
                .andExpect(jsonPath("$.story").value("A test adventure."))
                .andExpect(jsonPath("$.currentRoom").value("Starting Room"));

        verify(gameService).startGame("testUser", "Space");
    }

    @Test
    void processCommand() throws Exception {
        GameResponse gameResponse = new GameResponse(
                "game123",
                "Space Adventure",
                "A test adventure.",
                "North Room",
                "A northern room.",
                java.util.List.of(),
                false,
                "You moved north.",
                "",
                1000,
                1,
                5
        );

        when(gameService.processCommand("game123", "go north"))
                .thenReturn(gameResponse);

        mockMvc.perform(post("/games/game123/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "command": "go north"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("game123"))
                .andExpect(jsonPath("$.currentRoom").value("North Room"))
                .andExpect(jsonPath("$.movementMessage").value("You moved north."));

        verify(gameService).processCommand("game123", "go north");
    }
}