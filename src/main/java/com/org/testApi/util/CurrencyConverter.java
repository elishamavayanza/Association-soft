package com.org.testApi.util;

import com.org.testApi.models.Currency;
import com.org.testApi.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CurrencyConverter {

    @Autowired
    private ExchangeRateService exchangeRateService;

    /**
     * Convert amount from one currency to another
     *
     * @param amount The amount to convert
     * @param from   Source currency
     * @param to     Target currency
     * @return Converted amount
     */
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (from == to) {
            return amount;
        }

        return exchangeRateService.convertAmount(amount, from, to);
    }

    /**
     * Convert amount to default currency (CDF)
     *
     * @param amount   The amount to convert
     * @param currency Source currency
     * @return Amount converted to CDF
     */
    public BigDecimal convertToDefault(BigDecimal amount, Currency currency) {
        return convert(amount, currency, Currency.CDF);
    }

    /**
     * Get exchange rate between two currencies
     *
     * @param from Source currency
     * @param to   Target currency
     * @return Exchange rate
     */
    public BigDecimal getExchangeRate(Currency from, Currency to) {
        return exchangeRateService.getExchangeRate(from, to)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No exchange rate found between " + from + " and " + to));
    }
}