package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Gestion des périodes de grâce pour les prêts.
 */
@Entity
@Table(name = "grace_periods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GracePeriod extends BaseEntity {
    
    /**
     * Prêt concerné par la période de grâce
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    @ToString.Exclude
    private Loan loan;
    
    /**
     * Type de prêt associé
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id")
    @ToString.Exclude
    private LoanType loanType;
    
    /**
     * Date de début de la période de grâce
     */
    private LocalDate startDate;
    
    /**
     * Date de fin de la période de grâce
     */
    private LocalDate endDate;
    
    /**
     * Nombre de jours de grâce
     */
    private Integer days;
    
    /**
     * Raison de l'octroi de la période de grâce
     */
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    /**
     * Approuvé par (ID de l'utilisateur)
     */
    private Long approvedBy;
    
    /**
     * Statut de la période de grâce
     */
    @Enumerated(EnumType.STRING)
    private GracePeriodStatus status;
    
    /**
     * Commentaires sur la période de grâce
     */
    @Column(columnDefinition = "TEXT")
    private String comments;
    
    /**
     * Indique si la période de grâce a été utilisée
     */
    private Boolean used;
    
    /**
     * Enumération des statuts possibles pour une période de grâce
     */
    public enum GracePeriodStatus {
        PENDING,      // En attente d'approbation
        APPROVED,     // Approuvée
        REJECTED,     // Rejetée
        ACTIVE,       // Active
        EXPIRED,      // Expirée
        USED          // Utilisée
    }
}