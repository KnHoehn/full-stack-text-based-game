package com.hoehn.game.service;

import com.hoehn.game.entities.Score;
import com.hoehn.game.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hoehn.game.dto.ScoreResponse;

import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    // Saves the score to the database
    public void createScore(Score score) {

        scoreRepository.save(score);

    }

    // Retrieves the top 10 scores in descending order
    public List<ScoreResponse> getLeaderboard() {

        return scoreRepository.findTop10ByOrderByScoreDesc()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Retrieves the top 10 scores for the user in descending order
    public List<ScoreResponse> getPersonalBest(String userName) {

        return scoreRepository.findTop10ByUser_UserNameOrderByScoreDesc(userName)
                .stream()
                .map(this::convertToResponse)
                .toList();

    }

    // Creates a score response that can be sent to the frontend
    private ScoreResponse convertToResponse(Score score) {

        return new ScoreResponse(
                score.getId(),
                score.getUser().getUserName(),
                score.getScore(),
                score.getMoves(),
                score.getTime(),
                score.getTheme()
        );
    }
}
