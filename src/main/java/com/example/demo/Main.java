package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Tic-Tac-Toe: Best of Five");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Fout bij het laden van sample.fxml: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}