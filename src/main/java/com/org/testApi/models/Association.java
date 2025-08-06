package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "associations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Association {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate creationDate;
    private LocalDate lastUpdateDate;

    @Column(length = 100)
    private String location;

    @Column(length = 20)
    private String legalStatus; // Association loi 1901, fondation, etc.

    @Column(length = 50)
    private String siret;

    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<FinancialTransaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<Document> documents = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDate.now();
        this.lastUpdateDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdateDate = LocalDate.now();
    }
}