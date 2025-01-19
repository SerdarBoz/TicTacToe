package com.example.demo;

import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoard {
    private final List<Button> buttons;

    public GameBoard(Button[] buttons) {
        this.buttons = new ArrayList<>(Arrays.asList(buttons));
    }

    public void resetBoard() {
        buttons.stream().forEach(button -> {
            button.setDisable(false);
            button.setText("");
        });

    }

    public void disableAllButtons() {
        buttons.stream().forEach(button -> {
            button.setDisable(true);
        });
    }

    public boolean isFull() {
        return buttons.stream().noneMatch(button -> button.getText().isEmpty());

    }

    public String getLine(int index) {
        return switch (index) {
            case 0 -> buttons.get(0).getText() + buttons.get(1).getText() + buttons.get(2).getText();
            case 1 -> buttons.get(3).getText() + buttons.get(4).getText() + buttons.get(5).getText();
            case 2 -> buttons.get(6).getText() + buttons.get(7).getText() + buttons.get(8).getText();
            case 3 -> buttons.get(0).getText() + buttons.get(4).getText() + buttons.get(8).getText();
            case 4 -> buttons.get(2).getText() + buttons.get(4).getText() + buttons.get(6).getText();
            case 5 -> buttons.get(0).getText() + buttons.get(3).getText() + buttons.get(6).getText();
            case 6 -> buttons.get(1).getText() + buttons.get(4).getText() + buttons.get(7).getText();
            case 7 -> buttons.get(2).getText() + buttons.get(5).getText() + buttons.get(8).getText();
            default -> null;
        };
    }

    public List<Button> getAvailableButtons() {
        List<Button> availableButtons = new ArrayList<>();
        for (Button button : buttons) {
            if (!button.isDisabled() && button.getText().isEmpty()) {
                availableButtons.add(button);
            }
        }
        return availableButtons;
    }
}