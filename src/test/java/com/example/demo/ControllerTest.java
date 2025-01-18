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
        // Start JavaFX-toolkit
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

        // Mock de knoppen
        controller.button1 = new Button();
        controller.button2 = new Button();
        controller.button3 = new Button();
        controller.button4 = new Button();
        controller.button5 = new Button();
        controller.button6 = new Button();
        controller.button7 = new Button();
        controller.button8 = new Button();
        controller.button9 = new Button();

        // Mock de aanvullende knoppen
        controller.tryAgainButton = new Button();
        controller.restartSeriesButton = new Button();

        // Mock de Text-velden
        controller.winnerText = new Text();
        controller.scoreboardText = new Text();
        controller.errorText = new Text();

        // Zet de knoppen in de lijst
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

        // Initialiseer de controller
        controller.initialize(null, null);
    }

    @Test
    void testCheckIfGameIsOver_XWins() {
        // Simuleer dat speler X drie knoppen in een rij heeft
        controller.button1.setText("X");
        controller.button2.setText("X");
        controller.button3.setText("X");

        // Controleer of het spel correct eindigt
        controller.checkIfGameIsOver();
        assertEquals("X won! Score: 1 - 0", controller.winnerText.getText());
    }
    @Test
    void testCheckIfGameIsOver_OWins() {
        // Simuleer dat speler O drie knoppen in een kolom heeft
        controller.button1.setText("O");
        controller.button4.setText("O");
        controller.button7.setText("O");

        controller.checkIfGameIsOver();

        // Controleer of de winnaar correct wordt gedetecteerd
        assertEquals("O won! Score: 0 - 1", controller.winnerText.getText());
    }

    @Test
    void testCheckIfGameIsOver_Draw() {
        // Vul alle knoppen zonder winnaar
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

        // Controleer of het gelijkspel correct wordt gedetecteerd
        assertEquals("It's a draw! Score: 0 - 0", controller.winnerText.getText());
    }
}
