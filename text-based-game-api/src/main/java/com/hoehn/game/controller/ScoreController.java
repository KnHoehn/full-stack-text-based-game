package com.hoehn.game.controller;

import com.hoehn.game.entities.Score;
import com.hoehn.game.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
