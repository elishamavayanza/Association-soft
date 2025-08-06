package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "project_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @ToString.Exclude
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    @Column(length = 50)
    private String roleInProject;

    private LocalDate joinDate;
    private LocalDate leaveDate;

    @PrePersist
    protected void onCreate() {
        if (this.joinDate == null) {
            this.joinDate = LocalDate.now();
        }
    }

    public boolean isActive() {
        return leaveDate == null;
    }
}