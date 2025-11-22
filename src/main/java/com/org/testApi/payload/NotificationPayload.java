package com.org.testApi.payload;

public class NotificationPayload {
    private Long userId;
    private String message;

    // Default constructor
    public NotificationPayload() {
    }

    // Constructor with parameters
    public NotificationPayload(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}