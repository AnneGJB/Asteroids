package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
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
        

        BorderPane startLayout = new BorderPane();
        startLayout.setPrefSize(WIDTH, HEIGHT);
        Button startButton = new Button("Start");
        startLayout.setCenter(startButton);
                
        Scene startScene = new Scene(startLayout);
        

        BorderPane endLayout = new BorderPane();
        endLayout.setPrefSize(WIDTH, HEIGHT);
        Button tryAgainButton = new Button("Try again");
        endLayout.setCenter(tryAgainButton);
        Text text = new Text();
        text.setText("1. 30200 John Doe  \n2. 29900 Peter Smith");
        endLayout.setTop(text);
        
        Scene endScene = new Scene(endLayout);
        

        startButton.setOnAction(event -> {
            startGame(window, endScene);
        });

        tryAgainButton.setOnAction(event -> {
            startGame(window, endScene);
        });


        window.setTitle("Asteroids!");
        window.setScene(startScene);
        window.show();
    }

    private void startGame(Stage window, Scene endScene) {
        this.game = new Game(WIDTH, HEIGHT);
            game.setOnGameOver(() -> window.setScene(endScene));
            game.start();
            window.setScene(game.getScene());
    }

}
