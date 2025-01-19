package com.example.demo;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    private Controller controller;
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9;

    @BeforeAll
    static void initToolkit() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        controller = new Controller();

        button1 = new Button();
        button2 = new Button();
        button3 = new Button();
        button4 = new Button();
        button5 = new Button();
        button6 = new Button();
        button7 = new Button();
        button8 = new Button();
        button9 = new Button();

        controller.button1 = button1;
        controller.button2 = button2;
        controller.button3 = button3;
        controller.button4 = button4;
        controller.button5 = button5;
        controller.button6 = button6;
        controller.button7 = button7;
        controller.button8 = button8;
        controller.button9 = button9;

        controller.tryAgainButton = new Button();
        controller.restartSeriesButton = new Button();

        controller.winnerText = new Text();
        controller.scoreboardText = new Text();
        controller.errorText = new Text();

        controller.gameBoard = new GameBoard(button1, button2, button3, button4, button5, button6, button7, button8, button9);

        controller.initialize(null, null);
    }

    @Test
    void testCheckIfGameIsOver_XWins() {
        button1.setText("X");
        button2.setText("X");
        button3.setText("X");

        controller.checkIfGameIsOver();

        assertEquals("X wint! Score: 1 - 0", controller.winnerText.getText());
    }

    @Test
    void testCheckIfGameIsOver_OWins() {
        button1.setText("O");
        button4.setText("O");
        button7.setText("O");

        controller.checkIfGameIsOver();

        assertEquals("O wint! Score: 0 - 1", controller.winnerText.getText());
    }

    @Test
    void testCheckIfGameIsOver_Draw() {
        button1.setText("X");
        button2.setText("O");
        button3.setText("X");
        button4.setText("X");
        button5.setText("X");
        button6.setText("O");
        button7.setText("O");
        button8.setText("X");
        button9.setText("O");

        controller.checkIfGameIsOver();

        assertEquals("Gelijkspel! Score: 0 - 0", controller.winnerText.getText());
    }
}