package com.org.testApi.dto;

import com.org.testApi.models.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateDTO {
    private Long id;
    private Currency fromCurrency;
    private Currency toCurrency;
    private BigDecimal rate;
    private LocalDateTime lastUpdated;
}