package com.project;

import com.database.DatabaseManager;
import com.notifcationPackage.NotificationHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Read (Select) Methods
    public static Project getProjectById(int projectId) throws SQLException {
        String query = "SELECT *, status FROM projects WHERE id=?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Project(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("budget"),
                        resultSet.getInt("shared_persons"),
                        resultSet.getDate("startdate").toLocalDate(),
                        resultSet.getDate("enddate").toLocalDate()

                );
            }
        }
        return null;
    }
    //Hol alle Projekte von DatenBank

    public static List<Project> getAllProjects() throws SQLException {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                try {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double budget = resultSet.getDouble("budget");
                    int duration = resultSet.getInt("duration");
                    int sharedPersons = resultSet.getInt("shared_persons");
                    String statusString = resultSet.getString("status");
                    LocalDate startDate = resultSet.getDate("startdate") != null ? resultSet.getDate("startdate").toLocalDate() : null;
                    LocalDate endDate = resultSet.getDate("enddate") != null ? resultSet.getDate("enddate").toLocalDate() : null;
                    double totalbezahlt=resultSet.getDouble("amount_paid");
                    // Ensure required fields are not null
                    if (name == null || startDate == null || endDate == null) {
                        throw new IllegalArgumentException("Project with ID " + id + " has null required fields.");
                    }

                    // Convert status string to ProjectStatus enum
                    Project.ProjectStatus status = statusString != null ? Project.ProjectStatus.valueOf(statusString.toUpperCase()) : Project.ProjectStatus.WARTEND;

                    // Use the constructor with status
                    projects.add(new Project(id, name, budget, duration, startDate, endDate, status));
                } catch (Exception e) {
                    // Log the error and continue with the next project
                    System.err.println("Error processing project: " + e.getMessage());
                }
            }
        }
        return projects;
    }


    public static List<Project> getAllProject() throws SQLException {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT id, name, budget, duration, shared_persons, status, startdate, enddate FROM projects";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                try {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double budget = resultSet.getDouble("budget");
                    int duration = resultSet.getInt("duration");
                    int sharedPersons = resultSet.getInt("shared_persons");
                    String statusString = resultSet.getString("status");
                    LocalDate startDate = resultSet.getDate("startdate") != null ? resultSet.getDate("startdate").toLocalDate() : null;
                    LocalDate endDate = resultSet.getDate("enddate") != null ? resultSet.getDate("enddate").toLocalDate() : null;

                    // Debugging output to check statusString
                    System.out.println("Status String: " + statusString);

                    // Handle null statusString
                    Project.ProjectStatus status;
                    if (statusString != null) {
                        status = Project.ProjectStatus.valueOf(statusString.toUpperCase());
                    } else {
                        // Set default status if statusString is null
                        status = Project.ProjectStatus.WARTEND;
                    }

                    projects.add(new Project(id, name, budget, duration, startDate, endDate, status));
                } catch (Exception e) {
                    // Log the error and continue with the next project
                    System.err.println("Error processing project: " + e.getMessage());
                }
            }
        }
        return projects;
    }



    //Projekt auf DB hinfügem
    public static void addProjectToDatabase(String name, double budget, int sharedPersons,LocalDate startDate , LocalDate endDate) throws SQLException {
        String query = "INSERT INTO projects (name, budget, duration, shared_persons, startdate, enddate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, budget);
            preparedStatement.setInt(3, (int) java.time.temporal.ChronoUnit.MONTHS.between(startDate, endDate));
            preparedStatement.setInt(4, sharedPersons);
            preparedStatement.setDate(5, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(6, java.sql.Date.valueOf(endDate));
            preparedStatement.executeUpdate();
        }
    }

    // Update project
    public static void updateProjectInDatabase(Project project) throws SQLException {
        String query = "UPDATE projects SET name=?, budget=?, duration=?, shared_persons=?, startdate=?, enddate=? WHERE id=?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, project.getName());
            preparedStatement.setDouble(2, project.getBudget());
            preparedStatement.setLong(3, project.getDuration());
            preparedStatement.setInt(4, project.getSharedPersons());//index 4

            // Check if startDate is not null before converting
            if (project.getStartDate() != null) {
                preparedStatement.setDate(5, java.sql.Date.valueOf(project.getStartDate()));
            } else {
                preparedStatement.setNull(5, Types.DATE);
            }

            // Check if endDate is not null before converting
            if (project.getEndDate() != null) {
                preparedStatement.setDate(6, java.sql.Date.valueOf(project.getEndDate()));
            } else {
                preparedStatement.setNull(6, Types.DATE);
            }

            preparedStatement.setInt(7, project.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Delete project
    public static void deleteProjectFromDatabase(int id) throws SQLException {
        String query = "DELETE FROM projects WHERE id=?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.print("Deleted " + id);

    }
}

public static void setStatusOfProject(int projectId, String status) {
    String query = "UPDATE projects SET status = ? WHERE id = ?";
    try (Connection connection = DatabaseManager.getConnectToanyDb("project_management", "root", "");
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, status);
        preparedStatement.setInt(2, projectId);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Status updated successfully for project ID: " + projectId);
        } else {
            System.out.println("No project found with ID: " + projectId);
        }
    } catch (SQLException e) {
        System.err.println("Error updating status for project ID " + projectId + ": " + e.getMessage());
        e.printStackTrace();
    }
}
    //
    public static void projektVonDatenbankLoeschen(int projectId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if there are any payments associated with the project
            if (arePaymentsExist(connection, projectId)) {
                // Delete payments associated with the project
                deleteProjectPayments(connection, projectId);
            }
            // Now delete the project
            deleteProject(projectId);
            System.out.println("Deleted project with ID: " + projectId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to handle it in the caller method
        }
    }

    private static boolean arePaymentsExist(Connection connection, int projectId) throws SQLException {
        String query = "SELECT COUNT(*) FROM project_payments WHERE project_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

    private static void deleteProjectPayments(Connection connection, int projectId) throws SQLException {
        String query = "DELETE FROM project_payments WHERE project_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectId);
            preparedStatement.executeUpdate();
        }
    }

    private static void deleteProject(int projectId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Disable foreign key checks
            connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0").execute();

            // Delete associated payments
            String deletePaymentsQuery = "DELETE FROM payments WHERE project_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deletePaymentsQuery)) {
                preparedStatement.setInt(1, projectId);
                preparedStatement.executeUpdate();
            }

            // Delete the project
            String deleteProjectQuery = "DELETE FROM projects WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProjectQuery)) {
                preparedStatement.setInt(1, projectId);
                preparedStatement.executeUpdate();
            }

            // Re-enable foreign key checks
            connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1").execute();

            System.out.println("Deleted project with ID: " + projectId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTotalPaidForProject(double totalBezahlt,int projekId) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection("project_management");
             PreparedStatement stmt = conn.prepareStatement("UPDATE projects SET amount_paid = ? WHERE id = ?")) {
            stmt.setDouble(1, totalBezahlt);
            stmt.setInt(2, projekId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                //System.out.println("Update successful for project ID: " + projekId);
                NotificationHelper.showSuccessNotification("Erfolg","Update successful");
            } else {
                NotificationHelper.showFailedNotification("Fehler","Kein Projekt gefunden");
            }
        } catch (SQLException e) {
            NotificationHelper.showFailedNotification("Fehler","Total bezahlte Beiträge könnten nicht aktualisiert werdem");
            e.printStackTrace();
            throw e;
        }
    }

   // public static void updateProjectInDatabase(int id, String name, double budget, int sharedPersons, LocalDate startDate, LocalDate endDate) {
   public static void updateProjectInDatabase(int id, String name, double budget, int sharedPersons, LocalDate startDate, LocalDate endDate) throws SQLException {
       String query = "UPDATE projects SET name=?, budget=?, shared_persons=?, startdate=?, enddate=? WHERE id=?";
       try (Connection connection = DatabaseManager.getConnectToanyDb("project_management", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
           preparedStatement.setString(1, name);
           preparedStatement.setDouble(2, budget);
           preparedStatement.setInt(3, sharedPersons);
           preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
           preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
           preparedStatement.setInt(6, id);

           int rowsAffected = preparedStatement.executeUpdate();
           if (rowsAffected == 0) {
               throw new SQLException("Updating project failed, no rows affected.");
           }
       } catch (SQLException e) {
           e.printStackTrace();
           throw new SQLException("Failed to update project in the database.", e);
       }
   }

 }


