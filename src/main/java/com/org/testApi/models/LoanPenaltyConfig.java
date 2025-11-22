package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Configuration des pénalités par type de prêt.
 */
@Entity
@Table(name = "loan_penalty_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanPenaltyConfig extends BaseEntity {
    
    /**
     * Type de prêt associé
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id", nullable = false)
    @ToString.Exclude
    private LoanType loanType;
    
    /**
     * Nombre de jours après l'échéance avant d'appliquer les pénalités
     */
    private Integer penaltyDelayDays;
    
    /**
     * Taux de pénalité journalier
     */
    @Column(precision = 10, scale = 4)
    private BigDecimal dailyPenaltyRate;
    
    /**
     * Taux de pénalité maximum (plafond)
     */
    @Column(precision = 10, scale = 4)
    private BigDecimal maxPenaltyRate;
    
    /**
     * Pénalité progressive (true) ou fixe (false)
     */
    private Boolean progressivePenalty;
    
    /**
     * Intervalle de calcul des pénalités en jours
     */
    private Integer calculationIntervalDays;
    
    /**
     * Message de relance automatique
     */
    @Column(columnDefinition = "TEXT")
    private String reminderMessage;
}