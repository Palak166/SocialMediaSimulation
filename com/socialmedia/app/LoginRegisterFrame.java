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
        userDAO = new UserDAO();
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
            // Open the main social media dashboard
            DashboardFrame dashboard = new DashboardFrame(user);
            dashboard.setVisible(true);
            this.dispose(); // Close login/register window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Registration failed. Username or email might already exist.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
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
