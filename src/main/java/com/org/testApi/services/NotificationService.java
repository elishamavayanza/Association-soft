package com.org.testApi.services;

import com.org.testApi.models.User;

public interface NotificationService extends ObservableService<String> {
    void sendNotificationToUser(User user, String message);
    void sendNotificationToAllUsers(String message);
    void sendNotificationToRole(String role, String message);
}
