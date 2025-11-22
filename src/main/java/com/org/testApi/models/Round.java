package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rounds")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Round extends BaseEntity {

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private RoundStatus status = RoundStatus.UPCOMING;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rotating_group_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private RotatingGroup rotatingGroup;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Contribution> contributions = new ArrayList<>();

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Penalty> penalties = new ArrayList<>();
    
    // Many-to-many relationship for beneficiaries (members who receive money from this round)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "round_beneficiaries",
        joinColumns = @JoinColumn(name = "round_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Member> beneficiaries = new ArrayList<>();
    
    // Total amount distributed in this round
    @Column(name = "total_amount_distributed")
    private BigDecimal totalAmountDistributed;
    
    /**
     * Devise du montant total distribué.
     * Hérite de la devise du groupe rotatif.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    
    // Date when the money was distributed to beneficiaries
    @Column(name = "distribution_date")
    private LocalDateTime distributionDate;
    
    // Amount each beneficiary receives (if equally distributed)
    @Column(name = "amount_per_beneficiary")
    private BigDecimal amountPerBeneficiary;
}