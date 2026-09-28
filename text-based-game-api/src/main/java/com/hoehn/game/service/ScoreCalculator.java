package com.hoehn.game.service;

public final class ScoreCalculator {

    // Variable constant representing the amount of seconds in 10 minutes.
    private static final int NUM_SECONDS_IN_TEN_MINUTES = 600;
    // Variable constant to zero out the user's scores if they go negative.
    private static final int MIN_SCORE = 0;


    private ScoreCalculator() {}

    // This method calculates the user's total end score.
    public static Long calculateScore(final Long totalTime, final int moveScore) {

        int finalMoveScore = moveScore;

        long totalScore;

        // Starts at 600 because anything more than ten minutes would result in a time modifier of 0
        long timeScore = NUM_SECONDS_IN_TEN_MINUTES;

        // Subtracts the amount of time it took the user to beat the game from timeScore and zeros out the score if the user took longer than ten minutes
        if (totalTime < NUM_SECONDS_IN_TEN_MINUTES) {
            timeScore = timeScore - totalTime;
        } else {
            timeScore = MIN_SCORE;
        }

        // Zero's out moveScore if it ends up negative
        if (moveScore < MIN_SCORE) {
            finalMoveScore = MIN_SCORE;
        }

        // If timeScore is greater than 0, calculate the total score with the time multiplier.
        if (timeScore > MIN_SCORE) {
            totalScore = finalMoveScore * timeScore;
            // Else the total score is just the finalMoveScore.
        } else {
            totalScore = finalMoveScore;
        }

        return totalScore;
    }
}
