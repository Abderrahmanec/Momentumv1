package com.example.momentumv1;

import org.apache.commons.codec.digest.DigestUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;

public class DatabaseManager {

    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    static {
        Properties properties = new Properties();

        try (FileInputStream db_configLoad = new FileInputStream("src/main/java/com/example/momentumv1/db_config.properties")) {
            properties.load(db_configLoad);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }

        DB_URL =Appconfig.getDbUrl();
        DB_USER =Appconfig.getDbUsername();
        DB_PASSWORD = Appconfig.getDbPassword();

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static boolean authenticateUser(String username, String password) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String salt = resultSet.getString("salt");
                        if (salt == null || salt.isEmpty()) {

                            // Handle missing salt (e.g., prompt to reset password)
                            return false;
                        }
                        String hashedPassword = hashPassword(password, salt);
                        sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                        try (PreparedStatement passwordStatement = connection.prepareStatement(sql)) {
                            passwordStatement.setString(1, username);
                            passwordStatement.setString(2, hashedPassword);
                            try (ResultSet passwordResultSet = passwordStatement.executeQuery()) {
                                return passwordResultSet.next(); // Return true if password matches
                            }
                        }
                    } else {
                        System.out.println("User not found: " + username);
                        return false; // User not found
                    }
                }
            }
        } catch (SQLException e) {
            // Log the exception with sanitized data (avoid sensitive info)
            System.err.println("Authentication error: " + e.getMessage().replaceAll("password", "*****"));
            return false;
        }
    }

    private static String hashPassword(String password, String salt) {
        // Check if password or salt is null
        if (password == null || salt == null) {
            throw new IllegalArgumentException("Password or salt cannot be null");
        }

        // Combine password and salt, then hash using a strong algorithm (replace SHA-256)
        String combined = password + salt;
        // Consider using a secure hashing function like bcrypt or scrypt
        // (use a dedicated library for secure implementation)
        return DigestUtils.sha256Hex(combined);
    }

    public static class SaltGenerator {

        public static String generateSalt() {
            SecureRandom random = new SecureRandom();
            // Adjust size based on your security needs (typically 32 bytes)
            byte[] saltBytes = new byte[32];
            random.nextBytes(saltBytes);
            // Convert bytes to a secure string representation (e.g., Base64)
            return new String(Base64.getEncoder().encode(saltBytes));
        }
    }

    public static boolean updatePassword(String username, String newPassword) {
        try (Connection connection = getConnection()) {
            String salt = SaltGenerator.generateSalt();
            String hashedPassword = hashPassword(newPassword, salt);
            String sql = "UPDATE users SET password = ?, salt = ? WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, hashedPassword);
                statement.setString(2, salt);
                statement.setString(3, username);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            // Log the exception with sanitized data (avoid sensitive info)
            System.err.println("Password update error: " + e.getMessage().replaceAll("password", "*****"));
            return false;
        }
    }
}