package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Représente l'historique des rôles occupés par un membre au sein de l'association.
 * <p>
 * Chaque instance correspond à un rôle attribué à un membre pendant une période donnée.
 * Permet de tracer les évolutions des responsabilités du membre.
 * </p>
 */
@Entity
@Table(name = "member_role_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberRoleHistory extends BaseEntity {

    /**
     * Membre auquel ce rôle est associé.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    /**
     * Nom du rôle occupé (ex : "Président", "Trésorier", "Secrétaire").
     * Limité à 50 caractères.
     */
    @Column(length = 50)
    private String role;

    /**
     * Date de début de la prise de fonction pour ce rôle.
     */
    private LocalDate startDate;

    /**
     * Date de fin de la prise de fonction pour ce rôle.
     * Peut être nulle si le membre occupe encore ce rôle.
     */
    private LocalDate endDate;

    /**
     * Indique si ce rôle confère des droits d'administrateur.
     */
    @Column(columnDefinition = "boolean default false")
    private boolean isAdmin;
}