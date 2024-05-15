package com.example.momentumv1;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class LoginKlasse extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 770, 700);
        stage.setTitle("MomentumPro!");
        // Load CSS file relative to the FXML file
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}