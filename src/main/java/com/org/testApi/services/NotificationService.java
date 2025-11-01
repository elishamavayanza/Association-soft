package com.org.testApi.services;

import com.org.testApi.models.User;

/**
 * Service interface for handling notifications across multiple channels
 * including email, SMS, and push notifications.
 */
public interface NotificationService extends ObservableService<String> {
    
    /**
     * Send a notification to a specific user through all available channels
     * (email, SMS, push) based on the user's configured contact information.
     * 
     * @param user The user to send the notification to
     * @param message The message content to send
     */
    void sendNotificationToUser(User user, String message);
    
    /**
     * Send a notification to all users in the system.
     * 
     * @param message The message content to send
     */
    void sendNotificationToAllUsers(String message);
    
    /**
     * Send a notification to all users with a specific role.
     * 
     * @param role The role name to filter users by
     * @param message The message content to send
     */
    void sendNotificationToRole(String role, String message);
}