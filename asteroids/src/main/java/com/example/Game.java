package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.characters.Projectile;
import com.example.helpers.CharacterManager;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Game {
    private int frameWidth;
    private int frameHeight;
    private Scene scene;
    private Map<KeyCode, Boolean> pressedKeys;
    private CharacterManager characterManager;
    private Pane pane;
    private Text text;
    private AtomicInteger points;

    public Game(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.pressedKeys = new HashMap<>();
        this.characterManager = new CharacterManager(frameWidth, frameHeight);
        this.pane = new Pane();
        this.text = new Text(10, 20, "Points: 0");
        this.points = new AtomicInteger();
    }

    public void start() {
        pane.setPrefSize(frameWidth, frameHeight);
        pane.getChildren().add(text);
        pane.getChildren().add(characterManager.getShipCharacter());
        pane.getChildren().addAll(characterManager.getAsteroidCharacters());

        Scene scene = new Scene(pane);

        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        startAnimationTimer();

        this.scene = scene;
    }

    private void startAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                handleKeyPress();

                characterManager.moveAllCharacters();

                if (characterManager.shipCollisionOccured()) {
                    stop();
                }

                handleProjectileCollision();
                addRandomAsteroid();
            }
        }.start();
    }

    private void handleKeyPress() {
        if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
            characterManager.turnShipLeft();
        }

        if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
            characterManager.turnShipRight();
        }

        if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
            characterManager.accelerateShip();
        }

        if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
            characterManager.decelerateShip();
        }

        if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && characterManager.getProjectiles().size() < 3) {
            Projectile projectile = characterManager.generateProjectile();
            this.pane.getChildren().add(projectile.getCharacter());
        }
    }

    private void handleProjectileCollision() {
        List<Polygon> collidedCharacters = characterManager.getProjectileCollision();
        if (!(collidedCharacters.isEmpty())) {
            text.setText("Points: " + points.addAndGet(1000));
            pane.getChildren().removeAll(collidedCharacters);
        }
    }

    private void addRandomAsteroid() {
        Polygon asteroidCharacter = characterManager.generateRandomAsteroid();
        if (asteroidCharacter != null) {
            pane.getChildren().add(asteroidCharacter);
        }
    }

    public Scene getScene() {
        return this.scene;
    }

}
