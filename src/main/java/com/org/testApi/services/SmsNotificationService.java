package com.org.testApi.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService {

    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.phone.number:}")
    private String fromPhoneNumber;

    private boolean initialized = false;

    private void initializeTwilio() {
        if (!initialized && accountSid != null && !accountSid.isEmpty()
                && authToken != null && !authToken.isEmpty()) {
            Twilio.init(accountSid, authToken);
            initialized = true;
        }
    }

    public void sendSms(String toPhoneNumber, String message) {
        try {
            initializeTwilio();

            if (!initialized) {
                System.out.println("Twilio not configured. SMS not sent: " + message);
                return;
            }

            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();

            System.out.println("SMS sent successfully to " + toPhoneNumber);
        } catch (Exception e) {
            System.err.println("Failed to send SMS to " + toPhoneNumber + ": " + e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }
}
