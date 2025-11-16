package com.example.gamezone;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HangmanController {

    private final String wordToGuess = "ELEVEN";
    private final StringBuilder guessedWord = new StringBuilder("______");
    private int attempts = 0;
    private final int maxAttempts = 6;

    @FXML private Label lblWord;
    @FXML private Label lblStatus;
    @FXML private ImageView hangmanImage;
    @FXML private GridPane alphabetGrid;

    @FXML
    public void initialize() {
        lblWord.setText(formatWord());
        updateHangmanImage();

        // Create alphabet buttons A-Z
        char letter = 'A';
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 7 && letter <= 'Z'; col++) {
                Button btn = new Button(String.valueOf(letter));
                btn.setPrefSize(40, 40);
                btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: #1abc9c; -fx-font-weight: bold;");
                btn.setOnAction(this::handleLetterClick);
                alphabetGrid.add(btn, col, row);
                letter++;
            }
        }
    }

    private void handleLetterClick(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        clicked.setDisable(true);
        String letter = clicked.getText();
        boolean found = false;

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter.charAt(0)) {
                guessedWord.setCharAt(i, letter.charAt(0));
                found = true;
            }
        }

        if (!found) {
            attempts++;
            updateHangmanImage();
        }

        lblWord.setText(formatWord());

        if (guessedWord.toString().equals(wordToGuess)) {
            lblStatus.setText("🎉 You Won!");
            disableAllButtons();
        } else if (attempts >= maxAttempts) {
            lblStatus.setText("☠ GAME OVER");
            lblWord.setText(revealWord());
            disableAllButtons();
        }
    }

    private void updateHangmanImage() {
        String imgPath = "/com/example/gamezone/images/hangman" + attempts + ".png";
        hangmanImage.setImage(new Image(getClass().getResourceAsStream(imgPath)));
    }

    private void disableAllButtons() {
        for (Node node : alphabetGrid.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
    }

    private String formatWord() {
        StringBuilder spaced = new StringBuilder();
        for (char c : guessedWord.toString().toCharArray()) {
            spaced.append(c).append(" ");
        }
        return spaced.toString().trim();
    }

    private String revealWord() {
        StringBuilder spaced = new StringBuilder();
        for (char c : wordToGuess.toCharArray()) {
            spaced.append(c).append(" ");
        }
        return spaced.toString().trim();
    }

    @FXML
    void backToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setTitle("Game Zone");
    }
}
