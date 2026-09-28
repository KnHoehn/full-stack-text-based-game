package com.hoehn.game.dto;

// DTO for sending the result and message of a user registration attempt to the frontend.

public record RegisterUserResponse(
        boolean success,
        String message) {
}