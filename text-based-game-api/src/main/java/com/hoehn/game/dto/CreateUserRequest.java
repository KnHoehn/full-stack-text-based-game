package com.hoehn.game.dto;

// DTO for receiving username and password when creating a new user.

public class CreateUserRequest {

    private String userName;
    private String password;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}