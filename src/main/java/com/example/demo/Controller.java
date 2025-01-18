package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Controller implements Initializable {
    @FXML
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML
    public Button tryAgainButton, restartSeriesButton;
    @FXML
    public Text winnerText, scoreboardText, errorText;

    private Player currentPlayer = Player.X;
    private int playerXWins = 0;
    private int playerOWins = 0;
    private int gameCount = 1;

    ArrayList<Button> buttons;
    Random random = new Random();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));
        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });
        updateScoreboard();
        restartSeriesButton.setDisable(true);
    }

    private void updateScoreboard() {
        scoreboardText.setText("Scoreboard: X - " + playerXWins + ", O - " + playerOWins);
    }

    @FXML
    void restartSeries(ActionEvent event) {
        playerXWins = 0;
        playerOWins = 0;
        gameCount++;
        restartGame(null);
        winnerText.setText("Tic-Tac-Toe! Best of five!");
        restartSeriesButton.setDisable(true);
        tryAgainButton.setDisable(false);
        updateScoreboard();
    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::tryAgain);
        winnerText.setText("Tic-Tac-Toe! Best of five!");
        currentPlayer = Player.X;
    }

    public void tryAgain(Button button) {
        button.setDisable(false);
        button.setText("");
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            try {
                setPlayerSymbol(button);
                checkIfGameIsOver();
                if (currentPlayer == Player.O) {
                    computerMove();
                }
            } catch (CellOccupiedException e) {
                errorText.setText("Deze is al bezet! Probeer opnieuw.");
                new Timeline(
                        new KeyFrame(
                                Duration.seconds(1.5),
                                event -> errorText.setText("")
                        )
                ).play();
            }
        });
    }

    public void setPlayerSymbol(Button button) throws CellOccupiedException {
        if (!button.getText().isEmpty()) {
            throw new CellOccupiedException("Deze is al bezet!");
        }
        button.setText(currentPlayer.toString());
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;
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
            if (line.equals("XXX")) {
                playerXWins++;
                winnerText.setText("X wint! Score: " + playerXWins + " - " + playerOWins);
                updateScoreboard();
                disableButtons();
                checkBestOfFive();
                return;
            }
            if (line.equals("OOO")) {
                playerOWins++;
                winnerText.setText("O wint! Score: " + playerXWins + " - " + playerOWins);
                updateScoreboard();
                disableButtons();
                checkBestOfFive();
                return;
            }
        }
        if (buttons.stream().allMatch(button -> !button.getText().isEmpty())) {
            winnerText.setText("Gelijkspel! Score: " + playerXWins + " - " + playerOWins);
            disableButtons();
            updateScoreboard();
            checkBestOfFive();

        }
    }

    private void disableButtons() {
        buttons.forEach(button -> button.setDisable(true));
    }

    private void checkBestOfFive() {
        if (playerXWins == 3) {
            winnerText.setText("X wint het spel!");
            disableButtons();
            writeScoreToFile();
            restartSeriesButton.setDisable(false);
            tryAgainButton.setDisable(true);
        } else if (playerOWins == 3) {
            winnerText.setText("O wint het spel!");
            disableButtons();
            writeScoreToFile();
            restartSeriesButton.setDisable(false);
            tryAgainButton.setDisable(true);
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
            PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
            pause.setOnFinished(event -> {
                Button randomButton = availableButtons.get(random.nextInt(availableButtons.size()));
                try {
                    setPlayerSymbol(randomButton);
                    checkIfGameIsOver();
                } catch (CellOccupiedException e) {
                    e.printStackTrace();
                }
            });
            pause.play();
        }
    }


    private void writeScoreToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("score.txt", true))) {
            writer.write("Game " + gameCount + "\n");
            writer.write("X: " + playerXWins + "\n");
            writer.write("O: " + playerOWins + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class CellOccupiedException extends Exception {
        public CellOccupiedException(String message) {
            super(message);
        }
    }
}

enum Player {
    X, O
}