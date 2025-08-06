package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public enum ERole {
        ROLE_ADMIN,
        ROLE_MODERATOR,
        ROLE_MEMBER,
        ROLE_GUEST
    }
    @Size(max = 20)
    private String description;
}