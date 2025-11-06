package com.org.testApi.services;

import com.org.testApi.models.Contribution;
import com.org.testApi.models.Penalty;
import com.org.testApi.models.Round;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.repository.RoundRepository;
import com.org.testApi.repository.ContributionRepository;
import com.org.testApi.repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationSchedulerService {

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Scheduled task to send reminders for upcoming rounds
     * Runs every day at 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendUpcomingRoundReminders() {
        // Find rounds that are starting in the next 3 days
        LocalDate today = LocalDate.now();
        LocalDate threeDaysFromNow = today.plusDays(3);

        List<Round> upcomingRounds = roundRepository.findByStartDateBetweenAndStatus(
            today, threeDaysFromNow, com.org.testApi.models.RoundStatus.UPCOMING);

        for (Round round : upcomingRounds) {
            notificationService.sendUpcomingRoundReminder(round);
        }
    }

    /**
     * Scheduled task to send payment due notifications
     * Runs every day at 10:00 AM
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendPaymentDueNotifications() {
        // Find contributions that are due in the next 2 days
        LocalDate today = LocalDate.now();
        LocalDate twoDaysFromNow = today.plusDays(2);

        List<Contribution> dueContributions = contributionRepository.findByContributionDateBetweenAndStatus(
            today, twoDaysFromNow, com.org.testApi.models.ContributionStatus.PENDING);

        for (Contribution contribution : dueContributions) {
            notificationService.sendPaymentDueNotification(contribution);
        }
    }

    /**
     * Scheduled task to send distribution announcements
     * Runs every day at 11:00 AM
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void sendDistributionAnnouncements() {
        // Find rounds that had distributions in the last day
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Round> distributedRounds = roundRepository.findByDistributionDateAfter(
            yesterday.atStartOfDay());

        for (Round round : distributedRounds) {
            if (!round.getBeneficiaries().isEmpty() && round.getTotalAmountDistributed() != null) {
                notificationService.sendDistributionAnnouncement(round);
            }
        }
    }

    /**
     * Scheduled task to send penalty warnings
     * Runs every day at 12:00 PM
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendPenaltyWarnings() {
        // Find penalties created in the last day
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Penalty> recentPenalties = penaltyRepository.findByPenaltyDateAfter(yesterday);

        for (Penalty penalty : recentPenalties) {
            // Send warning for any penalty that is not PENDING (i.e., has been applied)
            // In this system, penalties that are PAID or WAIVED would have been applied
            if (penalty.getStatus() != com.org.testApi.models.PenaltyStatus.PENDING) {
                notificationService.sendPenaltyWarning(penalty);
            }
        }
    }
}