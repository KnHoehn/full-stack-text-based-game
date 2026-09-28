package com.hoehn.game.controller;

import com.hoehn.game.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.hoehn.game.dto.ScoreResponse;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.core.Authentication;

@WebMvcTest(ScoreController.class)
class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScoreService scoreService;

    @Test
    void getLeaderboard() throws Exception {
        List<ScoreResponse> scores = List.of(
                new ScoreResponse(
                        0,
                        "testUser",
                        1000,
                        5,
                        30,
                        "Space"
                )
        );

        when(scoreService.getLeaderboard())
                .thenReturn(scores);

        mockMvc.perform(get("/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("testUser"))
                .andExpect(jsonPath("$[0].score").value(1000))
                .andExpect(jsonPath("$[0].moves").value(5))
                .andExpect(jsonPath("$[0].time").value(30))
                .andExpect(jsonPath("$[0].theme").value("Space"));

        verify(scoreService).getLeaderboard();
    }

    @Test
    void getMyScores() throws Exception {
        List<ScoreResponse> scores = List.of(
                new ScoreResponse(
                        0,
                        "testUser",
                        1000,
                        5,
                        30,
                        "Space"
                )
        );

        when(scoreService.getPersonalBest("testUser"))
                .thenReturn(scores);

        Authentication authentication = org.mockito.Mockito.mock(
                Authentication.class
        );

        when(authentication.getName())
                .thenReturn("testUser");

        mockMvc.perform(get("/scores/me")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("testUser"))
                .andExpect(jsonPath("$[0].score").value(1000))
                .andExpect(jsonPath("$[0].moves").value(5))
                .andExpect(jsonPath("$[0].time").value(30))
                .andExpect(jsonPath("$[0].theme").value("Space"));

        verify(scoreService).getPersonalBest("testUser");
    }
}