package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Représente un prêt effectué par un membre.
 * <p>
 * Un prêt est associé à un {@link Member} et peut éventuellement être lié
 * à un{@link Document}. Il contient desinformations sur le montant,
 * les taux d'intérêt et de pénalité, les dates importantes, ainsi que
 * le statut du prêt.
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Loan extends BaseEntity {

/**
     * Membre ayant effectué le prêt.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    /**
     * Document ou objet prêté.
    * Ce lien est optionnel.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = true)
    @ToString.Exclude
    private Document document;

    /**
     * Montant du prêt.
     */
    private BigDecimal amount;

    /**
     * Taux d'intérêt appliqué au prêt.
     */
    private BigDecimal interestRate;

    /**
     * Taux de pénalité appliqué en cas de retard.
     */
    private BigDecimal penaltyRate;

    /**
     * Date de début du prêt.
     */
    private LocalDate loanDate;

    /**
     * Date de retourprévue.
     */
    private LocalDate dueDate;

    /**
     * Date de retour effective.
     * Reste null tant que le prêt n'est pas terminé.
     */
    private LocalDate returnDate;

    /**
     * Montant remboursé.
     */
    private BigDecimal amountRepaid;

    /**
* Date de remboursement.
     */
    private LocalDate repaymentDate;

    /**
     * Statut du prêt.
     * Par défaut, un prêt est actif.
     */
    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;

    /**
     * Montantde caution éventuel.
     * Peut être null si aucun dépôt n'est requis.
     */
    private BigDecimal depositAmount;

    /**
     * Indique si la caution a été remboursée.
     * Utilisé uniquement si un dépôt a été effectué.
     */
    private Boolean depositRefunded;

    /**
     * Notes supplémentaires concernant le prêt.
     * Optionnel.
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Calcule lemontant total dû pour ce prêt.
     *
     * @return le montant total dû*/
    public BigDecimal getTotalAmountDue() {
        if (amount == null) {
            return BigDecimal.ZERO;
        }

        // Calculer les intérêts
        BigDecimal interest = amount.multiply(interestRate != null ? interestRate : BigDecimal.ZERO);

        // Si le prêt est en retard, ajouterles pénalités
        BigDecimal penalty = BigDecimal.ZERO;
        if (status == LoanStatus.OVERDUE ||
                (status == LoanStatus.ACTIVE && dueDate != null && dueDate.isBefore(LocalDate.now()))) {
            penalty = amount.multiply(penaltyRate != null ? penaltyRate : BigDecimal.ZERO);
}

        return amount.add(interest).add(penalty);
    }

    /**
     * Indique si le prêt est en retard.
     *
     * @return true si la date deretour prévue est dépassée et que le prêt n'est pas encore terminé, false sinon
     */
   public boolean isOverdue() {
        return status == LoanStatus.OVERDUE ||
                (status == LoanStatus.ACTIVE && dueDate != null && dueDate.isBefore(LocalDate.now()));
    }

    /**
     * Vérifie si le membre a des prêts en retard.
     *
     * @return true si le membre a des prêts en retard, false sinon
     */
    public boolean hasOverdueLoans() {
        if (member != null) {
            return member.getLoans().stream()
                    .anyMatch(loan -> loan.getStatus() == LoanStatus.OVERDUE ||
                           (loan.getStatus() == LoanStatus.ACTIVE &&
                                    loan.getDueDate() != null &&
                                    loan.getDueDate().isBefore(LocalDate.now())));
        }
        return false;
    }

    /**
     * Met à jour le statut du prêt.
     */
    @PrePersist
    /*@PreUpdate*/
   protected void updateStatus() {
        // Ne pas modifier le statut si le prêt est déjà marqué comme remboursé
        if (status == LoanStatus.REPAID) {
            // Vérifier si le montant remboursé correspond au montant total dû
            BigDecimal totalAmountDue= getTotalAmountDue();
            if (amountRepaid != null && amountRepaid.compareTo(totalAmountDue) >= 0) {
                return; // Le prêt est correctement marqué comme remboursé
            }
            // Si le montant remboursé est inférieur au montant dû,réinitialiser le statut
        }

        // Si le montant remboursé est supérieur ou égal au montant total dû, marquer comme remboursé
        BigDecimal totalAmountDue = getTotalAmountDue();
        if (amountRepaid != null && totalAmountDue != null && amountRepaid.compareTo(totalAmountDue) >= 0) {
            status = LoanStatus.REPAID;
            return;
        }

        // Sinon, vérifier si le prêt est en retard
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            status = LoanStatus.OVERDUE;
        } else {
            status = LoanStatus.ACTIVE;
        }
    }

    /**
     * Enumération des différents statuts possibles pour un prêt.
     */
    public enum LoanStatus {
        ACTIVE,  // Prêt en cours
        OVERDUE,  // Prêt en retard
        REPAID    // Prêt remboursé}
    }
}