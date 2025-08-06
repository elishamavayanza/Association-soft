package com.org.testApi.models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    @Size(max = 2000)
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @ToString.Exclude
    private User manager;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<FinancialTransaction> transactions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNING;

    public enum ProjectStatus {
        PLANNING, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    }
}