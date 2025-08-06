package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ActivityType type; // CONFERENCE, WORKSHOP, PROJECT, etc.

    @FutureOrPresent
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Column(length = 100)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    private Project project; // Si l'activité fait partie d'un projet

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @ToString.Exclude
    private User createdBy;

    @ManyToMany
    @JoinTable(name = "activity_participants",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    @ToString.Exclude
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<FinancialTransaction> transactions = new ArrayList<>();

    @AssertTrue
    private boolean isEndDateValid() {
        return endDateTime == null || startDateTime == null || endDateTime.isAfter(startDateTime);
    }

    @Enumerated(EnumType.STRING)
    private ActivityStatus status = ActivityStatus.PLANNED;

    public enum ActivityType {
        CONFERENCE, WORKSHOP, MEETING, PROJECT, TRAINING, SOCIAL_EVENT, OTHER
    }

    public enum ActivityStatus {
        PLANNED, ONGOING, COMPLETED, CANCELLED
    }
}