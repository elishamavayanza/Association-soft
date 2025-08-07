package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;


/**
 * Représente une catégorie de transaction financière.
 * <p>
 * Une catégorie financière permet de classer les transactions (par exemple : "Cotisation", "Dépense administrative", etc.)
 * en fonction de leur nature (revenu ou dépense).
 * Chaque catégorie est liée à une {@link Association}.
 * </p>
 */
@Entity
@Table(name = "financial_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FinancialCategory extends BaseEntity {

    /**
     * Nom de la catégorie financière.
     * Ce champ est obligatoire et limité à 50 caractères.
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * Description optionnelle de la catégorie.
     * Permet de mieux contextualiser son utilisation.
     * Limité à 200 caractères.
     */
    @Column(length = 200)
    private String description;

    /**
     * Type de transaction associé à cette catégorie :
     * soit REVENUE (revenu), soit EXPENSE (dépense).
     */
    @Enumerated(EnumType.STRING)
    private FinancialTransaction.TransactionType type;

    /**
     * Association à laquelle appartient cette catégorie.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;
}