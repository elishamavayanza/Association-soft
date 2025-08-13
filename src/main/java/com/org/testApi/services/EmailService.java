package com.org.testApi.services;

public interface EmailService extends ObservableService<String> {
    void sendEmail(String to, String subject, String body);
    void sendWelcomeEmail(String userEmail);
    void sendPasswordResetEmail(String userEmail);
}
