package com.org.testApi.repository;

import com.org.testApi.models.Currency;
import com.org.testApi.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    
    /**
     * Find exchange rate by from and to currencies
     */
    Optional<ExchangeRate> findByFromCurrencyAndToCurrency(Currency from, Currency to);
    
    /**
     * Check if an exchange rate exists between two currencies
     */
    boolean existsByFromCurrencyAndToCurrency(Currency from, Currency to);
}