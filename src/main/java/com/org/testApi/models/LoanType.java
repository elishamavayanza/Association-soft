package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Représente un type de prêt avec ses configurations spécifiques.
 */
@Entity
@Table(name = "loan_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanType extends BaseEntity {
    
    /**
     * Nom du type de prêt
     */
    @Column(nullable = false, unique = true)
    private String name;
    
    /**
     * Description du type de prêt
     */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /**
     * Taux d'intérêt mensuel
     */
    @Column(precision = 10, scale = 4)
    private BigDecimal monthlyInterestRate;
    
    /**
     * Taux de pénalité maximum
     */
    @Column(precision = 10, scale = 4)
    private BigDecimal maxPenaltyRate;
    
    /**
     * Période de grâce en jours
     */
    private Integer gracePeriodDays;
    
    /**
     * Catégorie du prêt (par destination des fonds, source de financement, durée associative)
     */
    @Column(nullable = false)
    private String category;
    
    /**
     * Sous-catégorie spécifique
     */
    private String subCategory;
    
    /**
     * Indique si ce type de prêt est actif
     */
    @Builder.Default
    private Boolean active = true;
}