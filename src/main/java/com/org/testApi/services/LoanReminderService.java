package com.org.testApi.services;

import com.org.testApi.models.Loan;
import com.org.testApi.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanReminderService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Scheduled task to send reminders for loans due in one week
     * Runs every day at 10:00 AM
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendOneWeekLoanReminders() {
        LocalDate oneWeekFromNow = LocalDate.now().plusDays(7);
        List<Loan> loansDueInOneWeek = loanRepository.findLoansDueOn(oneWeekFromNow);
        
        for (Loan loan : loansDueInOneWeek) {
            sendLoanReminder(loan, "une semaine");
        }
    }

    /**
     * Scheduled task to send reminders for loans due in two days
     * Runs every day at 10:30 AM
     */
    @Scheduled(cron = "0 30 10 * * ?")
    public void sendTwoDaysLoanReminders() {
        LocalDate twoDaysFromNow = LocalDate.now().plusDays(2);
        List<Loan> loansDueInTwoDays = loanRepository.findLoansDueOn(twoDaysFromNow);
        
        for (Loan loan : loansDueInTwoDays) {
            sendLoanReminder(loan, "deux jours");
        }
    }

    /**
     * Sends a loan reminder to the member
     * @param loan The loan for which to send the reminder
     * @param timePeriod The time period before due date (e.g., "une semaine", "deux jours")
     */
    private void sendLoanReminder(Loan loan, String timePeriod) {
        try {
            if (loan.getMember() != null && 
                loan.getMember().getPhone() != null && 
                !loan.getMember().getPhone().isEmpty()) {
                
                String message = String.format(
                    "Bonjour %s %s, votre prêt #%d de %s %s doit être remboursé dans %s, le %s. " +
                    "Montant total dû: %s %s. Merci de procéder au remboursement avant la date d'échéance.",
                    loan.getMember().getFirstName(),
                    loan.getMember().getLastName(),
                    loan.getId(),
                    loan.getAmount(),
                    loan.getCurrency(),
                    timePeriod,
                    loan.getDueDate().toString(),
                    loan.getTotalAmountDue(),
                    loan.getCurrency()
                );
                
                notificationService.sendSmsNotification(loan.getMember().getPhone(), message);
            }
        } catch (Exception e) {
            System.err.println("Failed to send loan reminder for loan " + loan.getId() + ": " + e.getMessage());
        }
    }
}