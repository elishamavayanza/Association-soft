package com.org.testApi;

import com.org.testApi.models.Currency;
import com.org.testApi.services.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Test
    public void testUpdateAndGetExchangeRate() {
        // Update exchange rate
        exchangeRateService.updateExchangeRate(Currency.USD, Currency.EUR, new BigDecimal("0.95"));

        // Get exchange rate
        BigDecimal rate = exchangeRateService.getExchangeRate(Currency.USD, Currency.EUR)
                .orElse(BigDecimal.ZERO);

        assertEquals(new BigDecimal("0.95"), rate);
    }

    @Test
    public void testConvertAmount() {
        // Update exchange rate
        exchangeRateService.updateExchangeRate(Currency.USD, Currency.CDF, new BigDecimal("2000"));

        // Convert amount
        BigDecimal converted = exchangeRateService.convertAmount(
                new BigDecimal("100"), Currency.USD, Currency.CDF);

        assertEquals(new BigDecimal("200000"), converted);
    }

    @Test
    public void testSameCurrencyConversion() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal converted = exchangeRateService.convertAmount(amount, Currency.CDF, Currency.CDF);
        assertEquals(amount, converted);
    }
}