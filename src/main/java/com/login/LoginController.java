package com.login;

import com.UIUtilities;
import com.database.DatabaseManager;

import com.notifcationPackage.NotificationHelper;
import com.registration.RegistrationController;
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



public class LoginController {

    // Public UI elements (accessed directly for convenience)
    @FXML private Label welcome;
    @FXML private Label logStatus;
    @FXML private Label showPassword;
    @FXML private Label passwodLabel;
    @FXML private Label usernameLabel;
    @FXML private Label css;

    // FXML-annotated private UI elements (injected by FXML)
    @FXML private Button log;
    @FXML private TextField usernameField;
    @FXML private CheckBox showPasswordCheckbox;
    @FXML private PasswordField passwordField;
    @FXML private ProgressBar progressBar;
    @FXML private Pane paneBackgroundColor;
    @FXML private StackPane stackPaneBackgroundColor;
    @FXML ToggleButton togle;

    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isAuthenticated = DatabaseManager.authenticateUser(username, password);
        if (isAuthenticated) {
            //logStatus.setStyle("-fx-text-fill: green;");
            // logStatus.setText("Login Successful!");
            NotificationHelper.showSuccessNotification("Login Successfully","Login Successful!");
            hidden();

        } else {
            //  displayNotification();
            NotificationHelper.showFailedNotification("Login Failed","Incorrect username or password.");
            // logStatus.setStyle("-fx-text-fill: red;");
            //logStatus.setText("Login Failed: Incorrect username or password.");
        }
    }

    @FXML
    public void showPassword() {
        if (showPasswordCheckbox.isSelected()) {
            showPassword.setText("Hide Password");
           // setBorder(showPassword);
            showPassword.setText(passwordField.getText());
            passwordListener();
        } else {
            showPassword.setText("Show Password");
            showPassword.setText("");


        }
    }


    @FXML
    public void registerFenster() {
        //To close Login Window
        try{
            closePrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/src/main/resources/com/example/momentumv1/registration-view.fxml"));
            Parent root = fxmlLoader.load();
            // Call toggleDarkMode in LoginController before showing the stage
            toggleDarkMode();
            Stage stage = new Stage();
            stage.setTitle("Registration");
            stage.setScene(new Scene(root));

            // Retrieve the controller instance after the view is loaded
            RegistrationController registrationController = fxmlLoader.getController();
            // Call the toggleDarkMode method in the RegistrationController
            registrationController.toggleDarkMode();
            stage.show();
            animation();
        } catch (IOException e) {
            logStatus.setText("Error loading registration view: " + e.getMessage());
        }
    }

    @FXML
    public void closePrimaryStage() {
        Stage stage = (Stage) welcome.getScene().getWindow();
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
        boolean useWhiteText = isDarkMode; // Invert logic for label color

        if (!isDarkMode) {
            // dark backgound , white labels  styleClass=".label-white"
            UIUtilities.labelsColorToWhite(passwodLabel,usernameLabel,showPassword ,true);
            UIUtilities.setBackground(paneBackgroundColor, stackPaneBackgroundColor,true);//Black Background
        } else {
            UIUtilities.labelsColorToBlack(passwodLabel,usernameLabel,showPassword ,false);
            UIUtilities.setBackground(paneBackgroundColor, stackPaneBackgroundColor,false);
        }
        if (showPasswordCheckbox.isSelected()){
           // setBorder(showPassword);
        }
    }


    @FXML
    private void hidden(){
        showPasswordCheckbox.setVisible(false);
        showPassword.setVisible(false);
        usernameField.setVisible(false);
        passwordField.setVisible(false);
        String n=usernameField.getText();
        log.setVisible(false);
        //logStatus.setText("Hello to MomentumPro "+n);
    }

    @FXML
    private void wrongPasswordOrUsername() {
        String message="Username or password wrong!";
        String title="Login Failed!";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Show the alert dialog
        alert.showAndWait();
    }

    @FXML
    private void sucssesfull() {
        String message="Sucessful !";
        String title="Loged!";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Show the alert dialog
        alert.showAndWait();
    }




}