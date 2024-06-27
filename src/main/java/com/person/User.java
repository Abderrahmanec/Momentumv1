package com.person;
import com.database.DatabaseManager;
import com.project.Project;
import com.project.ProjectRepository;
import com.project.Project;
import com.project.ProjektController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Persone {
    private int projectId;
    private double amountPaid;
//int id, String username, String password, boolean isAdmin
    public User(int id, String username, String password,int projectId, double amountPaid) {
        super(id, username, password ,false);
        this.projectId = projectId;
        this.amountPaid = amountPaid;
    }

    public User(String username, String password, Project currentProject) {
        //super(0, username, password);
        this.projectId = currentProject.getId();
        this.amountPaid = 0.0;
    }

    @Override
    public boolean authenticate() {
        return DatabaseManager.authenticateUser(username, password);
    }

    public static User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username=?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getInt("project_id"),
                        resultSet.getDouble("amount_paid")
                );
            }
        }
        return null;
    }

    public int getProjectId() {
        return projectId;
    }

    public double getTotalAmountToPay() {
        Project project = getProject();
        if (project != null) {
            return project.getBudget() / project.getSharedPersons();
        }
        return 0;
    }

    public double getRemainingAmount() {
        return getTotalAmountToPay() - amountPaid;
    }

    public void makePayment(double amount) throws SQLException {
        if (amount > getRemainingAmount()) {
            throw new IllegalArgumentException("Payment exceeds the remaining amount.");
        }
        amountPaid += amount;
        System.out.print(amountPaid);
            ProjectRepository.getAllProjects();

        updateAmountPaidInDatabase();
        ProjectRepository.getAllProjects();
    }

    private void updateAmountPaidInDatabase() {
        String query = "UPDATE users SET amount_paid = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, amountPaid);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Project getProject() {
        try {
            return ProjectRepository.getProjectById(projectId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "User{id=%d, username='%s', projectId=%d, amountPaid=%.2f}",
                id, username, projectId, amountPaid
        );
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

}
