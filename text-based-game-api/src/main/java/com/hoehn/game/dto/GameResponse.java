package com.hoehn.game.dto;

import java.util.List;

public class GameResponse {

    private String gameId;
    private String gameName;
    private String story;
    private String currentRoom;
    private String itemDescription;
    private List<String> inventory;
    private boolean gameOver;
    private String message;
    private long score;
    private int moves;
    private long time;

    public GameResponse(
            String gameId,
            String gameName,
            String story,
            String currentRoom,
            String itemDescription,
            List<String> inventory,
            boolean gameOver,
            String message,
            long score,
            int moves,
            long time) {

        this.gameId = gameId;
        this.gameName = gameName;
        this.story = story;
        this.currentRoom = currentRoom;
        this.itemDescription = itemDescription;
        this.inventory = inventory;
        this.gameOver = gameOver;
        this.message = message;
        this.score = score;
        this.moves = moves;
        this.time = time;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public String getStory() {
        return story;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getMessage() {
        return message;
    }

    public long getScore() {
        return score;
    }

    public int getMoves() {
        return moves;
    }

    public long getTime() {
        return time;
    }
}