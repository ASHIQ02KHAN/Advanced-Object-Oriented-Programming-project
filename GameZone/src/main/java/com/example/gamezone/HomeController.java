package com.example.gamezone;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class HomeController {

    @FXML
    void openTicTacToe(ActionEvent event) throws IOException {
        loadSceneWithTransition(event, "tictactoe-view.fxml", "Tic Tac Toe");
    }

    @FXML
    void openHangman(ActionEvent event) throws IOException {
        loadSceneWithTransition(event, "hangman-view.fxml", "Hangman");
    }

    @FXML
    void openSnakeGame(ActionEvent event) throws IOException {
        loadSceneWithTransition(event, "snakegame-view.fxml", "Snake Game");
    }

    private void loadSceneWithTransition(ActionEvent event, String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle(title);

        // Scale animation effect on scene load
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), scene.getRoot());
        scaleTransition.setFromX(0.8);
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();

        stage.setScene(scene);
        stage.show();
    }
}
