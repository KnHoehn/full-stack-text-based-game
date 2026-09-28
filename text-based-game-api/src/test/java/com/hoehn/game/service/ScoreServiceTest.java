package com.hoehn.game.service;

import com.hoehn.game.dto.ScoreResponse;
import com.hoehn.game.entities.Score;
import com.hoehn.game.entities.User;
import com.hoehn.game.repository.ScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    private ScoreService createScoreService() {
        return new ScoreService(scoreRepository);
    }

    @Test
    void createScore() {
        Score score = new Score();

        ScoreService scoreService = createScoreService();

        scoreService.createScore(score);

        verify(scoreRepository).save(score);
    }

    @Test
    void getLeaderboard() {
        Score score = createTestScore();

        when(scoreRepository.findTop10ByOrderByScoreDesc())
                .thenReturn(List.of(score));

        ScoreService scoreService = createScoreService();

        List<ScoreResponse> result =
                scoreService.getLeaderboard();

        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().id());
        assertEquals("testUser", result.getFirst().userName());
        assertEquals(1000, result.getFirst().score());
        assertEquals(5, result.getFirst().moves());
        assertEquals(120, result.getFirst().time());
        assertEquals("Space", result.getFirst().theme());
    }

    @Test
    void getPersonalBest() {
        Score score = createTestScore();

        when(scoreRepository.findTop10ByUser_UserNameOrderByScoreDesc("testUser"))
                .thenReturn(List.of(score));

        ScoreService scoreService = createScoreService();

        List<ScoreResponse> result =
                scoreService.getPersonalBest("testUser");

        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().id());
        assertEquals("testUser", result.getFirst().userName());
        assertEquals(1000, result.getFirst().score());
        assertEquals(5, result.getFirst().moves());
        assertEquals(120, result.getFirst().time());
        assertEquals("Space", result.getFirst().theme());

        verify(scoreRepository)
                .findTop10ByUser_UserNameOrderByScoreDesc("testUser");
    }

    private Score createTestScore() {
        User user = new User();
        user.setUserName("testUser");

        Score score = new Score();
        score.setId(1);
        score.setUser(user);
        score.setScore(1000);
        score.setMoves(5);
        score.setTime(120);
        score.setTheme("Space");

        return score;
    }

    @Test
    void getLeaderboardWithNoScores() {
        when(scoreRepository.findTop10ByOrderByScoreDesc())
                .thenReturn(List.of());

        ScoreService scoreService = createScoreService();

        List<ScoreResponse> result = scoreService.getLeaderboard();

        assertEquals(0, result.size());
    }

    @Test
    void getPersonalBestWithNoScores() {
        when(scoreRepository.findTop10ByUser_UserNameOrderByScoreDesc("testUser"))
                .thenReturn(List.of());

        ScoreService scoreService = createScoreService();

        List<ScoreResponse> result = scoreService.getPersonalBest("testUser");

        assertEquals(0, result.size());
    }
}