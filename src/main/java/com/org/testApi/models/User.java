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
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre3et 50 caractères")
    private String username;

    /**
     * Adresse email unique, validée comme adresse correcte.
     * Limité à 100 caractères.
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "L'emailne peutpas être vide")
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
     * Ensemble des rôlesattribués à l'utilisateur.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    @JsonIgnore
    private Set<Role>roles = new HashSet<>();

    /**
     * Liste des activités créées par cet utilisateur.
     */
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Activity> createdEvents = new ArrayList<>();

    /**
* Listedes activités auxquelles cet utilisateur participe.
     */
    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Activity> attendedEvents = new ArrayList<>();

    /**
     * Prénom de l'utilisateur.
     */
    @Size(max =50, message = "Le prénom ne doit pas dépasser 50 caractères")
    @Column(name = "first_name")
    private String firstName;

    /**
     * Nom de famille de l'utilisateur.
     */
    @Size(max = 50, message = "Le nom de famille nedoit pas dépasser50 caractères")
    @Column(name = "last_name")
    private String lastName;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères")
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Date et heurede la dernière connexion.
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * Nombre de tentatives de connexion échouées.
     */
    @Column(name= "login_attempts")
    private int loginAttempts = 0;

    /**
     * Chemin vers la photo de profil dans le système de fichiers.
     * Ex: "/uploads/profiles/user123.jpg"
     */
    @Column(name = "profile_photo_path")
    private String profilePhotoPath;

/**
     * Type MIME de la photo de profil.
* Ex: "image/jpeg", "image/png"
     */
    @Column(name = "profile_photo_mime_type")
    private String profilePhotoMimeType;

    /**
     * Taille de la photo de profil en octets.
     */
    @Column(name ="profile_photo_size")
    private Long profilePhotoSize;

    /**
     * Token de l'appareil pour les notifications push.
    */
    @Column(name = "device_token", nullable = true)
    private String deviceToken;
// Manually adding missing setter methods to fix compilation errors
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles =roles;
    }
    
    // Manually adding missing getter methods tofix compilation errors
  public String getUsername() {
        return this.username;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public Set<Role> getRoles() {
        return this.roles;
    }
}