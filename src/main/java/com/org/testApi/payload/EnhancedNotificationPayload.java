package com.org.testApi.payload;

public class EnhancedNotificationPayload extends NotificationPayload {
    private String notificationType; // URGENT_REMINDER, PAYMENT_DUE, DISTRIBUTION_ANNOUNCEMENT, PENALTY_WARNING
    private Long entityId; // ID of the related entity (round, contribution, etc.)

    // Default constructor
    public EnhancedNotificationPayload() {
        super();
    }

    // Constructor with parameters
    public EnhancedNotificationPayload(Long userId, String message, String notificationType, Long entityId) {
        super(userId, message);
        this.notificationType = notificationType;
        this.entityId = entityId;
    }

    // Getters and setters
    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}