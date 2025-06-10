// src/com/socialMedia/dao/PostDAO.java
package com.socialmedia.dao;

import com.socialmedia.model.Post;
import com.socialmedia.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    public boolean createPost(Post post) {
        String sql = "INSERT INTO posts (user_id, content) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, post.getUserId());
            stmt.setString(2, post.getContent());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Post created successfully for user ID: " + post.getUserId()); // Add success message
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database Error during post creation for user ID: " + post.getUserId()); // More specific message
            System.err.println("SQLState: " + e.getSQLState()); // Add SQLState
            System.err.println("Error Code: " + e.getErrorCode()); // Add Error Code
            e.printStackTrace(); // <-- CRUCIAL: Print the full stack trace for debugging
            // In case of content too long, etc., this will help identify.
        }
        return false;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT id, user_id, content, created_at FROM posts ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String content = rs.getString("content");
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;
                posts.add(new Post(id, userId, content, createdAt));
            }
        } catch (SQLException e) {
            System.err.println("Database Error getting all posts."); // More specific message
            System.err.println("SQLState: " + e.getSQLState()); // Add SQLState
            System.err.println("Error Code: " + e.getErrorCode()); // Add Error Code
            e.printStackTrace(); // <-- CRUCIAL: Print the full stack trace for debugging
        }
        return posts;
    }

    public List<Post> getPostsByUserId(int userId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT id, user_id, content, created_at FROM posts WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int postUserId = rs.getInt("user_id");
                    String content = rs.getString("content");
                    Timestamp ts = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;
                    posts.add(new Post(id, postUserId, content, createdAt));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error getting posts by user ID: " + userId); // More specific message
            System.err.println("SQLState: " + e.getSQLState()); // Add SQLState
            System.err.println("Error Code: " + e.getErrorCode()); // Add Error Code
            e.printStackTrace(); // <-- CRUCIAL: Print the full stack trace for debugging
        }
        return posts;
    }
}
