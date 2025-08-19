package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Représente un prêt accordé à un membre.
 * <p>
 * Un prêt a un montant, un taux d'intérêt, une date d'échéance et un statut.
 * Si la date d'échéance est dépassée, des intérêts supplémentaires s'accumulent quotidiennement.
 * </p>
 */
@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Loan extends BaseEntity {

    /**
     * Membre ayant contracté le prêt.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    /**
     * Montant initial du prêt.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * Taux d'intérêt annuel en pourcentage (exemple: 5 pour 5%).
     */
    @Column(nullable = false)
    private BigDecimal interestRate;

    /**
     * Taux de pénalité journalier en pourcentage appliqué après la date d'échéance.
     */
    @Column(nullable = false)
    private BigDecimal penaltyRate;

    /**
     * Date à laquelle le prêt doit être remboursé.
     */
    @Column(nullable = false)
    private LocalDate dueDate;

    /**
     * Date effective du remboursement.
     */
    private LocalDate repaymentDate;

    /**
     * Montant total remboursé.
     */
    private BigDecimal amountRepaid;

    /**
     * Statut du prêt.
     */
    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;

    /**
     * Enumération des statuts possibles pour un prêt.
     */
    public enum LoanStatus {
        ACTIVE,   // Prêt actif
        REPAID,   // Prêt remboursé
        OVERDUE   // Prêt en retard
    }

    /**
     * Calcule le montant total dû, y compris les intérêts.
     *
     * @return le montant total dû
     */
    public BigDecimal getTotalAmountDue() {
        if (status == LoanStatus.REPAID) {
            return amountRepaid;
        }

        BigDecimal totalAmount = amount;
        LocalDate calculationDate = LocalDate.now();

        // Si la date d'échéance est dépassée, calculer les pénalités
        if (calculationDate.isAfter(dueDate)) {
            long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, calculationDate);

            // Calculer les intérêts de pénalité (intérêts simples quotidiens)
            BigDecimal dailyPenaltyRate = penaltyRate.divide(BigDecimal.valueOf(36500), 10, java.math.RoundingMode.HALF_UP);
            BigDecimal penaltyAmount = amount.multiply(dailyPenaltyRate).multiply(BigDecimal.valueOf(overdueDays));

            totalAmount = totalAmount.add(penaltyAmount);
        }

        return totalAmount;
    }

    /**
     * Vérifie si le prêt est en retard.
     *
     * @return true si le prêt est en retard, false sinon
     */
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate) && status != LoanStatus.REPAID;
    }

    /**
     * Vérifie si le membre associé à ce prêt a des prêts en retard.
     *
     * @return true si le membre a des prêts en retard, false sinon
     */
    public boolean hasOverdueLoans() {
        if (member != null && member.getLoans() != null) {
            return member.getLoans().stream()
                    .anyMatch(loan -> loan.isOverdue());
        }
        return false;
    }

    /**
     * Met à jour le statut du prêt.
     */
    @PrePersist
    @PreUpdate
    protected void updateStatus() {
        if (status == LoanStatus.REPAID) {
            return; // Ne rien faire si déjà remboursé
        }

        if (isOverdue()) {
            status = LoanStatus.OVERDUE;
        } else {
            status = LoanStatus.ACTIVE;
        }
    }
}
