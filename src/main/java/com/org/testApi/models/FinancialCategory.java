package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "financial_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    private FinancialTransaction.TransactionType type; // INCOME or EXPENSE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    @ToString.Exclude
    private Association association;
}
