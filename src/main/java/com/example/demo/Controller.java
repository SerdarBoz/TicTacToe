package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML
    private Text winnerText;
    @FXML
    private Button restartButton;
    @FXML
    private Button restartSeriesButton;
    @FXML
    private Text scoreboardText;

    private Player currentPlayer = Player.X; // Start met speler X
    private int playerXWins = 0;
    private int playerOWins = 0;

    ArrayList<Button> buttons;
    Random random = new Random();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));

        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });

        updateScoreboard(); // Initialize the scoreboard
        restartSeriesButton.setDisable(true);
    }

    private void updateScoreboard() {
        scoreboardText.setText("Scoreboard: X - " + playerXWins + ", O - " + playerOWins);
    }

    @FXML
    void restartSeries(ActionEvent event) {
        playerXWins = 0;
        playerOWins = 0;

        restartGame(null);

        winnerText.setText("Tic-Tac-Toe");
        restartSeriesButton.setDisable(true);
        restartButton.setDisable(false);
        updateScoreboard();
    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
        currentPlayer = Player.X; // Start opnieuw met speler X
    }

    public void resetButton(Button button) {
        button.setDisable(false);
        button.setText("");
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            checkIfGameIsOver();

            if (currentPlayer == Player.O) {
                computerMove();
            }
        });
    }

    public void setPlayerSymbol(Button button) {
        button.setText(currentPlayer.toString());
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X; // Wissel tussen X en O
    }

    public void checkIfGameIsOver() {
        for (int a = 0; a < 8; a++) {
            String line;
            switch (a) {
                case 0:
                    line = button1.getText() + button2.getText() + button3.getText();
                    break;
                case 1:
                    line = button4.getText() + button5.getText() + button6.getText();
                    break;
                case 2:
                    line = button7.getText() + button8.getText() + button9.getText();
                    break;
                case 3:
                    line = button1.getText() + button5.getText() + button9.getText();
                    break;
                case 4:
                    line = button3.getText() + button5.getText() + button7.getText();
                    break;
                case 5:
                    line = button1.getText() + button4.getText() + button7.getText();
                    break;
                case 6:
                    line = button2.getText() + button5.getText() + button8.getText();
                    break;
                case 7:
                    line = button3.getText() + button6.getText() + button9.getText();
                    break;
                default:
                    line = null;
                    break;
            }

            // X wins
            if (line.equals("XXX")) {
                playerXWins++;
                winnerText.setText("X won! Score: " + playerXWins + " - " + playerOWins);
                updateScoreboard();
                disableButtons();
                checkBestOfFive();
                return;
            }

            // O wins
            if (line.equals("OOO")) {
                playerOWins++;
                winnerText.setText("O won! Score: " + playerXWins + " - " + playerOWins);
                updateScoreboard();
                disableButtons();
                checkBestOfFive();
                return;
            }
        }

        if (buttons.stream().allMatch(Button::isDisabled)) {
            winnerText.setText("It's a draw! Score: " + playerXWins + " - " + playerOWins);
            updateScoreboard();
            checkBestOfFive();
        }
    }

    private void disableButtons() {
        buttons.forEach(button -> button.setDisable(true));
    }

    private void checkBestOfFive() {
        if (playerXWins == 3) {
            winnerText.setText("X wins the series!");
            disableButtons();
            restartSeriesButton.setDisable(false);
            restartButton.setDisable(true);
        } else if (playerOWins == 3) {
            winnerText.setText("O wins the series!");
            disableButtons();
            restartSeriesButton.setDisable(false);
            restartButton.setDisable(true);
        }
    }

    private void computerMove() {
        ArrayList<Button> availableButtons = new ArrayList<>();
        for (Button button : buttons) {
            if (!button.isDisabled() && button.getText().isEmpty()) {
                availableButtons.add(button);
            }
        }

        if (!availableButtons.isEmpty()) {
            Button randomButton = availableButtons.get(random.nextInt(availableButtons.size()));
            randomButton.setText(Player.O.toString());
            randomButton.setDisable(true);
            currentPlayer = Player.X;
            checkIfGameIsOver();
        }
    }
}

// Enum for Player
enum Player {
    X, O
}
