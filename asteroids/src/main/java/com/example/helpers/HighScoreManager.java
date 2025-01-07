package com.example.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

import com.example.Score;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HighScoreManager {
    private ObservableList<Score> highScores;
    private String filePath;
    private int latestPoints;
    private static final int NUMBER_OF_HIGH_SCORES = 10;

    public HighScoreManager() {
        this.highScores = FXCollections.observableArrayList();
        this.filePath = "asteroids\\highscores.csv";
        extractHighScoresFromFile();
    }

    private void extractHighScoresFromFile() {
        try (Scanner fileScanner = new Scanner(Paths.get(this.filePath))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] highScoreParts = line.split(",");
                Score score = new Score(Integer.parseInt(highScoreParts[0]), highScoreParts[1]);
                this.highScores.add(score);
            }

        } catch (IOException exception) {
            System.out.println("Problem with file scanner: ");
            System.out.println(exception.getMessage());

        }
        while (this.highScores.size() < NUMBER_OF_HIGH_SCORES) {
            this.highScores.add(new Score(0, "---"));
        }
        setRanks();
    }

    private void setRanks() {
        this.highScores.sort((a, b) -> b.getScore() - a.getScore());
        for (int i = 0; i < NUMBER_OF_HIGH_SCORES; i++) {
            this.highScores.get(i).setRank(i + 1);
        }
    }

    public ObservableList<Score> getHighScores() {
        return this.highScores;
    }

    public boolean isHighScore() {
        return this.latestPoints > this.highScores.get(this.highScores.size() - 1).getScore();
    }

    public void addScore(String name) {
        Score newScore = new Score(this.latestPoints, name);
        this.highScores.remove(9);
        this.highScores.add(newScore);
        setRanks();
        try (PrintWriter writer = new PrintWriter(this.filePath)) {
            for (Score highScore : this.highScores) {
                writer.println(highScore.getScore() + "," + highScore.getName());
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Problem with print writer: ");
            System.out.println(exception.getMessage());
        }
    }

    public void setLatestPoints(int points) {
        this.latestPoints = points;
    }

    public int getLatestPoints() {
        return this.latestPoints;
    }

}
