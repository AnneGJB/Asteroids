package com.example;

import com.example.helpers.HighScoreManager;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 400;
    private HighScoreManager highScoreManager = new HighScoreManager();
    private Game game;

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

    @Override
    public void start(Stage window) {

        // Start layout
        BorderPane startLayout = new BorderPane();
        startLayout.setPrefSize(WIDTH, HEIGHT);
        Button startButton = new Button("Start");
        startLayout.setCenter(startButton);

        Scene startScene = new Scene(startLayout);


        // End layout
        BorderPane endLayout = new BorderPane();
        endLayout.setPrefSize(WIDTH, HEIGHT);
        endLayout.setPadding(new Insets(20));
        
        TableView<Score> table = getHighScoreTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(WIDTH * 0.5);
        table.setMaxHeight(HEIGHT * 0.68);

        Button tryAgainButton = new Button("Try again");

        VBox endInterface = new VBox();
        endInterface.getChildren().setAll(table, tryAgainButton);
        endInterface.setMaxWidth(WIDTH/2);

        endLayout.setCenter(endInterface);
        Scene endScene = new Scene(endLayout);

        
        // Great score layout
        BorderPane greatScoreLayout = new BorderPane();
        greatScoreLayout.setPrefSize(WIDTH, HEIGHT);

        Text greatScoreText = new Text("Great Score!");
        
        HBox greatScoreInputBox = new HBox();
        Label greatScoreNameLabel = new Label("Name: ");
        TextField greatScoreTextField = new TextField();
        Button greatScoreButton = new Button("Submit");
        greatScoreButton.setOnAction(event -> {
            highScoreManager.addScore(greatScoreTextField.getText());
            greatScoreTextField.setText("");
            window.setScene(endScene);
        });
        greatScoreInputBox.getChildren().addAll(greatScoreNameLabel, greatScoreTextField, greatScoreButton);
        
        VBox greatScoreInterface = new VBox();
        greatScoreInterface.getChildren().addAll(greatScoreText, greatScoreInputBox);
        greatScoreInterface.setMaxHeight(HEIGHT/2);
        greatScoreInterface.setMaxWidth(WIDTH/2);

        greatScoreLayout.setCenter(greatScoreInterface);
        Scene greatScoreScene = new Scene(greatScoreLayout);


        
        startButton.setOnAction(event -> {
            startGame(window, endScene, greatScoreScene);
        });

        tryAgainButton.setOnAction(event -> {
            startGame(window, endScene, greatScoreScene);
        });

        window.setTitle("Asteroids!");
        window.setScene(startScene);
        window.show();
    }

    private void startGame(Stage window, Scene endScene, Scene greatScoreScene) {
        this.game = new Game(WIDTH, HEIGHT, highScoreManager);
        game.setOnGameOver(() -> {
            if(highScoreManager.isHighScore()){
                window.setScene(greatScoreScene);
            } else {
                window.setScene(endScene);
            }
        });
        game.start();
        window.setScene(game.getScene());
    }

    private TableView<Score> getHighScoreTable() {
        ObservableList<Score> highScores = highScoreManager.getHighScores();

        TableView<Score> table = new TableView<>();
        table.setItems(highScores);

        TableColumn<Score, Integer> rankColumn = new TableColumn<Score, Integer>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory("rank"));
        TableColumn<Score, Integer> scoreColumn = new TableColumn<Score, Integer>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory("score"));
        TableColumn<Score, String> nameColumn = new TableColumn<Score, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));

        table.getColumns().setAll(rankColumn, scoreColumn, nameColumn);

        return table;
    }

}
