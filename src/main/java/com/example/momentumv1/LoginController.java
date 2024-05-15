package com.example.momentumv1;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;



import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;



import static com.example.momentumv1.UIUtilities.setBorder;

public class LoginController {

    @FXML private   Button log;

    public  Label css;
    public Label welcome;
    @FXML
    private  Label logStatus;
    @FXML
    private TextField usernameField;

    @FXML
    private CheckBox showPasswordCheckbox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label showPassword;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane paneBackgroundColor;

    @FXML
    private StackPane stackPaneBackgroundColor;

    @FXML
    ToggleButton togle;


    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isAuthenticated = DatabaseManager.authenticateUser(username, password);
        if (isAuthenticated) {
            logStatus.setStyle("-fx-text-fill: green;");
            logStatus.setText("Login Successful!");
            hidden();

        } else {
            logStatus.setStyle("-fx-text-fill: red;");
            logStatus.setText("Login Failed: Incorrect username or password.");
        }
    }

    @FXML
    public void showPassword() {
        if (showPasswordCheckbox.isSelected()) {
            showPassword.setText("Hide Password");
            setBorder(showPassword);
            showPassword.setText(passwordField.getText());
            passwordListener();
        } else {
            showPassword.setText("Show Password");
            showPassword.setText("");
            showPassword.setStyle(null);

        }
    }


    @FXML
    public void registerFenster() {
        //To close Login Window
        try{
        closePrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration-view.fxml"));
        Parent root = fxmlLoader.load();
        // Call toggleDarkMode in LoginController before showing the stage
        toggleDarkMode();
        Stage stage = new Stage();
        stage.setTitle("Registration");
        stage.setScene(new Scene(root));
        stage.show();

        // Retrieve the controller instance after the view is loaded
        RegistrationController registrationController = fxmlLoader.getController();

        // Call the toggleDarkMode method in the RegistrationController
        registrationController.toggleDarkMode();
        animation();
        } catch (IOException e) {
            logStatus.setText("Error loading registration view: " + e.getMessage());
        }
    }

    @FXML
    public void closePrimaryStage() {
        Stage stage = (Stage) logStatus.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void animation() {
        if (progressBar != null) {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), new KeyValue(progressBar.opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.opacityProperty(), 3)),
                    new KeyFrame(Duration.seconds(4), new KeyValue(progressBar.opacityProperty(), 6))
            );
            timeline.play();
            timeline.setOnFinished(event -> closePrimaryStage());
        } else {
            logStatus.setText("Progress Bar not initialized.");
        }
    }

    @FXML
    Label name;
    @FXML
    Label pass;

    @FXML
    public void passwordListener() {
        // Add listener to the showPasswordCheckbox
        this.passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.showPasswordCheckbox.isSelected()) {
                this.showPassword.setText(newValue);

            } else {
                this.showPassword.setText("");
            }
        });
    }

    @FXML
    public void toggleDarkMode() {
        boolean isDarkMode=togle.isSelected();
        if (isDarkMode) {
            // For light mode
            UIUtilities.changeBackgroundToWhite(paneBackgroundColor, stackPaneBackgroundColor);
            UIUtilities.labelsColorToBlack(showPassword,name,pass);
        } else {
            UIUtilities.changeBackgroundToDarkMode(paneBackgroundColor, stackPaneBackgroundColor);
            UIUtilities.labelsColorToWhite(showPassword,name,pass);
        }
        if (showPasswordCheckbox.isSelected()){
            setBorder(showPassword);
        }
    }


    @FXML
    private void hidden(){
        showPasswordCheckbox.setVisible(false);
        showPassword.setVisible(false);
        usernameField.setVisible(false);
        passwordField.setVisible(false);
        name.setVisible(false);
        pass.setVisible(false);
        String n=usernameField.getText();
        log.setVisible(false);
        logStatus.setText("Hello to MomentumPro "+n);
    }

}