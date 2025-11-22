package com.org.testApi.dto.reports;

import com.org.testApi.dto.MemberDTO;
import com.org.testApi.dto.ContributionDTO;
import com.org.testApi.dto.PenaltyDTO;
import com.org.testApi.models.ContributionStatus;
import com.org.testApi.models.PenaltyType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class MemberParticipationTrackingDTO {
    private MemberDTO member;
    
    // Historique de participation
    private List<ContributionDetail> contributionHistory;
    
    // Contributions manquées
    private List<MissingContribution> missingContributions;
    
    // Paiements en retard
    private List<LatePayment> latePayments;
    
    // Statistiques globales
    private ParticipationStatistics statistics;

    @Data
    public static class ContributionDetail {
        private Long roundId;
        private Integer roundNumber;
        private BigDecimal amount;
        private LocalDate contributionDate;
        private ContributionStatus status;
    }

    @Data
    public static class MissingContribution {
        private Long roundId;
        private Integer roundNumber;
        private BigDecimal expectedAmount;
        private LocalDate dueDate;
        private LocalDate actualDate;
        private Long daysLate;
    }

    @Data
    public static class LatePayment {
        private Long roundId;
        private Integer roundNumber;
        private BigDecimal amount;
        private BigDecimal penaltyAmount;
        private LocalDate dueDate;
        private LocalDate paidDate;
        private Long daysLate;
        private PenaltyType penaltyType;
    }

    @Data
    public static class ParticipationStatistics {
        private Long totalExpectedContributions;
        private Long totalActualContributions;
        private Long totalMissedContributions;
        private Long totalLateContributions;
        private BigDecimal totalContributedAmount;
        private BigDecimal totalPenaltyAmount;
        private Double participationRate; // Pourcentage de participation
        private Double punctualityRate; // Pourcentage de paiements à temps
    }
}