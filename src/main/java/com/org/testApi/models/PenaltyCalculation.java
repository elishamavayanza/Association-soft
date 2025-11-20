package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Calcul des pénalités sur les prêts en retard.
 */
@Entity
@Table(name = "penalty_calculations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PenaltyCalculation extends BaseEntity {
    
    /**
     * Prêt concerné par le calcul de pénalité
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    @ToString.Exclude
    private Loan loan;
    
    /**
     * Date de calcul de la pénalité
     */
    private LocalDate calculationDate;
    
    /**
     * Nombre de jours de retard
     */
    private Integer daysOverdue;
    
    /**
     * Montant de base sur lequel la pénalité est calculée
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal baseAmount;
    
    /**
     * Taux de pénalité appliqué
     */
    @Column(precision = 10, scale = 4)
    private BigDecimal penaltyRate;
    
    /**
     * Montant de pénalité calculé
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal penaltyAmount;
    
    /**
     * Montant cumulé des pénalités précédentes
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal accumulatedPenalties;
    
    /**
     * Total des pénalités après ce calcul
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal totalPenalties;
    
    /**
     * Indique si cette pénalité a été appliquée au prêt
     */
    private Boolean applied;
    
    /**
     * Référence au configuration de pénalité utilisée
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "penalty_config_id")
    @ToString.Exclude
    private LoanPenaltyConfig penaltyConfig;
}