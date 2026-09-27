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

    public Score createScore(Score score) {

        return scoreRepository.save(score);

    }

    // Calls the score repository to retrieve the top 10 scores in descending order
    public List<ScoreResponse> getLeaderboard() {

        return scoreRepository.findTop10ByOrderByScoreDesc()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<ScoreResponse> getPersonalBest(String userName) {

        // Calls the score repository to retrieve the top 10 scores from a user in descending order
        return scoreRepository.findTop10ByUser_UserNameOrderByScoreDesc(userName)
                .stream()
                .map(this::convertToResponse)
                .toList();

    }

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
