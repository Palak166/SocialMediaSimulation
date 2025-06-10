// src/com/socialMedia/util/DBConnection.java
package com.socialmedia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/social_media_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "Sun$et_R0ckz!92"; // Your MySQL root password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
