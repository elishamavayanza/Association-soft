package com.org.testApi.services;

import com.org.testApi.models.MembershipFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class MembershipFeeNotificationService implements Observer<MembershipFee> {

    @Autowired
    private MembershipFeeService membershipFeeService;

    @Autowired
    private NotificationService notificationService;

    @PostConstruct
    public void init() {
        membershipFeeService.addObserver(this);
    }

    @Override
    public void update(String event, MembershipFee membershipFee) {
        // Only send notification for new membership fees
        if ("SAVE".equals(event) && membershipFee.getId() != null) {
            sendMembershipFeeNotification(membershipFee);
        }
    }

    private void sendMembershipFeeNotification(MembershipFee membershipFee) {
        if (membershipFee.getMember() != null && 
            membershipFee.getMember().getUser() != null && 
            membershipFee.getMember().getUser().getPhoneNumber() != null &&
            !membershipFee.getMember().getUser().getPhoneNumber().isEmpty()) {
            
            String message = buildNotificationMessage(membershipFee);
            notificationService.sendSmsNotification(
                membershipFee.getMember().getUser().getPhoneNumber(),
                message
            );
        }
    }

    private String buildNotificationMessage(MembershipFee membershipFee) {
        StringBuilder message = new StringBuilder();
        message.append("Cher(e) ")
               .append(membershipFee.getMember().getFirstName())
               .append(" ")
               .append(membershipFee.getMember().getLastName())
               .append(",\n\n");
               
        message.append("Nous vous confirmons que votre cotisation de ")
               .append(membershipFee.getAmount())
               .append(" ")
               .append(membershipFee.getCurrency())
               .append(" a été enregistrée avec succès le ")
               .append(membershipFee.getPaymentDate())
               .append(".");
               
        if (membershipFee.getFeeType() != null) {
            message.append("\nType de cotisation: ")
                   .append(membershipFee.getFeeType().getFrenchLabel());
        }
        
        message.append("\n\nMerci pour votre contribution.");
        
        return message.toString();
    }
}