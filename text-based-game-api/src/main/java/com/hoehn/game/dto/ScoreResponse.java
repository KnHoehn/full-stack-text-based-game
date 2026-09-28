package com.hoehn.game.dto;

// DTO for sending score information to the frontend.

public record ScoreResponse(
        int id,
        String userName,
        int score,
        int moves,
        int time,
        String theme) {
}