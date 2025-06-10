// src/com/socialMedia/dao/UserDAO.java
package com.socialmedia.dao;

import com.socialmedia.model.User;
import com.socialmedia.util.DBConnection;

import java.sql.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // In a real app, hash this password!
            stmt.setString(3, user.getEmail());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("User registered successfully: " + user.getUsername()); // <--- ADD THIS
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database Error during user registration for: " + user.getUsername()); // <--- ENHANCED MESSAGE
            System.err.println("SQLState: " + e.getSQLState()); // <--- ADD THIS
            System.err.println("Error Code: " + e.getErrorCode()); // <--- ADD THIS
            e.printStackTrace(); // <--- ADD THIS: Crucial for full stack trace

            // Specific handling for duplicate entry error (MySQL error code 1062)
            if (e.getErrorCode() == 1062) {
                System.err.println("Reason: Username or Email already exists."); // <--- ENHANCED MESSAGE
            }
            // Optional: You could add more specific checks for other error codes if needed.
        }
        return false;
    }

    public User loginUser(String username, String password) {
        String sql = "SELECT id, username, password, email, created_at FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // In a real app, compare with hashed password!

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String user = rs.getString("username"); // Renamed variable to avoid conflict with class name
                    String pass = rs.getString("password");
                    String email = rs.getString("email");
                    Timestamp ts = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;
                    return new User(id, user, pass, email, createdAt);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error during user login for: " + username); // <--- ENHANCED MESSAGE
            System.err.println("SQLState: " + e.getSQLState()); // <--- ADD THIS
            System.err.println("Error Code: " + e.getErrorCode()); // <--- ADD THIS
            e.printStackTrace(); // <--- ADD THIS: Crucial for full stack trace
        }
        return null;
    }
    // ... (after loginUser method) ...

    public User getUserById(int userId) { // <-- Renamed parameter to userId for clarity
        String sql = "SELECT id, username, password, email, created_at FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId); // Set the user ID for the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Reconstruct User object from database data
                    int id = rs.getInt("id"); // Get ID from ResultSet
                    String username = rs.getString("username");
                    String pass = rs.getString("password"); // Not strictly needed for display, but good to fetch
                    String email = rs.getString("email");
                    Timestamp ts = rs.getTimestamp("created_at");
                    // Convert Timestamp to LocalDateTime
                    LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;
                    return new User(id, username, pass, email, createdAt);
                }
            }
        }  catch (SQLException e) {
            System.err.println("Database Error getting user by ID: " + userId); // <--- ENHANCED MESSAGE
            System.err.println("SQLState: " + e.getSQLState()); // <--- ADDED THIS for consistency
            System.err.println("Error Code: " + e.getErrorCode()); // <--- ADDED THIS for consistency
            e.printStackTrace();
        }
        return null;
    }
} // End of UserDAO class

