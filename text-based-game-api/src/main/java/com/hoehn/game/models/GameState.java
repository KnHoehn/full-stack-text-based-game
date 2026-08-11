package com.hoehn.game.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GameState {

    private final String gameId;
    private String theme;
    private String userName;
    private String currentRoom;
    private List<String> inventory;
    private int moves;
    private int moveScore;
    private final long startTime;
    private boolean gameOver;

    public GameState() {
        this.moves = 0;
        this.moveScore = 1000;
        this.gameId = UUID.randomUUID().toString();
        this.startTime = new Date().getTime();
        this.inventory = new ArrayList<>();
        this.gameOver = false;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getGameId() {
        return gameId;
    }

    public String getTheme() {
        return theme;
    }

    public String getUserName() {
        return userName;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public int getMoves() {
        return moves;
    }

    public int getMoveScore() {
        return moveScore;
    }

    public void incrementMoves() { this.moves++; }

    public void scorePenalty(int movePenalty) { this.moveScore -= movePenalty; }

    public void addToInventor(String item) {
        inventory.add(item);
    }

}
