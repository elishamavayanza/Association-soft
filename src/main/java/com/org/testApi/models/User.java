package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    /**
     * Adresse email unique, validée comme adresse correcte.
     * Limité à 100 caractères.
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être une adresse valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    /**
     * Mot de passe hashé de l'utilisateur.
     */
    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    /**
     * Ensemble des rôles attribués à l'utilisateur.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    /**
     * Liste des activités créées par cet utilisateur.
     */
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Activity> createdEvents = new ArrayList<>();

    /**
     * Liste des activités auxquelles cet utilisateur participe.
     */
    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Activity> attendedEvents = new ArrayList<>();

    /**
     * Prénom de l'utilisateur.
     */
    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
    private String firstName;

    /**
     * Nom de famille de l'utilisateur.
     */
    @Size(max = 50, message = "Le nom de famille ne doit pas dépasser 50 caractères")
    private String lastName;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères")
    private String phoneNumber;

    /**
     * Date et heure de la dernière connexion.
     */
    private LocalDateTime lastLogin;

    /**
     * Nombre de tentatives de connexion échouées.
     */
    private int loginAttempts = 0;

    /**
     * Chemin vers la photo de profil dans le système de fichiers.
     * Ex: "/uploads/profiles/user123.jpg"
     */
    private String profilePhotoPath;

    /**
     * Type MIME de la photo de profil.
     * Ex: "image/jpeg", "image/png"
     */
    private String profilePhotoMimeType;

    /**
     * Taille de la photo de profil en octets.
     */
    private Long profilePhotoSize;

    /**
     * Token de l'appareil pour les notifications push.
     */
    private String deviceToken;

}