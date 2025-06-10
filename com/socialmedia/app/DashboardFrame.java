// src/com/socialMedia/app/DashboardFrame.java
package com.socialmedia.app;

import com.socialmedia.dao.PostDAO;
import com.socialmedia.dao.UserDAO;
import com.socialmedia.model.Post;
import com.socialmedia.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private PostDAO postDAO;
    private UserDAO userDAO;
    private JTextArea postContentArea;
    private JPanel feedPanel;// Panel to display posts
    private JLabel charCountLabel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        // DAO instances are initialized here. Add userDAO as well.
        try { // It's good practice to wrap DAO initialization in a try-catch for DB errors
            this.postDAO = new PostDAO();
            this.userDAO = new UserDAO(); // <--- ADD THIS LINE
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not initialize database components. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);     // <--- CHANGE: Exit the application gracefully
            // No return needed after System.exit(1) as the JVM will terminate.
        }

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

        // --- ADD CHARACTER COUNT LABEL AND LISTENER HERE ---
        charCountLabel = new JLabel("Characters: 0"); // Initialize the label
        charCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Optional: smaller font
        newPostPanel.add(charCountLabel, BorderLayout.SOUTH); // Add it to the bottom of newPostPanel

        // Add DocumentListener to the text area
        postContentArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateCharCount();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateCharCount();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateCharCount(); // Plain text components usually don't fire this for content changes
            }
        });
        // --- END CHARACTER COUNT CODE ---

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
        // --- ADD THIS CODE FOR PROFILE BUTTON ---
        JButton profileButton = new JButton("My Profile");
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProfileDialog(); // This method will be defined next
            }
        });
        bottomPanel.add(profileButton); // Add profile button to the panel
// --- END OF PROFILE BUTTON CODE ---
        add(bottomPanel, BorderLayout.SOUTH);
        loadPosts(); // Load posts when the dashboard opens
    }


    private void createNewPost() {
        String content = postContentArea.getText().trim();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Post content cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop if content is empty
        }

        Post newPost = new Post(currentUser.getId(), content);
        if (postDAO.createPost(newPost)) {
            JOptionPane.showMessageDialog(this, "Post created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            postContentArea.setText(""); // Clear the text area
            loadPosts(); // Reload posts to show the new one
        } else {
            // DAO returned false, meaning post creation failed due to DB error
            JOptionPane.showMessageDialog(this, "Failed to create post. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
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
                // --- CORRECTED SECTION FOR USERNAME ---
                        // Fetch the User object using the user_id from the post
                        User poster = userDAO.getUserById(post.getUserId());
                // Get the username; if user is null (e.g., deleted), show "Unknown User"
                String username = (poster != null) ? poster.getUsername() : "Unknown User";
                // --- END OF CORRECTED SECTION ---

                JTextArea postDisplay = new JTextArea(
                        username + " posted:\n" +
                                post.getContent() + "\n" +
                                "(" + post.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ")"
                );
                postDisplay.setEditable(false);
                postDisplay.setLineWrap(true);
                postDisplay.setWrapStyleWord(true);
                postDisplay.setBorder(BorderFactory.createCompoundBorder( // Add border and padding
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true), // Light gray border with rounded corners (true for rounded)
                        BorderFactory.createEmptyBorder(10, 10, 10, 10) // Inner padding
                ));
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
    private void showProfileDialog() {
        JDialog profileDialog = new JDialog(this, "User Profile", true);
        profileDialog.setSize(300, 200);
        profileDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (currentUser != null) {
            JLabel usernameLabel = new JLabel("Username: " + currentUser.getUsername());
            usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            panel.add(usernameLabel);

            JLabel emailLabel = new JLabel("Email: " + currentUser.getEmail());
            emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panel.add(emailLabel);

            JLabel memberSinceLabel = new JLabel("Member Since: " + currentUser.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            memberSinceLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            panel.add(memberSinceLabel);
        } else {
            panel.add(new JLabel("User data not available."));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> profileDialog.dispose());
        buttonPanel.add(closeButton);

        profileDialog.add(panel, BorderLayout.CENTER);
        profileDialog.add(buttonPanel, BorderLayout.SOUTH);

        profileDialog.setVisible(true);
    }
    // In src/com/socialMedia/app/DashboardFrame.java
// ... (after showProfileDialog() method) ...

    /**
     * Updates the text of the character count label based on the current content of postContentArea.
     */
    private void updateCharCount() {
        int charCount = postContentArea.getText().length();
        charCountLabel.setText("Characters: " + charCount);
        // Optional: Add a character limit (e.g., 200 characters)
        // if (charCount > 200) {
        //     charCountLabel.setForeground(Color.RED);
        //     // Also prevent posting if over limit in createNewPost()
        // } else {
        //     charCountLabel.setForeground(Color.BLACK); // Or default color
        // }
    }
}

