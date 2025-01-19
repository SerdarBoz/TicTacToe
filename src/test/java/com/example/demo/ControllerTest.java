package com.example.demo;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerTest {
    private Controller controller;

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

        controller.button1 = new Button();
        controller.button2 = new Button();
        controller.button3 = new Button();
        controller.button4 = new Button();
        controller.button5 = new Button();
        controller.button6 = new Button();
        controller.button7 = new Button();
        controller.button8 = new Button();
        controller.button9 = new Button();

        controller.tryAgainButton = new Button();
        controller.restartSeriesButton = new Button();

        controller.winnerText = new Text();
        controller.scoreboardText = new Text();
        controller.errorText = new Text();

        controller.buttons = new ArrayList<>();
        controller.buttons.add(controller.button1);
        controller.buttons.add(controller.button2);
        controller.buttons.add(controller.button3);
        controller.buttons.add(controller.button4);
        controller.buttons.add(controller.button5);
        controller.buttons.add(controller.button6);
        controller.buttons.add(controller.button7);
        controller.buttons.add(controller.button8);
        controller.buttons.add(controller.button9);

        controller.initialize(null, null);
    }

    @Test
    void testCheckIfGameIsOver_XWins() {
        controller.button1.setText("X");
        controller.button2.setText("X");
        controller.button3.setText("X");

        controller.checkIfGameIsOver();
        assertEquals("X wint! Score: 1 - 0", controller.winnerText.getText());
    }

    @Test
    void testCheckIfGameIsOver_OWins() {
        controller.button1.setText("O");
        controller.button4.setText("O");
        controller.button7.setText("O");
        controller.checkIfGameIsOver();
        assertEquals("O wint! Score: 0 - 1", controller.winnerText.getText());
    }

    @Test
    void testCheckIfGameIsOver_Draw() {
        controller.button1.setText("X");
        controller.button2.setText("O");
        controller.button3.setText("X");
        controller.button4.setText("X");
        controller.button5.setText("X");
        controller.button6.setText("O");
        controller.button7.setText("O");
        controller.button8.setText("X");
        controller.button9.setText("O");

        controller.checkIfGameIsOver();
        assertEquals("Gelijkspel! Score: 0 - 0", controller.winnerText.getText());
    }
}