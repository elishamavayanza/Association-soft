package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * Représente un rôle utilisateur dans le système.
 * <p>
 * Chaque rôle définit un niveau d'accès ou un type d'utilisateur,
 * tel qu'administrateur, modérateur, membre ou invité.
 * </p>
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /**
     * Identifiant unique du rôle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /**
     * Nom du rôle, défini par l'énumération {@link ERole}.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    /**
     * Description optionnelle du rôle.
     * Limité à 50 caractères.
     */
    @Size(max = 50)
    private String description;


    /**
     * Enumération des différents rôles possibles dans l'application.
     */
    public enum ERole {
        ROLE_ADMIN,      // Administrateur avec tous les droits
        ROLE_MODERATOR,  // Modérateur avec droits spécifiques
        ROLE_MEMBER,     // Membre standard
        ROLE_GUEST       // Invité avec accès limité
    }

}