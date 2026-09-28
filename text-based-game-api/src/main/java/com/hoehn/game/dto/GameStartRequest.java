package com.hoehn.game.dto;

// DTO for receiving the player's selected game theme from the frontend.

public class GameStartRequest {

    private String theme;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}