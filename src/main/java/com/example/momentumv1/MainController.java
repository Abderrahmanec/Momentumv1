package com.example.momentumv1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.registration.RegistrationController;

import java.io.IOException;

public class MainController {


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
        newWindow.setScene(scene);
        newWindow.setTitle("New Window");
        newWindow.show();
    }

    @FXML
    public  void openNewWindowRe() throws IOException {
        // Launch a new stage for the new window

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/momentumv1/registration-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage newWindow = new Stage(); // Instantiate Stage object
        newWindow.setScene(scene);
        newWindow.setTitle("New Window");
        newWindow.show();

    }


}
