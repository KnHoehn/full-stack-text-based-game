package com.hoehn.game.service;

import com.hoehn.game.entities.Score;
import com.hoehn.game.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Score> getLeaderboard() {

        return scoreRepository.findTop10ByOrderByScoreDesc();

    }

    public List<Score> getPersonalBest(String userName) {

        return scoreRepository.findTop10ByUserNameOrderByScoreDesc(userName);

    }
}
