package com.registration;

import com.UIUtilities;
import com.config.Appconfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class RegistrationController {

    @FXML private Label welcomeUser;
    @FXML private Label userNameLabel;
    @FXML private Label status;
    @FXML private Label passwordLabel1;
    @FXML private Label passwordLabel2;
    @FXML private Label showPassword2;
    @FXML private Label showPassword1;
    @FXML private PasswordField passwordField1;
    @FXML private PasswordField passwordField2;
    @FXML private CheckBox showPasswordCheckBox;
    @FXML private TextField usernameField;

    // Database URL, username, and password
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    static {
        Properties properties = new Properties();
        try (FileInputStream db_configLoad = new FileInputStream("src/main/java/com/config/db_config.properties")) {
            properties.load(db_configLoad);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
        DB_URL = Appconfig.getDbUrl();
        DB_USER =Appconfig.getDbUsername();
        DB_PASSWORD = Appconfig.getDbPassword();
    }

    @FXML private void passwordConfirmation() {
        toggleDarkMode();
        String username = usernameField.getText();
        String password1 = passwordField1.getText();
        String password2 = passwordField2.getText();
        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() || !password1.equals(password2))
        {
            status.setStyle("-fx-text-fill: red;");
            status.setText("Please fill in all fields and ensure passwords match.");
            return;
        }

        // Generate a random salt
        String salt = generateSalt();
        // Hash the password with the generated salt
        String hashedPassword = hashPassword(password1, salt);
        registerUser(username, hashedPassword, salt);
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // Consider adjusting size based on your needs
        random.nextBytes(saltBytes);
        return new String(Base64.getEncoder().encode(saltBytes));
    }

    private String hashPassword(String password, String salt) {
        // (consider using a dedicated library for secure hashing)
        String combinedString = password + salt;
        return DigestUtils.sha256Hex(combinedString);
    }

    @FXML
    private void registerUser(String username, String hashedPassword, String salt) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Update SQL statement to include salt
            String sql = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, salt);
                // Execute the SQL statement
                preparedStatement.executeUpdate();
                hidden();
                status.setStyle("-fx-text-fill: green;");
                status.setText("User registered successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            status.setText("Error registering user: " + e.getMessage());
        }
    }

    // Remaining code for UI functionalities (unchanged)
    @FXML  private Pane paneBackgroudColor;
    @FXML  private StackPane stackPaneBackgroundColor;
    @FXML  private ToggleButton mode;

    @FXML public void toggleDarkMode() {
        boolean isLight=mode.isSelected();
        boolean useWhiteText = isLight; // Invert logic for label color
        if (isLight) {
            // For dark mode
            String darkColor = "#171a1e";
            //   UIUtilities.labelsColorToWhite(passwordLabel1,passwordLabel2,userNameLabel);
            UIUtilities.labelsColorToWhite(passwordLabel1,passwordLabel2,userNameLabel ,true);
            UIUtilities.setBackground(paneBackgroudColor, stackPaneBackgroundColor,true);//Black Background
            //stackPaneBackgroundColor.setBackground(new Background(new BackgroundFill(Color.web(darkColor), null, null)));
            //paneBackgroudColor.setBackground(new Background(new BackgroundFill(Color.web(darkColor), null, null)));

        } else {
            // For light mode
            UIUtilities.labelsColorToBlack(passwordLabel1,passwordLabel2,userNameLabel ,false);
            UIUtilities.setBackground(paneBackgroudColor, stackPaneBackgroundColor,false);

        }
    }

    @FXML
    public void showPassword() {
        passwordListener();
        if (showPasswordCheckBox.isSelected()) {
            showPassword1.setText(passwordField1.getText());
            showPassword2.setText(passwordField2.getText());

        } else {
            showPassword1.setText("");
            showPassword2.setText("");
            showPassword1.setStyle(null);
        }
    }

    @FXML
    public void passwordListener() {
        // ... ( UI logic)
        this.passwordField1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (showPasswordCheckBox.isSelected()) {
                this.showPassword1.setText(newValue);
                // Update the value of passwordField2 if it's not already equal to newValue
                if (!this.passwordField1.getText().equals(newValue)) {
                    this.passwordField1.setText(newValue);
                }
            } else {
                this.showPassword1.setText("");
            }
        });

        // Add listener to the textProperty of passwordField2
        this.passwordField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (showPasswordCheckBox.isSelected()) {
                this.showPassword2.setText(newValue);
                // Update the value of passwordField1 if it's not already equal to newValue
                if (!this.passwordField2.getText().equals(newValue)) {
                    this.passwordField2.setText(newValue);
                }
            } else {
                this.showPassword2.setText("");
            }
        });
    }


    public void hidden(){
        usernameField.setVisible(false);
        passwordField1.setVisible(false);
        passwordField2.setVisible(false);
    }

}
