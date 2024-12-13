package com.example.pacmangame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PacManGameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PacManGameApplication.class.getResource("pacman-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 700);
        stage.setTitle("Pac-Man Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
