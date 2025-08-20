package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Représente une activité organisée par une association.
 * <p>
 * Une activité peut être une conférence, un atelier, un projet, etc.
 * Elle peut être liée à une association et éventuellement à un projet spécifique.
 * Elle peut également avoir des participants et des transactions financières associées.
 * </p>
 */
@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Activity extends BaseEntity {

    /**
     * Titre de l'activité.
     * Ne peut pas dépasser 100 caractères.
     */
    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String title;

    /**
     * Description détaillée de l'activité (optionnelle).
     * Maximum 1000 caractères.
     */
    @Column(columnDefinition = "TEXT")
    @Size(max = 1000)
    private String description;

    /**
     * Type d'activité (conférence, atelier, etc.).
     */
    @Enumerated(EnumType.STRING)
    private ActivityType type; // CONFERENCE, WORKSHOP, PROJECT, etc.

    /**
     * Date et heure de début de l'activité.
     * Doit être dans le futur ou le présent.
     */
    @FutureOrPresent
    private LocalDateTime startDateTime;

    /**
     * Date et heure de fin de l'activité (optionnelle).
     */
    private LocalDateTime endDateTime;

    /**
     * Lieu où se déroule l'activité (optionnel).
     */
    @Column(length = 100)
    private String location;

    @Builder.Default
    private Boolean deleted = false;


    /**
     * Association à laquelle est liée cette activité.
     * Ce lien est obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;

    /**
     * Projet auquel cette activité est associée (optionnel).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    private Project project; // Si l'activité fait partie d'un projet

    /**
     * Utilisateur ayant créé cette activité (optionnel).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id")
    @ToString.Exclude
    private User creator;

    /**
     * Liste des participants à l'activité.
     */
    @ManyToMany
    @JoinTable(name = "activity_participants",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    @ToString.Exclude
    private List<User> participants = new ArrayList<>();

    /**
     * Liste des transactions financières associées à cette activité.
     */
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<FinancialTransaction> transactions = new ArrayList<>();

    /**
     * Valide que la date de fin est postérieure à la date de début (si les deux sont définies).
     *
     * @return {@code true} si la date de fin est valide ou non définie.
     */
    @AssertTrue
    private boolean isEndDateValid() {
        return endDateTime == null || startDateTime == null || endDateTime.isAfter(startDateTime);
    }

    /**
     * Statut actuel de l'activité (par défaut : {@code PLANNED}).
     */
    @Enumerated(EnumType.STRING)
    private ActivityStatus status = ActivityStatus.PLANNED;

    /**
     * Enumération des types possibles d'activités.
     */
    public enum ActivityType {
        CONFERENCE, WORKSHOP, MEETING, PROJECT, TRAINING, SOCIAL_EVENT, OTHER
    }

    /**
     * Enumération des statuts possibles d'une activité.
     */
    public enum ActivityStatus {
        PLANNED, ONGOING, COMPLETED, CANCELLED
    }
}