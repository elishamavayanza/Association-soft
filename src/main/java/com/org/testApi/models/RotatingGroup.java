package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.testApi.models.BaseEntity;
import com.org.testApi.models.Currency;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rotating_groups")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= true)
@ToString(callSuper = true)
public class RotatingGroup extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "contribution_amount", nullable = false)
    private BigDecimal contributionAmount;

    /**
     * Devise du montant de la contribution.
     * Par défaut, CDF (Franc congolais).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    @Builder.Default
    private Currency currency = Currency.CDF;

    @Column(name = "max_members")
    private Integer maxMembers;

    @Enumerated(EnumType.STRING)
    @Column(name = "rotation_frequency", nullable = false)
    private RotationFrequency rotationFrequency;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private GroupStatus status = GroupStatus.PENDING;

    @Column(name = "auto_generate_rounds")
    @Builder.Default
    private Boolean autoGenerateRounds = Boolean.TRUE;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rotating_group_members",
        joinColumns = @JoinColumn(name = "rotating_group_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "rotatingGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Round> rounds = new ArrayList<>();
}