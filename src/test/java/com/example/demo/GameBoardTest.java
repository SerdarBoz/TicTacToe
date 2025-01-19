package com.example.demo;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

class GameBoardTest {

    private GameBoard gameBoard;
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
        button1 = new Button();
        button2 = new Button();
        button3 = new Button();
        button4 = new Button();
        button5 = new Button();
        button6 = new Button();
        button7 = new Button();
        button8 = new Button();
        button9 = new Button();
        gameBoard = new GameBoard(button1, button2, button3, button4, button5, button6, button7, button8, button9);
    }

    @Test
    void testResetBoard() {
        button1.setText("X");
        button2.setText("O");
        button3.setText("X");

        gameBoard.resetBoard();

        assertEquals("", button1.getText());
        assertEquals("", button2.getText());
        assertEquals("", button3.getText());
    }

    @Test
    void testIsFull() {
        button1.setText("X");
        button2.setText("O");
        button3.setText("X");
        button4.setText("O");
        button5.setText("X");
        button6.setText("O");
        button7.setText("X");
        button8.setText("O");
        button9.setText("X");

        assertTrue(gameBoard.isFull());
    }

    @Test
    void testGetAvailableButtons() {
        button1.setText("X");
        button2.setText("O");
        button3.setText("");
        button4.setText("X");
        button5.setText("");
        button6.setText("O");
        button7.setText("X");
        button8.setText("");
        button9.setText("O");

        List<Button> availableButtons = gameBoard.getAvailableButtons();

        List<Button> expected = Arrays.asList(button3, button5, button8);
        assertTrue(availableButtons.containsAll(expected));
        assertEquals(3, availableButtons.size());
    }
}