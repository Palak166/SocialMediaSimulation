// src/com/socialMedia/model/Post.java
package com.socialmedia.model;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private int userId;
    private String content;
    private LocalDateTime createdAt;

    // Constructor for creating a new post
    public Post(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    // Constructor for retrieving post from DB
    public Post(int id, int userId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}