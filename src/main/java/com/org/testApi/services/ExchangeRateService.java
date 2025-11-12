package com.org.testApi.services;

import com.org.testApi.models.Currency;
import com.org.testApi.models.ExchangeRate;
import com.org.testApi.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    /**
     * Get exchange rate from one currency to another
     */
    public Optional<BigDecimal> getExchangeRate(Currency from, Currency to) {
        if (from == to) {
            return Optional.of(BigDecimal.ONE);
        }

        return exchangeRateRepository.findByFromCurrencyAndToCurrency(from, to)
                .map(ExchangeRate::getRate);
    }

    /**
     * Convert amount from one currency to another
     */
    public BigDecimal convertAmount(BigDecimal amount, Currency from, Currency to) {
        if (from == to) {
            return amount;
        }

        Optional<BigDecimal> rate = getExchangeRate(from, to);
        if (rate.isPresent()) {
            return amount.multiply(rate.get()).setScale(6, RoundingMode.HALF_UP);
        } else {
            // Try reverse conversion
            Optional<BigDecimal> reverseRate = getExchangeRate(to, from);
            if (reverseRate.isPresent()) {
                return amount.divide(reverseRate.get(), 6, RoundingMode.HALF_UP);
            }
        }

        throw new IllegalArgumentException("No exchange rate found between " + from + " and " + to);
    }

    /**
     * Update or create exchange rate
     */
    public ExchangeRate updateExchangeRate(Currency from, Currency to, BigDecimal rate) {
        Optional<ExchangeRate> existingRate = exchangeRateRepository
                .findByFromCurrencyAndToCurrency(from, to);

        ExchangeRate exchangeRate;
        if (existingRate.isPresent()) {
            exchangeRate = existingRate.get();
            exchangeRate.setRate(rate);
        } else {
            exchangeRate = ExchangeRate.builder()
                    .fromCurrency(from)
                    .toCurrency(to)
                    .rate(rate)
                    .build();
        }
        
        exchangeRate.setLastUpdated(LocalDateTime.now());
        return exchangeRateRepository.save(exchangeRate);
    }

    /**
     * Get all exchange rates
     */
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    /**
     * Check if exchange rate exists between two currencies
     */
    public boolean existsExchangeRate(Currency from, Currency to) {
        return exchangeRateRepository.existsByFromCurrencyAndToCurrency(from, to);
    }
}