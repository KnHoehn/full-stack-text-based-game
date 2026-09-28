package com.hoehn.game.dto;

// DTO for sending user information to the frontend.

public class UserResponse {

    private String userName;

    public UserResponse(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}