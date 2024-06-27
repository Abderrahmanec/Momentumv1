package com.person;

import com.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class Admin extends Persone {

    @FXML public TextField descriptionField;
    @FXML public TextField projectNameField;
    @FXML public TextField startDateField;
    @FXML public TextField endDateField;
    @FXML public ListView teamMembersList;
    @FXML public TextField statusField;
    @FXML TextField u;
    @FXML
    PasswordField p;
    public Admin() {
        super();   // No-argument constructor
    }
    public Admin(int id, String username, String password,boolean isAdmin) {
        super(id, username, password ,isAdmin);
    }

    @FXML
    @Override
    public boolean authenticate() {
        username = u.getText();
        password = p.getText();
        boolean isAuthenticated = DatabaseManager.authenticateUser(username, password);

        if (isAuthenticated) {
            showAlert("Login Successful", "Welcome, " + username + "!");
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }

        return isAuthenticated;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getInt("project_id"),
                        resultSet.getDouble("amount_paid")
                ));
            }
        }
        return users;
    }

    @FXML
    public boolean createProject(String name, double budget, int duration, int sharedPersons) {
        String query = "INSERT INTO projects (name, budget, shared_persons) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, budget);
            preparedStatement.setInt(3, duration);
            preparedStatement.setInt(4, sharedPersons);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProject(int projectId, String name, double budget, int duration, int sharedPersons) {

        String query = "UPDATE projects SET name = ?, budget = ?, duration = ?, shared_persons = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, budget);
            preparedStatement.setInt(3, duration);
            preparedStatement.setInt(4, sharedPersons);
            preparedStatement.setInt(5, projectId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProject(int projectId) {
        String query = "DELETE FROM projects WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void handleSaveAction(ActionEvent actionEvent) {
        System.out.print("Handle Save Action");
    }

    public void handleCancelAction(ActionEvent actionEvent) {
        System.out.print(actionEvent.getEventType());
    }

    public void handleAddMemberAction(ActionEvent actionEvent) {
    }

    public void handleRemoveMemberAction(ActionEvent actionEvent) {
    }

    public void handleBackToDashboardAction(ActionEvent actionEvent) {
    }


}
