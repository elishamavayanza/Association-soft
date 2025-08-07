package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Représente un utilisateur du système.
 * <p>
 * Un utilisateur possède un nom d'utilisateur unique, une adresse email,
 * un mot de passe, ainsi que des rôles d'accès.
 * Il peut créer et participer à des activités.
 * </p>
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseEntity {

    /**
     * Nom d'utilisateur unique.
     */
    @Column(nullable = false, length = 50)
    private String username;

    /**
     * Adresse email unique, validée comme adresse correcte.
     * Limité à 100 caractères.
     */
    @Column(nullable = false, length = 100)
    @Email(message = "L'email doit être une adresse valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    /**
     * Mot de passe hashé de l'utilisateur.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Ensemble des rôles attribués à l'utilisateur.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    /**
     * Liste des activités créées par cet utilisateur.
     */
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<Activity> createdEvents = new ArrayList<>();

    /**
     * Liste des activités auxquelles cet utilisateur participe.
     */
    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    @ToString.Exclude
    private List<Activity> attendedEvents = new ArrayList<>();

    /**
     * Prénom de l'utilisateur.
     */
    private String firstName;

    /**
     * Nom de famille de l'utilisateur.
     */
    private String lastName;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    private String phoneNumber;

    /**
     * Date et heure de la dernière connexion.
     */
    private LocalDateTime lastLogin;

    /**
     * Nombre de tentatives de connexion échouées.
     */
    private int loginAttempts = 0;

}