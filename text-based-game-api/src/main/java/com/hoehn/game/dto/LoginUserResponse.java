package com.hoehn.game.dto;

// DTO for sending the authentication token to the frontend after login.

public record LoginUserResponse(String token) {
}