package com.hoehn.game.dto;

public class RegisterUserResponse {

// DTO for sending the result and message of a user registration attempt to the frontend.

    private boolean success;
    private String message;

    public RegisterUserResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
