package com.hoehn.game.dto;

import java.util.List;

// DTO for sending game state and game information to the frontend.

public record GameResponse(
        String gameId,
        String gameName,
        String story,
        String currentRoom,
        String itemDescription,
        List<String> inventory,
        boolean gameOver,
        String movementMessage,
        String message,
        long score,
        int moves,
        long time) {
}