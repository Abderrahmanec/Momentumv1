package com.personeControllers;

import com.notifcationPackage.NotificationHelper;
import com.project.Project;
import com.project.ProjectRepository;
import com.project.ProjektController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserController {

    public TextField pid;
    public TextField uid;
    public TextField betragTextField;
    @FXML
    private TextField usernameTextField;

    @FXML
    private Button makePaymentButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/project_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private ProjektController projektController;

    @FXML
    public void initialize() {
        // Initialization if needed
    }

    public void setProjektController(ProjektController projektController) {
        this.projektController = projektController;
    }

    @FXML
    public void makePayment() throws SQLException {
        ensureUserExists();

        double amount = 0;
        try {
            amount = Double.parseDouble(betragTextField.getText());
        } catch (NumberFormatException e) {
            NotificationHelper.showFailedNotification("Invalid Input", "Please enter a valid payment amount.");
            return;
        }

        int userId = getCurrentUserId();
        int projectId = getCurrentProjectId();

        LocalDateTime paymentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String paymentDateFormatted = paymentDateTime.format(formatter);

        if (processPayment(userId, projectId, amount, paymentDateFormatted)) {
            // Update the project after making the payment
            updateProjectAmountPaid(projectId);

            NotificationHelper.showSuccessNotification("Payment Successful", "Payment has been processed successfully.");
        } else {
            NotificationHelper.showFailedNotification("Payment Failed", "Payment could not be processed.");
        }
    }

    private int getCurrentUserId() {
        return Integer.parseInt(uid.getText());
    }

    private int getCurrentProjectId() {
        return Integer.parseInt(pid.getText());
    }

    private boolean processPayment(int userId, int projectId, double amount, String paymentDateFormatted) {
        String paymentQuery = "INSERT INTO project_payments (project_id, payment_amount, payment_date) VALUES (?, ?, ?)";
        String updateProjectQuery = "UPDATE projects SET amount_paid = amount_paid + ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement paymentStatement = connection.prepareStatement(paymentQuery);
             PreparedStatement updateProjectStatement = connection.prepareStatement(updateProjectQuery)) {

            paymentStatement.setInt(1, projectId);
            paymentStatement.setDouble(2, amount);
            paymentStatement.setString(3, paymentDateFormatted);
            int paymentRows = paymentStatement.executeUpdate();

            updateProjectStatement.setDouble(1, amount);
            updateProjectStatement.setInt(2, projectId);
            int updateRows = updateProjectStatement.executeUpdate();

            return paymentRows > 0 && updateRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateProjectAmountPaid(int projectId) throws SQLException {
        // Fetch the updated project details from the database
        Project updatedProject = ProjectRepository.getProjectById(projectId);
        if (updatedProject != null) {
       //   ProjectRepository.getAllProjects();
            System.out.println("Updated Project Amount Paid: " + updatedProject.getTotalPaid());
        }
    }

    private void ensureUserExists() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM users_admins");
            if (rs.next() && rs.getInt(1) == 0) {
                String insertUserQuery = "INSERT INTO users_admins (username, password, is_admin) VALUES ('testuser', 'password123', 0)";
                statement.executeUpdate(insertUserQuery);
                System.out.println("Inserted default user 'testuser'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
