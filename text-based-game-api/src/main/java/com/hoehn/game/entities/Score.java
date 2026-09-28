package com.hoehn.game.entities;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Entity for score_board table

@Entity
@Table(name = "score_board")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public User getUser() {
        return user;
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

    public void setUser(User user) {
        this.user = user;
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
