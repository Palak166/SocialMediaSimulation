// src/com/socialMedia/app/LoginRegisterFrame.java
package com.socialmedia.app;

import com.socialmedia.dao.UserDAO;
import com.socialmedia.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginRegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailRegisterField;
    private JTextField usernameRegisterField;
    private JPasswordField passwordRegisterField;
    private JPasswordField confirmPasswordField;

    private UserDAO userDAO;

    public LoginRegisterFrame() {
        // --- ADD THIS TRY-CATCH BLOCK ---
        try {
            userDAO = new UserDAO(); // Initialize UserDAO
            // Optional: You can add a small test here if your UserDAO constructor doesn't
            // inherently test the connection, though usually it will fail on first operation.
            // For example:
            // if (userDAO.loginUser("nonexistent", "password") != null) { /* this won't happen but forces connection */ }
        } catch (Exception e) { // Catching a broad 'Exception' because DBConnection issues might throw different types
            JOptionPane.showMessageDialog(this,
                    "<html><font color='red'><b>Database Connection Error:</b></font><br>" +
                            "The application could not connect to the database.<br>" +
                            "Please ensure the MySQL server is running and configured correctly.",
                    "Application Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log the full error to console for debugging
            System.exit(1);     // Exit the application gracefully as it cannot function without DB
            return;             // Stop constructor execution
        }
        // --- END OF ADDED TRY-CATCH ---
        setTitle("Social Media Simulation - Login/Register");
        setSize(800, 500); // Increased size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set a better look and feel (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the default look and feel.
        }

        setLayout(new GridLayout(1, 2, 10, 10)); // Two columns for login and register

        // --- Login Panel ---
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        loginPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        loginPanel.add(loginButton, gbc);

        add(loginPanel);

        // --- Register Panel ---
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(BorderFactory.createTitledBorder("Register"));
        GridBagConstraints gbcReg = new GridBagConstraints();
        gbcReg.insets = new Insets(5, 5, 5, 5); // Padding

        // Username
        gbcReg.gridx = 0;
        gbcReg.gridy = 0;
        gbcReg.anchor = GridBagConstraints.EAST;
        registerPanel.add(new JLabel("Username:"), gbcReg);
        gbcReg.gridx = 1;
        gbcReg.anchor = GridBagConstraints.WEST;
        usernameRegisterField = new JTextField(20);
        registerPanel.add(usernameRegisterField, gbcReg);

        // Email
        gbcReg.gridx = 0;
        gbcReg.gridy = 1;
        gbcReg.anchor = GridBagConstraints.EAST;
        registerPanel.add(new JLabel("Email:"), gbcReg);
        gbcReg.gridx = 1;
        gbcReg.anchor = GridBagConstraints.WEST;
        emailRegisterField = new JTextField(20);
        registerPanel.add(emailRegisterField, gbcReg);

        // Password
        gbcReg.gridx = 0;
        gbcReg.gridy = 2;
        gbcReg.anchor = GridBagConstraints.EAST;
        registerPanel.add(new JLabel("Password:"), gbcReg);
        gbcReg.gridx = 1;
        gbcReg.anchor = GridBagConstraints.WEST;
        passwordRegisterField = new JPasswordField(20);
        registerPanel.add(passwordRegisterField, gbcReg);

        // Confirm Password
        gbcReg.gridx = 0;
        gbcReg.gridy = 3;
        gbcReg.anchor = GridBagConstraints.EAST;
        registerPanel.add(new JLabel("Confirm Password:"), gbcReg);
        gbcReg.gridx = 1;
        gbcReg.anchor = GridBagConstraints.WEST;
        confirmPasswordField = new JPasswordField(20);
        registerPanel.add(confirmPasswordField, gbcReg);

        // Register Button
        gbcReg.gridx = 0;
        gbcReg.gridy = 4;
        gbcReg.gridwidth = 2;
        gbcReg.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });
        registerPanel.add(registerButton, gbcReg);

        add(registerPanel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        User user = userDAO.loginUser(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + user.getUsername() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            DashboardFrame dashboard = new DashboardFrame(user);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Login failed. Invalid username or password. Please try again.", // Slightly enhanced
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performRegistration() {
        String username = usernameRegisterField.getText();
        String email = emailRegisterField.getText();
        String password = new String(passwordRegisterField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Basic validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required for registration.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User(username, password, email);
        if (userDAO.registerUser(newUser)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            // Clear fields after successful registration
            usernameRegisterField.setText("");
            emailRegisterField.setText("");
            passwordRegisterField.setText("");
            confirmPasswordField.setText("");
        } else {
            // This message is already quite good, as the DAO specifically logs the duplicate error.
            // We can add "Please try a different username or email." for more direct guidance.
            JOptionPane.showMessageDialog(this,
                    "Registration failed. This username or email might already be in use. Please try a different username or email.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginRegisterFrame().setVisible(true);
            }
        });
    }
}

