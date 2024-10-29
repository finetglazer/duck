package com.banker.duckrace.model;

public class Bet {
    private String playerId;     // The unique ID of the player placing the bet
    private int candidateId;     // The ID of the candidate (duck) the player is betting on
    private int amount;          // The amount of points the player is betting

    public Bet(String playerId, int candidateId, int amount) {
        this.playerId = playerId;
        this.candidateId = candidateId;
        this.amount = amount;
    }

    // Getter and Setter methods

    public String getPlayerId() {
        return playerId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public int getAmount() {
        return amount;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
