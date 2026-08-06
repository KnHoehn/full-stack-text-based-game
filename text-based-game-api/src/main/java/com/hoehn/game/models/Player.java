package com.hoehn.game.models;

/** This class creates Player objects. */
public class Player {

    /** Variable to keep track of player moves. */
    private int moves;
    /** Variable to keep track of the player's move score. */
    private int moveScore;

    /** Default constructor that initializes the player with a move count of 0 and a move score of 1000. */
    public Player() {
        this.moves = 0;
        this.moveScore = 1000;
    }

    /** Gets the player's moves.
     *
     * @return the player's total moves.
     * */
    public int getMoves() {
        return moves;
    }

    /** Gets the player's move score.
     *
     * @return the player's total move score.
     * */
    public int getMoveScore() {
        return moveScore;
    }

    /** Increments the player's move score by 1. */
    public void incrementMoves() { this.moves++; }

    /** Decrements the player's move score given the penalty.
     *
     * @param movePenalty the penalty to decrement the move score by.
     * */
    public void scorePenalty(int movePenalty) { this.moveScore -= movePenalty; }

}
