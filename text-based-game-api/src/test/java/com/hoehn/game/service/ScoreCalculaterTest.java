package com.hoehn.game.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreCalculatorTest {

    @Test
    void calculateScoreUnderTenMinutes() {
        Long result = ScoreCalculator.calculateScore(100L, 1000);

        assertEquals(500000L, result);
    }

    @Test
    void calculateScoreAtTenMinutes() {
        Long result = ScoreCalculator.calculateScore(600L, 1000);

        assertEquals(1000L, result);
    }

    @Test
    void calculateScoreOverTenMinutes() {
        Long result = ScoreCalculator.calculateScore(700L, 1000);

        assertEquals(1000L, result);
    }

    @Test
    void calculateScoreWithNegativeMoveScore() {
        Long result = ScoreCalculator.calculateScore(600L, -100);

        assertEquals(0L, result);
    }

    @Test
    void calculateScoreWithNegativeMoveScoreUnderTenMinutes() {
        Long result = ScoreCalculator.calculateScore(100L, -100);

        assertEquals(0L, result);
    }
}