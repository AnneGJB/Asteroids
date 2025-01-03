package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.characters.Projectile;
import com.example.helpers.CharacterManager;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 400;

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

    @Override
    public void start(Stage window) {
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        Pane pane = new Pane();
        Text text = new Text(10, 20, "Points: 0");
        AtomicInteger points = new AtomicInteger();
        CharacterManager characterManager = new CharacterManager(WIDTH, HEIGHT);
        
        pane.setPrefSize(WIDTH, HEIGHT);
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

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    characterManager.turnShipLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    characterManager.turnShipRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    characterManager.accelerateShip();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && characterManager.getProjectiles().size() < 3) {
                    Projectile projectile = characterManager.generateProjectile();
                    pane.getChildren().add(projectile.getCharacter());
                }

                characterManager.moveAllCharacters();

                if(characterManager.shipCollisionOccured()) {
                    stop();
                }
                
                List<Polygon> collidedCharacters = characterManager.getProjectileCollision();
                if(!(collidedCharacters.isEmpty())) {
                    text.setText("Points: " + points.addAndGet(1000));
                    pane.getChildren().removeAll(collidedCharacters);
                }

                Polygon asteroidCharacter = characterManager.generateAsteroidRandomly();
                if(asteroidCharacter != null) {
                    pane.getChildren().add(asteroidCharacter);
                }
                
            }
        }.start();

        window.setTitle("Asteroids!");
        window.setScene(scene);
        window.show();
    }

}
