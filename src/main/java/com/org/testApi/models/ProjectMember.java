package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Représente l'affectation d'un membre à un projet.
 * <p>
 * Cette classe relie un {@link Member} à un {@link Project} avec un rôle spécifique
 * dans le projet, ainsi que les dates de début et de fin de participation.
 * </p>
 */
@Entity
@Table(name = "project_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectMember extends BaseEntity {

    /**
     * Projet auquel le membre est affecté.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @ToString.Exclude
    private Project project;

    /**
     * Membre affecté au projet.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    /**
     * Rôle occupé par le membre dans le projet (exemple : "Chef de projet", "Développeur").
     * Limité à 50 caractères.
     */
    @Column(length = 50)
    private String roleInProject;

    /**
     * Date de début de la participation du membre au projet.
     * Si non renseignée, sera initialisée à la date courante lors de la création.
     */
    private LocalDate joinDate;

    /**
     * Date de fin de la participation du membre au projet.
     * Permet de savoir si le membre est encore actif dans ce projet.
     */
    private LocalDate leaveDate;

    /**
     * Initialise la date d'adhésion (joinDate) si elle n'a pas été définie avant la persistance.
     */
    @PrePersist
    protected void onCreate() {
        if (this.joinDate == null) {
            this.joinDate = LocalDate.now();
        }
    }

    /**
     * Indique si le membre est actuellement actif dans ce projet
     * (c’est-à-dire que la date de départ n’est pas définie).
     *
     * @return true si actif, false sinon.
     */
    public boolean isActive() {
        return leaveDate == null;
    }
}