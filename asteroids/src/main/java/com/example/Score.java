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
        return score;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
