package com.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 400;
    private Game game;

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

    @Override
    public void start(Stage window) {
        this.game = new Game(WIDTH, HEIGHT);

        game.start();

        window.setTitle("Asteroids!");
        window.setScene(game.getScene());
        window.show();
    }

}
