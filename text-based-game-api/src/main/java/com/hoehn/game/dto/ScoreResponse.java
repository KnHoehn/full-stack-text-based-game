package com.hoehn.game.dto;

public class ScoreResponse {

    private int id;
    private String userName;
    private int score;
    private int moves;
    private int time;
    private String theme;

    public ScoreResponse(
            int id,
            String userName,
            int score,
            int moves,
            int time,
            String theme) {

        this.id = id;
        this.userName = userName;
        this.score = score;
        this.moves = moves;
        this.time = time;
        this.theme = theme;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public int getMoves() {
        return moves;
    }

    public int getTime() {
        return time;
    }

    public String getTheme() {
        return theme;
    }
}