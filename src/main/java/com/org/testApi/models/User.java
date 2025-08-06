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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    @Email(message = "L'email doit être une adresse valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<Activity> createdEvents = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    @ToString.Exclude
    private List<Activity> attendedEvents = new ArrayList<>();

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean active = true;

    private LocalDateTime lastLogin;
    private int loginAttempts = 0;

}