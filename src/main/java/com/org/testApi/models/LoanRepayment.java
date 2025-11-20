package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Suivi des remboursements de prêt.
 */
@Entity
@Table(name = "loan_repayments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanRepayment extends BaseEntity {
    
    /**
     * Prêt associé
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    @ToString.Exclude
    private Loan loan;
    
    /**
     * Montant du remboursement
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal amount;
    
    /**
     * Date du remboursement
     */
    private LocalDate repaymentDate;
    
    /**
     * Solde restant après ce remboursement
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal remainingBalance;
    
    /**
     * Montant des intérêts inclus dans ce remboursement
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal interestAmount;
    
    /**
     * Montant des pénalités inclus dans ce remboursement
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal penaltyAmount;
    
    /**
     * Type de remboursement
     */
    @Enumerated(EnumType.STRING)
    private RepaymentType type;
    
    /**
     * Référence de transaction (numéro de reçu, etc.)
     */
    private String transactionReference;
    
    /**
     * Commentaires sur le remboursement
     */
    @Column(columnDefinition = "TEXT")
    private String comments;
    
    /**
     * Enumération des types de remboursement
     */
    public enum RepaymentType {
        REGULAR,      // Remboursement régulier
        PARTIAL,      // Remboursement partiel
        FULL,         // Remboursement complet
        EARLY         // Remboursement anticipé
    }
}