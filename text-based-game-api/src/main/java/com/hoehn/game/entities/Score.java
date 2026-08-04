package com.hoehn.game.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "score_board")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private int id;


    // TODO change foreign key relationship in database from username to userid instead?

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "moves", nullable = false)
    private int moves;

    @Column(name = "time", nullable = false)
    private int time;

    @Column(name = "theme", nullable = false)
    private String theme;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(String userName) {
        this.userName = userName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
