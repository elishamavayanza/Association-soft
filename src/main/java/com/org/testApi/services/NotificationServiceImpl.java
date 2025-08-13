package com.org.testApi.services;

import com.org.testApi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsNotificationService smsService;

    @Autowired
    private PushNotificationService pushService;

    private List<Observer<String>> observers = new ArrayList<>();

    @Override
    public void sendNotificationToUser(User user, String message) {
        // Envoyer une notification par email
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            try {
                emailService.sendEmail(
                        user.getEmail(),
                        "Notification de l'application",
                        message
                );
            } catch (Exception e) {
                System.err.println("Failed to send email to user " + user.getUsername() + ": " + e.getMessage());
            }
        }

        // Envoyer un SMS si le numéro de téléphone est disponible
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            try {
                smsService.sendSms(user.getPhoneNumber(), message);
            } catch (Exception e) {
                System.err.println("Failed to send SMS to user " + user.getUsername() + ": " + e.getMessage());
            }
        }

        // Envoyer une notification push si le token est disponible
        // Vérifier d'abord si la méthode getDeviceToken existe et si le token est disponible
        try {
            if (user.getClass().getMethod("getDeviceToken") != null) {
                String deviceToken = user.getDeviceToken();
                if (deviceToken != null && !deviceToken.isEmpty() && pushService != null) {
                    pushService.sendPushNotification(deviceToken, "Notification", message);
                }
            }
        } catch (NoSuchMethodException e) {
            System.out.println("Device token not supported for this user entity");
        } catch (Exception e) {
            System.err.println("Failed to send push notification to user " + user.getUsername() + ": " + e.getMessage());
        }

        System.out.println("Sending notification to user: " + user.getUsername());
        System.out.println("Message: " + message);
        System.out.println("Notification sent successfully!");

        // Notifier les observateurs qu'une notification a été envoyée à un utilisateur
        notifyObservers("NOTIFICATION_SENT_TO_USER", user.getUsername() + ":" + message);
    }

    @Override
    public void sendNotificationToAllUsers(String message) {
        // Récupérer tous les utilisateurs actifs
        List<User> allUsers = userService.getAllUsers();

        // Envoyer des notifications à tous les utilisateurs
        for (User user : allUsers) {
            sendNotificationToUser(user, message);
        }

        System.out.println("Sending notification to all users");
        System.out.println("Message: " + message);
        System.out.println("Broadcast notification sent successfully!");

        // Notifier les observateurs qu'une notification a été envoyée à tous les utilisateurs
        notifyObservers("NOTIFICATION_SENT_TO_ALL_USERS", message);
    }

    @Override
    public void sendNotificationToRole(String role, String message) {
        // Récupérer tous les utilisateurs avec le rôle spécifié
        List<User> usersWithRole = userService.getAllUsers().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(userRole -> userRole.getName().equals(role)))
                .collect(Collectors.toList());

        // Envoyer des notifications aux utilisateurs avec le rôle spécifié
        for (User user : usersWithRole) {
            sendNotificationToUser(user, message);
        }

        System.out.println("Sending notification to users with role: " + role);
        System.out.println("Message: " + message);
        System.out.println("Role-based notification sent successfully!");

        // Notifier les observateurs qu'une notification a été envoyée à un rôle spécifique
        notifyObservers("NOTIFICATION_SENT_TO_ROLE", role + ":" + message);
    }

    @Override
    public void addObserver(Observer<String> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, String entity) {
        for (Observer<String> observer : observers) {
            observer.update(event, entity);
        }
    }
}
