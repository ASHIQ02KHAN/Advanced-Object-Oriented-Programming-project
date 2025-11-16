package com.example.gamezone;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SnakeGameController implements Initializable {

    @FXML private StackPane canvasContainer;
    @FXML private Label lblScore;

    private SnakeGameCanvas canvas;

    public interface ScoreNotifier {
        void onScoreChange(int score);
        void onGameOver(int finalScore);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas = new SnakeGameCanvas(new ScoreNotifier() {
            @Override
            public void onScoreChange(int score) {
                lblScore.setText("Length: " + score);  // ✅ Changed here
            }

            @Override
            public void onGameOver(int finalScore) {
                showGameOverDialog(finalScore);
            }
        });

        canvasContainer.getChildren().add(canvas);
        canvas.requestFocus();
    }

    @FXML
    void restartGame() {
        canvas.resetGame();
        canvas.requestFocus();
    }

    @FXML
    void backToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setTitle("Game Zone");
    }

    private void showGameOverDialog(int score) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("🐍 Snake Crashed!");
        alert.setContentText("Your final length: " + score + "\nClick OK to continue.");  // ✅ Length not score
        alert.showAndWait();
    }
}
