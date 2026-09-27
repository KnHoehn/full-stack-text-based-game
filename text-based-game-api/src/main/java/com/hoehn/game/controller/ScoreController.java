package com.hoehn.game.controller;

import com.hoehn.game.entities.Score;
import com.hoehn.game.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import java.util.List;
import com.hoehn.game.dto.ScoreResponse;

@RestController
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    // Endpoint for retrieving the leaderboard
    @GetMapping("/leaderboard")
    public ResponseEntity<List<ScoreResponse>> getLeaderboard() {

        // calls the score service to get the leaderboard entries from the database
        return ResponseEntity.ok(scoreService.getLeaderboard());
    }

    @GetMapping("/scores/me")
    public ResponseEntity<List<ScoreResponse>> getMyScores(Authentication authentication) {

        String userName = authentication.getName();

        return ResponseEntity.ok(scoreService.getPersonalBest(userName));
    }
}
