package com.notifcationPackage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NotificationHelper {
    public static void showSuccessNotification(String title, String message) {
        showAlert(title, message, AlertType.INFORMATION, "/com/example/momentumv1/icons/success.svg");
    }

    public static void showFailedNotification(String title, String message) {
        showAlert(title, message, AlertType.INFORMATION, "/com/example/momentumv1/icons/Erroricon.png");
    }


    private static void showAlert(String title, String message, AlertType alertType, String iconPath) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (iconPath != null && !iconPath.isEmpty()) {
            ImageView imageView = new ImageView(new Image(NotificationHelper.class.getResourceAsStream(iconPath)));
            imageView.setFitWidth(48); // Adjust size if necessary
            imageView.setFitHeight(48);
            alert.setGraphic(imageView);
        }
        // Show the alert dialog
        alert.showAndWait();
    }
}
