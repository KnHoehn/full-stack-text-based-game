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

    // Endpoint for adding a new score to the database
    @PostMapping("/scores")
    public ResponseEntity<Score> createScore(@RequestBody Score score) {

        // Calls the score service to create the score and insert it to the database
        Score newScore = scoreService.createScore(score);

        // TODO: do i really need to be returning the score here?
        return ResponseEntity.ok(newScore);
    }

    // Endpoint for retrieving the leaderboard
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboard() {

        // calls the score service to get the leaderboard entries from the database
        return ResponseEntity.ok(scoreService.getLeaderboard());
    }

    // Endpoint for retrieving the user's personal best scores
    @GetMapping("/personal-best/{userName}")
    public ResponseEntity<List<Score>> getPersonalBest(@PathVariable String userName) {
        // Calls te score service to get the user's personal best entries from the database
        return ResponseEntity.ok(scoreService.getPersonalBest(userName));
    }
}
