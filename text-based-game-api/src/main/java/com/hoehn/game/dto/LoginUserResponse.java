package com.hoehn.game.dto;

public class LoginUserResponse {

    private String token;

    public LoginUserResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}