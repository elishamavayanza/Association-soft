package com.org.testApi.services;

import com.org.testApi.models.User;
import com.org.testApi.models.Round;
import com.org.testApi.models.Contribution;
import com.org.testApi.models.Penalty;
import com.org.testApi.models.Member;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private List<Observer<String>> observers = new ArrayList<>();

    @Override
    public void sendNotificationToUser(User user, String message) {
        // Envoyer une notification par email
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            try {
                sendEmailNotification(
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
                sendSmsNotification(user.getPhoneNumber(), message);
            } catch (Exception e) {
                System.err.println("Failed to send SMS to user " + user.getUsername() + ": " + e.getMessage());
            }
        }

        System.out.println("Sending notification to user: " + user.getUsername());
        System.out.println("Message: " + message);
        System.out.println("Notification sent successfully!");

        // Notifier les observateurs qu'une notification a été envoyée à un utilisateur
        notifyObservers("NOTIFICATION_SENT_TO_USER", user.getUsername() + ":" + message);
    }

    @Override
    public void sendNotificationToAllUsers(String message) {
        System.out.println("Sending notification to all users");
        System.out.println("Message: " + message);
        System.out.println("Broadcast notification sent successfully!");

        // Notifier les observateurs qu'une notification a été envoyée à tous les utilisateurs
        notifyObservers("NOTIFICATION_SENT_TO_ALL_USERS", message);
    }

    @Override
    public void sendNotificationToRole(String role, String message) {
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
    
    /**
     * Sends an email notification to the specified email address
     * @param emailAddress The recipient's email address
     * @param subject The email subject
     * @param message The email message content
     */
    @Override
    public void sendEmailNotification(String emailAddress, String subject, String message) {
        // In a real implementation, this would integrate with an email service like SendGrid, SMTP, etc.
        System.out.println("EMAIL NOTIFICATION");
        System.out.println("To: " + emailAddress);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("------------------------");
    }
    
    /**
     * Sends an SMS notification to the specified phone number
     * @param phoneNumber The recipient's phone number
     * @param message The SMS message content
     */
    @Override
    public void sendSmsNotification(String phoneNumber, String message) {
        // In a real implementation, this would integrate with an SMS service like Twilio, etc.
        System.out.println("SMS NOTIFICATION");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: " + message);
        System.out.println("------------------------");
    }
    
    /**
     * Sends a reminder for an upcoming round
     * @param round The round for which to send the reminder
     */
    @Override
    public void sendUpcomingRoundReminder(Round round) {
        String message = String.format(
            "RAPPEL: Le tour #%d du groupe \"%s\" commencera le %s. " +
            "N'oubliez pas de faire votre contribution de %s.",
            round.getRoundNumber(),
            round.getRotatingGroup().getName(),
            round.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            round.getRotatingGroup().getContributionAmount()
        );
        
        // Send to all group members
        for (Member member : round.getRotatingGroup().getMembers()) {
            if (member.getUser() != null) {
                sendNotificationToUser(member.getUser(), message);
            }
        }
        
        notifyObservers("UPCOMING_ROUND_REMINDER_SENT", 
                       "Round #" + round.getRoundNumber() + " of group " + round.getRotatingGroup().getName());
    }
    
    /**
     * Sends a payment due notification to a member
     * @param contribution The contribution that is due
     */
    @Override
    public void sendPaymentDueNotification(Contribution contribution) {
        Member member = contribution.getMember();
        Round round = contribution.getRound();
        
        String message = String.format(
            "RAPPEL DE PAIEMENT: Votre contribution de %s pour le tour #%d du groupe \"%s\" est due avant le %s.",
            contribution.getAmount(),
            round.getRoundNumber(),
            round.getRotatingGroup().getName(),
            contribution.getContributionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        
        if (member.getUser() != null) {
            sendNotificationToUser(member.getUser(), message);
        }
        
        notifyObservers("PAYMENT_DUE_NOTIFICATION_SENT", 
                       "Payment due notification for member " + member.getId() + 
                       " for round #" + round.getRoundNumber());
    }
    
    /**
     * Sends an announcement about fund distribution
     * @param round The round for which funds are being distributed
     */
    @Override
    public void sendDistributionAnnouncement(Round round) {
        String message = String.format(
            "ANNONCE DE DISTRIBUTION: Les fonds du tour #%d du groupe \"%s\" ont été distribués. " +
            "Montant total distribué: %s.",
            round.getRoundNumber(),
            round.getRotatingGroup().getName(),
            round.getTotalAmountDistributed()
        );
        
        // Send to all beneficiaries
        for (Member beneficiary : round.getBeneficiaries()) {
            if (beneficiary.getUser() != null) {
                sendNotificationToUser(beneficiary.getUser(), message);
            }
        }
        
        notifyObservers("DISTRIBUTION_ANNOUNCEMENT_SENT", 
                       "Distribution announcement for round #" + round.getRoundNumber() + 
                       " of group " + round.getRotatingGroup().getName());
    }
    
    /**
     * Sends a warning about an incurred penalty
     * @param penalty The penalty that was incurred
     */
    @Override
    public void sendPenaltyWarning(Penalty penalty) {
        Member member = penalty.getMember();
        Round round = penalty.getRound();
        
        String message = String.format(
            "AVERTISSEMENT DE PÉNALITÉ: Une pénalité de %s a été appliquée pour le tour #%d du groupe \"%s\". " +
            "Raison: %s.",
            penalty.getAmount(),
            round.getRoundNumber(),
            round.getRotatingGroup().getName(),
            penalty.getReason()
        );
        
        if (member.getUser() != null) {
            sendNotificationToUser(member.getUser(), message);
        }
        
        notifyObservers("PENALTY_WARNING_SENT", 
                       "Penalty warning for member " + member.getId() + 
                       " for round #" + round.getRoundNumber());
    }
}