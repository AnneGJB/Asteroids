package com.example;

import com.example.helpers.HighScoreManager;

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

public class UserInterface {
    private int width;
    private int height;
    private Stage window;
    private Scene startScene;
    private Scene endScene;
    private Scene greatScoreScene;
    private HighScoreManager highScoreManager;
    private Game game;

    public UserInterface(Stage window) {
        this.width = AsteroidsApplication.WIDTH;
        this.height = AsteroidsApplication.HEIGHT;
        this.window = window;
        this.highScoreManager = new HighScoreManager();
        createStartScene();
        createEndScene();
        createGreatScoreScene();
    }

    private void createStartScene() {
        BorderPane startLayout = new BorderPane();
        startLayout.setPrefSize(this.width, this.height);
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            startGame();
        });
        startLayout.setCenter(startButton);

        this.startScene = new Scene(startLayout);
    }

    private void createEndScene() {
        BorderPane endLayout = new BorderPane();
        endLayout.setPrefSize(this.width, this.height);
        endLayout.setPadding(new Insets(20));

        TableView<Score> table = getHighScoreTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(this.width * 0.5);
        table.setMaxHeight(this.height * 0.68);

        Button tryAgainButton = new Button("Try again");
        tryAgainButton.setOnAction(event -> {
            startGame();
        });

        VBox endInterface = new VBox();
        endInterface.getChildren().setAll(table, tryAgainButton);
        endInterface.setMaxWidth(this.width / 2);

        endLayout.setCenter(endInterface);
        this.endScene = new Scene(endLayout);
    }

    private void createGreatScoreScene() {
        BorderPane greatScoreLayout = new BorderPane();
        greatScoreLayout.setPrefSize(this.width, this.height);

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
        greatScoreInterface.setMaxHeight(this.height / 2);
        greatScoreInterface.setMaxWidth(this.width / 2);

        greatScoreLayout.setCenter(greatScoreInterface);
        this.greatScoreScene = new Scene(greatScoreLayout);
    }

    private void startGame() {
        this.game = new Game(highScoreManager);
        game.setOnGameOver(() -> {
            if (highScoreManager.isHighScore()) {
                window.setScene(this.greatScoreScene);
            } else {
                window.setScene(this.endScene);
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

    public Scene getStartScene() {
        return this.startScene;
    }

}
