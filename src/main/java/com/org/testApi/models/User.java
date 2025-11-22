package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "photo", length = 255)
    private String photo;

    /**
     * Chemin vers la photo de profil du membre.
     * Ce champ peut être null si aucune photo n'est disponible.
     */
    @Column(name = "profile_photo_path", nullable = true)
    private String profilePhotoPath;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Member> members = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name ="user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "userParticipants", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Activity> attendedActivities = new ArrayList<>();
    
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
   public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive();
    }
}