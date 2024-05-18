package com.registration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Registration extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image("file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/momentumpro-favicon-color.png"));
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("/com/example/momentumv1/registration-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("MomentumPro!");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/momentumv1/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}