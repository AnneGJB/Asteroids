package com.example;

import javafx.application.Application;

import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

    @Override
    public void start(Stage window) {
        UserInterface userInterface = new UserInterface(window);
        
        window.setTitle("Asteroids!");
        window.setScene(userInterface.getStartScene());
        window.show();
    }

}
