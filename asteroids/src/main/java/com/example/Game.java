package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.characters.Projectile;
import com.example.helpers.CharacterManager;
import com.example.helpers.HighScoreManager;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Game {
    private int frameWidth;
    private int frameHeight;
    private HighScoreManager highScoreManager;
    private Scene scene;
    private Map<KeyCode, Boolean> pressedKeys;
    private CharacterManager characterManager;
    private Pane pane;
    private Text pointsText;
    private AtomicInteger points;
    private Runnable gameOverAction;

    public Game(HighScoreManager highScoreManager) {
        this.frameWidth = AsteroidsApplication.WIDTH;
        this.frameHeight = AsteroidsApplication.HEIGHT;
        this.highScoreManager = highScoreManager;
        this.pressedKeys = new HashMap<>();
        this.characterManager = new CharacterManager();
        this.pane = new Pane();
        this.pointsText = new Text(10, 20, "Points: 0");
        this.points = new AtomicInteger();
    }

    public void start() {
        this.pane.setPrefSize(this.frameWidth, this.frameHeight);
        this.pane.getChildren().add(this.pointsText);
        this.pane.getChildren().add(this.characterManager.getShipCharacter());
        this.pane.getChildren().addAll(this.characterManager.getAsteroidCharacters());

        this.scene = new Scene(this.pane);

        this.scene.setOnKeyPressed(event -> {
            this.pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        this.scene.setOnKeyReleased(event -> {
            this.pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        startAnimationTimer();

    }

    private void startAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                handleKeyPress();

                characterManager.moveAllCharacters();

                if (characterManager.shipHasCollided()) {
                    stop();
                    runGameOver();
                }

                handleProjectileCollision();
                addRandomAsteroid();
            }
        }.start();
    }

    private void handleKeyPress() {
        if (this.pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
            this.characterManager.turnShipLeft();
        }

        if (this.pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
            this.characterManager.turnShipRight();
        }

        if (this.pressedKeys.getOrDefault(KeyCode.UP, false)) {
            this.characterManager.accelerateShip();
        }

        if (this.pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
            this.characterManager.decelerateShip();
        }

        if (this.pressedKeys.getOrDefault(KeyCode.SPACE, false) && this.characterManager.getProjectiles().size() < 3) {
            Projectile projectile = this.characterManager.generateProjectile();
            this.pane.getChildren().add(projectile.getCharacter());
        }
    }

    private void handleProjectileCollision() {
        List<Polygon> collidedCharacters = this.characterManager.getProjectileCollision();
        if (!(collidedCharacters.isEmpty())) {
            this.pointsText.setText("Points: " + this.points.addAndGet(1000));
            this.pane.getChildren().removeAll(collidedCharacters);
        }
    }

    private void addRandomAsteroid() {
        Polygon asteroidCharacter = this.characterManager.generateRandomAsteroid();
        if (asteroidCharacter != null) {
            this.pane.getChildren().add(asteroidCharacter);
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setOnGameOver(Runnable action) {
        this.gameOverAction = action;
    }

    private void runGameOver() {
        this.highScoreManager.setLatestPoints(this.points.get());
        this.gameOverAction.run();
    }

}
