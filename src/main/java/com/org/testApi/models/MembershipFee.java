package com.org.testApi.models;

import com.org.testApi.models.Member;
import com.org.testApi.models.FinancialTransaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Représente une cotisation payée par un membre d'une association.
 * <p>
 * Chaque cotisation est associée à un membre et peut être liée à une transaction financière.
 * Elle contient les informations sur le montant, la date de paiement, la période couverte,
 * le mode de paiement, et une référence éventuelle.
 * </p>
 */
@Entity
@Table(name = "membership_fees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MembershipFee extends BaseEntity {

    /**
     * Membre ayant effectué la cotisation.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    /**
     * Montant payé pour la cotisation.
     * Ce champ est obligatoire.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * Date à laquelle le paiement a été effectué.
     * Ce champ est obligatoire.
     */
    @Column(nullable = false)
    private LocalDate paymentDate;

    /**
     * Date de début de la période couverte par cette cotisation (optionnelle).
     */
    private LocalDate startDate;

    /**
     * Date de fin de la période couverte par cette cotisation (optionnelle).
     */
    private LocalDate endDate;

    /**
     * Transaction financière associée à ce paiement (optionnelle).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    @ToString.Exclude
    private FinancialTransaction transaction;

    /**
     * Mode de paiement utilisé pour cette cotisation.
     */
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    /**
     * Référence associée au paiement (exemple : numéro de chèque, référence bancaire).
     */
    @Column(length = 50)
    private String reference;

    /**
     * Enumération des modes de paiement possibles.
     */
    public enum PaymentMethod {
        CASH,           // Paiement en espèces
        CHECK,          // Paiement par chèque
        BANK_TRANSFER,  // Virement bancaire
        CREDIT_CARD,    // Carte bancaire
        OTHER           // Autre mode de paiement
    }
}