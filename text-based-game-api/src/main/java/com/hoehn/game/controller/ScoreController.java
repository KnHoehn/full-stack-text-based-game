package com.hoehn.game.controller;

import com.hoehn.game.entities.Score;
import com.hoehn.game.entities.User;
import com.hoehn.game.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/scores")
    public ResponseEntity<Score> createScore(@RequestBody Score score) {

        Score newScore = scoreService.createScore(score);

        return ResponseEntity.ok(newScore);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboard() {
        return ResponseEntity.ok(scoreService.getLeaderboard());
    }

    @GetMapping("/personal-best/{userName}")
    public ResponseEntity<List<Score>> getPersonalBest(@PathVariable String userName) {
        return ResponseEntity.ok(scoreService.getPersonalBest(userName));
    }
}
