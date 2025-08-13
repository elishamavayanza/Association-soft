package com.org.testApi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private List<Observer<String>> observers = new ArrayList<>();

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            // Notifier les observateurs qu'un email a été envoyé
            notifyObservers("EMAIL_SENT", to);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email to: " + to, e);
        }
    }

    @Override
    public void sendWelcomeEmail(String userEmail) {
        String subject = "Bienvenue dans notre application!";
        String body = "Bonjour,\n\nMerci de vous être inscrit à notre application. "
                + "Nous sommes ravis de vous compter parmi nos utilisateurs.\n\n"
                + "Cordialement,\nL'équipe de l'application";

        sendEmail(userEmail, subject, body);
        notifyObservers("WELCOME_EMAIL_SENT", userEmail);
    }

    @Override
    public void sendPasswordResetEmail(String userEmail) {
        String subject = "Réinitialisation de votre mot de passe";
        String body = "Bonjour,\n\nVous avez demandé la réinitialisation de votre mot de passe. "
                + "Veuillez cliquer sur le lien suivant pour définir un nouveau mot de passe.\n\n"
                + "Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet e-mail.\n\n"
                + "Cordialement,\nL'équipe de l'application";

        sendEmail(userEmail, subject, body);
        notifyObservers("PASSWORD_RESET_EMAIL_SENT", userEmail);
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
