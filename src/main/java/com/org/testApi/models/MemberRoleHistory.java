package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "member_role_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRoleHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    @Column(length = 50)
    private String role;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(columnDefinition = "boolean default false")
    private boolean isAdmin;
}