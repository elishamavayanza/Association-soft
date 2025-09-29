package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Size;

/**
* Représente un projet géré par une association.
 * <p>
 * Un projet contientun nom, une description, desdates de début et de fin,
 * un statut, un gestionnaire, ainsi que des activités, membres associés et transactions financières.
 * </p>
 */
@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Project extends BaseEntity {

    /**
     * Nom du projet.
     * Champ obligatoire, limité à 100 caractères.
     */
    @Column(nullable= false, length = 100)
    @Size(max = 100)
   private String name;

    /**
    * Description détaillée du projet.
     * Champ optionnel, limité à 2000 caractères.
     */
    @Column(columnDefinition = "TEXT")
@Size(max = 2000)
    private String description;

    /**
     * Date dedébut prévue du projet.
*/
    private LocalDate startDate;

    /**
     * Date de fin prévue du projet.
     */
    private LocalDate endDate;

    /**
     * Association responsable du projet.
     * Celienest obligatoire.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;

    /**
     * Utilisateur désigné comme gestionnaire (manager) du projet.
     * Ce lien est optionnel.
*/
@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @ToString.Exclude
    private User manager;

/**
     * Liste des activités associées au projet.
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude@JsonIgnore
    private List<Activity> activities = new ArrayList<>();

/**
     * Liste des membres participant au projet.
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<ProjectMember> members = new ArrayList<>();

/**
    * Liste des transactions financières liées au projet.
     */
   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default@ToString.Exclude
    private List<FinancialTransaction> transactions = new ArrayList<>();

    /**
     * Statut actuel du projet.
     *Valeur pardéfaut : PLANNING.
     */
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNING;

/**
     * Enumération des différents statuts possibles pour un projet.
     */
    public enum ProjectStatus {
        PLANNING,     // En planification
       IN_PROGRESS,  // En cours
        ON_HOLD,      //En pause
        COMPLETED,    // Terminé
        CANCELLED     // Annulé
    }
}