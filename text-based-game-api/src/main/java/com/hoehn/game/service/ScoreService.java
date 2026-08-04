package com.hoehn.game.service;

import com.hoehn.game.entities.Score;
import com.hoehn.game.entities.User;
import com.hoehn.game.repository.ScoreRepository;
import com.hoehn.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

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
}
