package com.hoehn.game.dto;

// DTO for receiving a player's game command from the frontend.

public class CommandRequest {

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}