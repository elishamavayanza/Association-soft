package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Demande de prêt faite par un membre.
 */
@Entity
@Table(name = "loan_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanApplication extends BaseEntity {
    
    /**
     * Membre demandeur
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;
    
    /**
     * Type de prêt demandé
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id", nullable = false)
    @ToString.Exclude
    private LoanType loanType;
    
    /**
     * Montant demandé
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal requestedAmount;
    
    /**
     * Montant approuvé
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal approvedAmount;
    
    /**
     * Date de la demande
     */
    private LocalDate applicationDate;
    
    /**
     * Date de traitement/approbation
     */
    private LocalDate processedDate;
    
    /**
     * Justification de la demande
     */
    @Column(columnDefinition = "TEXT")
    private String purpose;
    
    /**
     * Statut de la demande
     */
    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus status;
    
    /**
     * Commentaires sur la décision
     */
    @Column(columnDefinition = "TEXT")
    private String decisionComments;
    
    /**
     * Prêt généré à partir de cette demande (si approuvée)
     */
    @OneToOne(mappedBy = "loanApplication", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Loan loan;
    
    /**
     * Enumération des statuts possibles pour une demande de prêt
     */
    public enum LoanApplicationStatus {
        PENDING,      // En attente
        APPROVED,     // Approuvée
        REJECTED,     // Rejetée
        CANCELLED     // Annulée
    }
}