package com.example.momentumv1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {


    public BorderPane borderPane;
    public Menu File;
    public MenuItem Close;
    public Menu Help;

    @FXML
    public  void print(){
        System.out.print("Hello in this class ");
    }


    @FXML
    public void openNewWindow() throws IOException {
        // Launch a new stage for the new window
        Stage newWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(loader.load());
        newWindow.getIcons().add(new Image("file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/momentumpro-favicon-color.png"));

        newWindow.setScene(scene);
        newWindow.setTitle("Login Registration");
        newWindow.show();
    }

    @FXML
    public  void openNewWindowRe() throws IOException {
        // Launch a new stage for the new window

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/momentumv1/registration-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage newWindow = new Stage(); // Instantiate Stage object
        newWindow.getIcons().add(new Image("file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/momentumpro-favicon-color.png"));
        newWindow.setScene(scene);
        newWindow.setTitle("Registration");
        newWindow.show();

    }

@FXML public void task()throws IOException {
    // Launch a new stage for the new window
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/momentumv1/task-view.fxml"));
    Scene scene = new Scene(loader.load());
    Stage newWindow = new Stage(); // Instantiate Stage object
    newWindow.getIcons().add(new Image("file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/momentumpro-favicon-color.png"));
    newWindow.setScene(scene);
    newWindow.setTitle("Tasks");
    newWindow.show();

}


    @FXML public void projekt()throws IOException {
        // Launch a new stage for the new window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/momentumv1/projekt-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Apply CSS
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        Stage newWindow = new Stage();
        newWindow.getIcons().add(new Image("file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/momentumpro-favicon-color.png"));
        newWindow.setScene(scene);
        newWindow.setTitle("Projekt");
        newWindow.show();
    }

    @FXML public void admin()throws IOException {
        // Launch a new stage for the new window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/momentumv1/dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        Stage newWindow = new Stage(); // Instantiate Stage object
        newWindow.getIcons().add(new Image("file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/momentumpro-favicon-color.png"));
        newWindow.setScene(scene);
        newWindow.setTitle("Dashboard");
        newWindow.show();
    }

    @FXML public void user()throws IOException {
        // Launch a new stage for the new window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/momentumv1/user-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage newWindow = new Stage(); // Instantiate Stage object
        newWindow.getIcons().add(new Image("file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/momentumpro-favicon-color.png"));
        newWindow.setScene(scene);
        newWindow.setTitle("USER VIEW");
        newWindow.show();
    }




}
