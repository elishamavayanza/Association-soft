package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "membership_fees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate paymentDate;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    @ToString.Exclude
    private FinancialTransaction transaction;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(length = 50)
    private String reference;

    public enum PaymentMethod {
        CASH, CHECK, BANK_TRANSFER, CREDIT_CARD, OTHER
    }
}
