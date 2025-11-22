package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "penalties")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Penalty extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /**
     * Devise du montant de la pénalité.
     * Par défaut, CDF (Franc congolais).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    @Builder.Default
    private Currency currency = Currency.CDF;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "penalty_type")
    private PenaltyType penaltyType;

    @Column(name = "penalty_date")
    private LocalDate penaltyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private PenaltyStatus status = PenaltyStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Round round;
}