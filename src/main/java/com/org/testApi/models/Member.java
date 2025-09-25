package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un membre d'une association.
 * <p>
 * Un membre est lié à un {@link User} et une {@link Association}.
 * Il peut avoir différents types (régulier, honoraire, bénévole, etc.)
 * et possède une historique des rôles ainsi que des cotisations.
 * </p>
 */
@Entity
@Table(name = "members")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Member extends BaseEntity {

    /**
     * Code unique du membre au sein de l'association.
     * Ce code est généré automatiquement à la création du membre.
     */
    @Column(name = "member_code", unique = true)
    private String memberCode;

    /**
     * Utilisateur associé au membre.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private User user;

    /**
     * Association à laquelle appartient ce membre.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Association association;

    /**
     * Date d'adhésion du membre à l'association.
     * Si non renseignée, sera initialisée à la date courante lors de la création.
     */
    private LocalDate joinDate;

    /**
     * Date de départ ou de désinscription du membre.
     * Permet de savoir si un membre est actif ou non.
     */
    private LocalDate leaveDate;

    /**
     * Type du membre (régulier, honoraire, bénévole, etc.).
     * Par défaut, un membre est de type REGULAR.
     */
    @Enumerated(EnumType.STRING)
    private MemberType type = MemberType.REGULAR;

    /**
     * Liste des cotisations (fees) associées à ce membre.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<MembershipFee> fees = new ArrayList<>();

    /**
     * Historique des rôles que ce membre a eus au sein de l'association.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<MemberRoleHistory> roleHistory = new ArrayList<>();


    /**
     * Liste des prêts associés à ce membre.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Loan> loans = new ArrayList<>();

    /**
     * Indique si ce membre a des droits d'administrateur.
     */
    @Column(columnDefinition = "boolean default false")
    private boolean isAdmin;


    /**
     * Initialise la date d'adhésion si elle n'a pas été définie avant la persistance.
     */
    @PrePersist
    protected void onCreate() {
        if (this.joinDate == null) {
            this.joinDate = LocalDate.now();
        }
        
        // Générer un code unique si ce n'est pas déjà fait
        if (this.memberCode == null) {
            this.memberCode = generateMemberCode();
        }
    }

    /**
     * Génère un code unique pour le membre.
     * Format: MBR-XXX-YYYYY (où XXX est l'ID de l'association et YYYYY est l'ID du membre)
     * 
     * @return Le code unique généré
     */
    private String generateMemberCode() {
        // À implémenter correctement dans le service
        // Cette implémentation basique sera remplacée par une plus robuste
        return "MBR-" + System.currentTimeMillis();
    }

    /**
     * Indique si le membre est actuellement actif (pas de date de départ définie).
     *
     * @return true si le membre est actif, false sinon.
     */
    public boolean isActive() {
        return leaveDate == null;
    }

    /**
     * Vérifie si le membre est éligible pour emprunter.
     * Un membre est éligible s'il:
     * 1. Est actif
     * 2. A payé au moins une cotisation
     * 3. N'a pas de prêts en retard
     *
     * @return true si le membre est éligible, false sinon
     */
    public boolean isEligibleForLoan() {
        // Vérifier si le membre est actif
        if (!isActive()) {
            return false;
        }

        // Vérifier si le membre a payé au moins une cotisation
        if (fees == null || fees.isEmpty()) {
            return false;
        }

        // Vérifier si le membre a des prêts en retard
        if (loans != null) {
            boolean hasOverdueLoans = loans.stream()
                    .anyMatch(loan -> loan.isOverdue());
            if (hasOverdueLoans) {
                return false;
            }
        }

        // Si toutes les conditions sont remplies, le membre est éligible
        return true;
    }


    /**
     * Enumération des différents types de membres possibles.
     */
    public enum MemberType {
        REGULAR,       // Membre régulier
        HONORARY,      // Membre honoraire
        BENEFACTOR,    // Bienfaiteur
        VOLUNTEER,     // Bénévole
        BOARD_MEMBER   // Membre du conseil d'administration
    }
}