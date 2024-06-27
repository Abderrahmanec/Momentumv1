package com.notifcationPackage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.Objects;

public class NotificationHelper {

    public static void showSuccessNotification(String title, String message) {
        showAlert(title, message, "/com/example/momentumv1/icons/success.svg", "alert-success");
    }

    public static void showFailedNotification(String title, String message) {
        showAlert(title, message, "/com/example/momentumv1/icons/Erroricon.png", "alert-danger");
    }

    private static void showAlert(String title, String message, String iconPath, String bootstrapClass) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Use a TextArea to display long messages
        TextArea textArea = new TextArea(message);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(textArea);
        dialogPane.setPrefSize(400, 300); // Adjust the size as needed

        // Load and set the icon if provided
        if (iconPath != null && !iconPath.isEmpty()) {
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(NotificationHelper.class.getResourceAsStream(iconPath))));
            imageView.setFitWidth(48); // Adjust size if necessary
            imageView.setFitHeight(48);
            alert.setGraphic(imageView);
        }

        // Apply BootstrapFX styles
        dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        dialogPane.getStyleClass().add(bootstrapClass);

        // Add CSS class to button
        dialogPane.lookupButton(ButtonType.OK).getStyleClass().add("custom-button");

        // Show the alert dialog
        alert.showAndWait();
    }

    public static void notification(String notification, String title, String message) {
        try {
            String notificationToUpperCase = notification.toUpperCase();
            AlertType alertType = AlertType.valueOf(notificationToUpperCase);

            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);

            // Use a TextArea to display long messages
            TextArea textArea = new TextArea(message);
            textArea.setWrapText(true);
            textArea.setEditable(false);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setContent(textArea);
            dialogPane.setPrefSize(400, 300); // Adjust the size as needed

            // Apply BootstrapFX styles
            dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            dialogPane.getStyleClass().add(getBootstrapClass(notificationToUpperCase));

            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid alert type: " + notification);
        }
    }

    private static String getBootstrapClass(String notificationType) {
        return switch (notificationType) {
            case "INFORMATION" -> "alert-info";
            case "WARNING" -> "alert-warning";
            default -> "alert-danger";
        };
    }
}
