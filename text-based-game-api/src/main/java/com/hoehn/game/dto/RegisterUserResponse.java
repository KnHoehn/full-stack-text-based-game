package com.hoehn.game.dto;

public class RegisterUserResponse {



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
