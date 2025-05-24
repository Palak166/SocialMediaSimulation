// src/com/socialmedia/app/DashboardFrame.java
package com.socialmedia.app;

import com.socialmedia.dao.PostDAO;
import com.socialmedia.model.Post;
import com.socialmedia.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private PostDAO postDAO;
    private JTextArea postContentArea;
    private JPanel feedPanel; // Panel to display posts

    public DashboardFrame(User user) {
        this.currentUser = user;
        this.postDAO = new PostDAO();
        setTitle("Social Media Dashboard - Welcome, " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set a better look and feel (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Ignore if Nimbus is not available
        }

        setLayout(new BorderLayout(10, 10)); // Outer layout

        // --- Top Panel for New Post ---
        JPanel newPostPanel = new JPanel(new BorderLayout(5, 5));
        newPostPanel.setBorder(BorderFactory.createTitledBorder("Create New Post"));
        postContentArea = new JTextArea(5, 40);
        postContentArea.setLineWrap(true);
        postContentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(postContentArea);
        newPostPanel.add(scrollPane, BorderLayout.CENTER);

        JButton postButton = new JButton("Post");
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewPost();
            }
        });
        newPostPanel.add(postButton, BorderLayout.EAST);
        add(newPostPanel, BorderLayout.NORTH);

        // --- Feed Panel for Displaying Posts ---
        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS)); // Arrange posts vertically
        JScrollPane feedScrollPane = new JScrollPane(feedPanel);
        feedScrollPane.setBorder(BorderFactory.createTitledBorder("Public Feed"));
        add(feedScrollPane, BorderLayout.CENTER);

        // --- Logout Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadPosts(); // Load posts when the dashboard opens
    }

    private void createNewPost() {
        String content = postContentArea.getText().trim();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Post content cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Post newPost = new Post(currentUser.getId(), content);
        if (postDAO.createPost(newPost)) {
            JOptionPane.showMessageDialog(this, "Post created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            postContentArea.setText(""); // Clear the text area
            loadPosts(); // Reload posts to show the new one
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create post.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPosts() {
        feedPanel.removeAll(); // Clear existing posts
        List<Post> posts = postDAO.getAllPosts(); // Get all posts
        // For a more complete social media, you'd fetch posts with usernames
        // For now, we'll just show the content and user ID

        if (posts.isEmpty()) {
            feedPanel.add(new JLabel("No posts yet. Be the first to post!"));
        } else {
            for (Post post : posts) {
                // In a real application, you'd fetch the username using post.getUserId()
                // For simplicity here, we'll just display "User [ID]"
                String username = "User " + post.getUserId();
                // You could add a UserDAO method to get username by ID.
                // User poster = userDAO.getUserById(post.getUserId());
                // String username = poster != null ? poster.getUsername() : "Unknown User";

                JTextArea postDisplay = new JTextArea(
                        username + " posted:\n" +
                                post.getContent() + "\n" +
                                "(" + post.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ")"
                );
                postDisplay.setEditable(false);
                postDisplay.setLineWrap(true);
                postDisplay.setWrapStyleWord(true);
                postDisplay.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                postDisplay.setPreferredSize(new Dimension(700, 100)); // Adjust size as needed
                postDisplay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Allow vertical expansion

                JPanel singlePostPanel = new JPanel(new BorderLayout());
                singlePostPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding
                singlePostPanel.add(postDisplay, BorderLayout.CENTER);
                feedPanel.add(singlePostPanel);
            }
        }
        feedPanel.revalidate();
        feedPanel.repaint();
    }

    private void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            LoginRegisterFrame loginRegisterFrame = new LoginRegisterFrame();
            loginRegisterFrame.setVisible(true);
            this.dispose(); // Close dashboard
        }
    }
}