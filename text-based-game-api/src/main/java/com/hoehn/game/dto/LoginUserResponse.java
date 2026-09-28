package com.hoehn.game.dto;

// DTO for sending the authentication token to the frontend after login.

public class LoginUserResponse {

    private String token;

    public LoginUserResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}