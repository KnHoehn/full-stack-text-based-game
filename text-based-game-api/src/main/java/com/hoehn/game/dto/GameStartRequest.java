package com.hoehn.game.dto;

public class GameStartRequest {

    // DTO for a game start request

    private String userName;
    private String theme;

    public String getUserName() {
        return userName;
    }

    public String getTheme() {
        return theme;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}