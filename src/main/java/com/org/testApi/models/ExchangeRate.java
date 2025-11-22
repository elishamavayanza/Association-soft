package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an exchange rate between two currencies.
 * This entity stores exchange rates that can be used to convert amounts between currencies.
 */
@Entity
@Table(name = "exchange_rates", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"from_currency", "to_currency"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ExchangeRate extends BaseEntity {

    /**
     * The currency to convert from.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency", nullable = false)
    private Currency fromCurrency;

    /**
     * The currency to convert to.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency", nullable = false)
    private Currency toCurrency;

    /**
     * The exchange rate value.
     * For example: 1 USD = 2000 CDF, rate would be 2000
     */
    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    /**
     * Date and time when the rate was last updated.
     */
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}