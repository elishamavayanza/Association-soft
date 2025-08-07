package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Représente une transaction financière liée à une association.
 * <p>
 * Une transaction peut être un revenu ou une dépense, associée à une activité,
 * un projet, une catégorie financière, ou simplement à l'association.
 * </p>
 */
@Entity
@Table(name = "financial_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FinancialTransaction extends BaseEntity {

    /**
     * Montant de la transaction.
     * Ce champ est obligatoire.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * Date à laquelle la transaction a eu lieu.
     * Si non fournie, la date du jour est utilisée automatiquement.
     */
    @Column(nullable = false)
    private LocalDate transactionDate;

    /**
     * Description optionnelle pour détailler la transaction.
     * Limité à 100 caractères.
     */
    @Column(length = 100)
    private String description;

    /**
     * Type de la transaction : INCOME (revenu) ou EXPENSE (dépense).
     */
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * Association liée à la transaction.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;

    /**
     * Activité associée à cette transaction (optionnel).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    @ToString.Exclude
    private Activity activity;

    /**
     * Projet associé à cette transaction (optionnel).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    private Project project;

    /**
     * Catégorie financière de la transaction (optionnelle).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private FinancialCategory category;

    /**
     * Méthode appelée avant la persistance pour initialiser la date de transaction
     * si elle n’a pas été spécifiée.
     */
    @PrePersist
    protected void onCreate() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDate.now();
        }
    }

    /**
     * Enumération des types de transactions possibles.
     */
    public enum TransactionType {
        INCOME, // Revenu
        EXPENSE // Dépense
    }
}