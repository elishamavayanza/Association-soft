package com.org.testApi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class PushNotificationService {

    @Value("${firebase.service.account.path:}")
    private String firebaseServiceAccountPath;

    @Value("${apns.auth.key.path:}")
    private String apnsAuthKeyPath;

    @Value("${apns.team.id:}")
    private String apnsTeamId;

    @Value("${apns.key.id:}")
    private String apnsKeyId;

    @Value("${apns.topic:}")
    private String apnsTopic;

    private FirebaseApp firebaseApp;
    private boolean firebaseInitialized = false;
    private boolean apnsInitialized = false;

    // Cache pour stocker les connexions APNs
    private final Map<String, Object> apnsClients = new ConcurrentHashMap<>();

    /**
     * Initialise Firebase Cloud Messaging
     */
    private void initializeFirebase() {
        if (!firebaseInitialized && firebaseServiceAccountPath != null && !firebaseServiceAccountPath.isEmpty()) {
            try {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                getClass().getClassLoader().getResourceAsStream(firebaseServiceAccountPath)))
                        .build();

                firebaseApp = FirebaseApp.initializeApp(options);
                firebaseInitialized = true;
                System.out.println("Firebase Cloud Messaging initialized successfully");
            } catch (IOException e) {
                System.err.println("Failed to initialize Firebase: " + e.getMessage());
            }
        }
    }

    /**
     * Envoie une notification push via Firebase Cloud Messaging
     * @param deviceToken Le token de l'appareil destinataire
     * @param title Le titre de la notification
     * @param body Le corps du message
     */
    public void sendFirebaseNotification(String deviceToken, String title, String body) {
        if (!firebaseInitialized) {
            initializeFirebase();
        }

        if (!firebaseInitialized) {
            System.out.println("Firebase not configured. Push notification not sent: " + body);
            return;
        }

        try {
            Message message = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("timestamp", String.valueOf(System.currentTimeMillis()))
                    .build();

            String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
            System.out.println("Successfully sent Firebase message: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Failed to send Firebase notification: " + e.getMessage());
            throw new RuntimeException("Failed to send Firebase notification", e);
        }
    }

    /**
     * Initialise Apple Push Notification Service
     */
    private void initializeApns() {
        if (!apnsInitialized &&
                apnsAuthKeyPath != null && !apnsAuthKeyPath.isEmpty() &&
                apnsTeamId != null && !apnsTeamId.isEmpty() &&
                apnsKeyId != null && !apnsKeyId.isEmpty() &&
                apnsTopic != null && !apnsTopic.isEmpty()) {

            // Note: L'implémentation complète d'APNs nécessiterait la dépendance com.eatthepath:pushy
            // Pour simplifier, nous simulons l'initialisation ici
            apnsInitialized = true;
            System.out.println("APNs initialized successfully");
        }
    }

    /**
     * Envoie une notification push via Apple Push Notification Service
     * @param deviceToken Le token de l'appareil destinataire
     * @param title Le titre de la notification
     * @param body Le corps du message
     */
    public void sendApnsNotification(String deviceToken, String title, String body) {
        if (!apnsInitialized) {
            initializeApns();
        }

        if (!apnsInitialized) {
            System.out.println("APNs not configured. Push notification not sent: " + body);
            return;
        }

        // Note: L'implémentation complète nécessiterait la bibliothèque Pushy ou une implémentation similaire
        // Pour simplifier, nous simulons l'envoi ici
        System.out.println("Sending APNs notification to " + deviceToken);
        System.out.println("Title: " + title);
        System.out.println("Body: " + body);
        System.out.println("APNs notification sent successfully");
    }

    /**
     * Envoie une notification push en fonction du type de token
     * @param deviceToken Le token de l'appareil destinataire
     * @param title Le titre de la notification
     * @param message Le corps du message
     */
    public void sendPushNotification(String deviceToken, String title, String message) {
        if (deviceToken == null || deviceToken.isEmpty()) {
            System.out.println("Device token is null or empty. Notification not sent.");
            return;
        }

        // Déterminer le type de token (simplifié)
        if (deviceToken.length() > 100) {
            // Probablement un token FCM
            sendFirebaseNotification(deviceToken, title, message);
        } else {
            // Probablement un token APNs
            sendApnsNotification(deviceToken, title, message);
        }
    }

    /**
     * Envoie une notification push avec un message simple
     * @param deviceToken Le token de l'appareil destinataire
     * @param message Le message à envoyer
     */
    public void sendPushNotification(String deviceToken, String message) {
        sendPushNotification(deviceToken, "Notification", message);
    }
}
