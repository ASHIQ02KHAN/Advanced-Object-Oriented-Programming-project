package com.example.gamezone;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TicTacToeController {

    @FXML private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML private Label winnerLabel;

    private boolean xTurn = true;

    @FXML
    void makeMove(ActionEvent event) {
        Button btn = (Button) event.getSource();
        if (btn.getText().isEmpty()) {
            btn.setText(xTurn ? "X" : "O");
            xTurn = !xTurn;
            checkWinner();
        }
    }

    void checkWinner() {
        String[][] board = {
                {btn00.getText(), btn01.getText(), btn02.getText()},
                {btn10.getText(), btn11.getText(), btn12.getText()},
                {btn20.getText(), btn21.getText(), btn22.getText()}
        };

        // Rows, Columns, Diagonals Check
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                winnerLabel.setText("The winner is: " + board[i][0]);
                return;
            }
            if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                winnerLabel.setText("The winner is: " + board[0][i]);
                return;
            }
        }

        if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            winnerLabel.setText("The winner is: " + board[0][0]);
            return;
        }

        if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            winnerLabel.setText("The winner is: " + board[0][2]);
        }
    }

    @FXML
    void resetGame() {
        Button[] buttons = {btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22};
        for (Button btn : buttons) {
            btn.setText("");
        }
        winnerLabel.setText("");
        xTurn = true;
    }

    @FXML
    void backToHome(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
        stage.setTitle("Game Zone");
    }
}
