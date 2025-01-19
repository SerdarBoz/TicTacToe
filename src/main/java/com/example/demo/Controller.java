package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

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
    private int totalGames = 0;

    GameBoard gameBoard;

    private final Random random = new Random();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Button[] buttonArray = {button1, button2, button3, button4, button5, button6, button7, button8, button9};
        gameBoard = new GameBoard(buttonArray);

        gameBoard.resetBoard();
        for (Button button : gameBoard.getAvailableButtons()) {
            setupButton(button);
        }
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
        gameBoard.resetBoard();
        winnerText.setText("Tic-Tac-Toe! Best of five!");
        currentPlayer = Player.X;
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
        if (currentPlayer == Player.X) {
            currentPlayer = Player.O;
        } else {
            currentPlayer = Player.X;
        }
    }

    public void checkIfGameIsOver() {
        for (int a = 0; a < 8; a++) {
            String line = gameBoard.getLine(a);
            if (line.equals("XXX")) {
                playerXWins++;
                totalGames++;
                winnerText.setText("X wint! Score: " + playerXWins + " - " + playerOWins);
                updateScoreboard();
                gameBoard.disableAllButtons();
                checkBestOfFive();
                return;
            }
            if (line.equals("OOO")) {
                playerOWins++;
                totalGames++;
                winnerText.setText("O wint! Score: " + playerXWins + " - " + playerOWins);
                updateScoreboard();
                gameBoard.disableAllButtons();
                checkBestOfFive();
                return;
            }
        }
        if (gameBoard.isFull()) {
            winnerText.setText("Gelijkspel! Score: " + playerXWins + " - " + playerOWins);
            gameBoard.disableAllButtons();
            updateScoreboard();
            checkBestOfFive();
        }
    }

    private void checkBestOfFive() {
        if (playerXWins == 3) {
            winnerText.setText("X wint het spel!");
            gameBoard.disableAllButtons();
            writeScoreToFile();
            restartSeriesButton.setDisable(false);
            tryAgainButton.setDisable(true);
        } else if (playerOWins == 3) {
            winnerText.setText("O wint het spel!");
            gameBoard.disableAllButtons();
            writeScoreToFile();
            restartSeriesButton.setDisable(false);
            tryAgainButton.setDisable(true);
        }
    }

    private void computerMove() {
        List<Button> availableButtons = gameBoard.getAvailableButtons();
        if (!availableButtons.isEmpty()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
            pause.setOnFinished(event -> {
                Button randomButton = availableButtons.get(random.nextInt(availableButtons.size()));
                setPlayerSymbol(randomButton);
                checkIfGameIsOver();
            });
            pause.play();
        }
    }

    private void writeScoreToFile() {
        int totalGamesInSeries = playerXWins + playerOWins;
        double playerOWinPercentage;
        double playerXWinPercentage;

        if (totalGamesInSeries == 0) {
            playerXWinPercentage = 0;
        } else {
            playerXWinPercentage = ((double) playerXWins / totalGamesInSeries) * 100;
        }
        if (totalGamesInSeries == 0) {
            playerOWinPercentage = 0;
        } else {
            playerOWinPercentage = ((double) playerOWins / totalGamesInSeries) * 100;
        }

        try (var writer = new BufferedWriter(new FileWriter("score.txt", true))) {
            writer.write("Game " + gameCount + "\n");
            writer.write("X Wins: " + playerXWins + " (" + String.format("%.1f", playerXWinPercentage) + "%)\n");
            writer.write("O Wins: " + playerOWins + " (" + String.format("%.1f", playerOWinPercentage) + "%)\n");
            writer.write("Total Games: " + totalGames + "\n\n");
        } catch (IOException e) {
            System.err.println("Fout bij het wegschrijven naar score.txt: " + e.getMessage());
            e.printStackTrace();
        }
    }
}