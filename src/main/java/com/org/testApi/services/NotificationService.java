package com.org.testApi.services;

import com.org.testApi.models.User;

public interface NotificationService {
    
    /**
     * Sends a notification to a specific user
     * @param user The user to send the notification to
     * @param message The message content
     */
    void sendNotificationToUser(User user, String message);
    
    /**
     * Sends a notification to all users
     * @param message The message content
     */
    void sendNotificationToAllUsers(String message);
    
    /**
     * Sends a notification to users with a specific role
     * @param role The role to target
     * @param message The message content
     */
    void sendNotificationToRole(String role, String message);

    void addObserver(Observer<String> observer);

    void removeObserver(Observer<String> observer);

    void notifyObservers(String event, String entity);

    /**
     * Sends an email notification to the specified email address
     * @param emailAddress The recipient's email address
     * @param subject The email subject
     * @param message The email message content
     */
    void sendEmailNotification(String emailAddress, String subject, String message);
    
    /**
     * Sends an SMS notification to the specified phone number
     * @param phoneNumber The recipient's phone number
     * @param message The SMS message content
     */
    void sendSmsNotification(String phoneNumber, String message);
}