package com.example;

public class Score {
    private int rank;
    private int score;
    private String name;

    public Score(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return this.score;
    }

    public String getName() {
        return this.name;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
