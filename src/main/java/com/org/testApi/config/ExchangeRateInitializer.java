package com.org.testApi.config;

import com.org.testApi.models.Currency;
import com.org.testApi.models.ExchangeRate;
import com.org.testApi.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class ExchangeRateInitializer implements CommandLineRunner {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default exchange rates if they don't exist
        initializeExchangeRates();
    }

    private void initializeExchangeRates() {
        // Define default exchange rates
        List<ExchangeRateData> defaultRates = Arrays.asList(
                // USD to other currencies
                new ExchangeRateData(Currency.USD, Currency.CDF, new BigDecimal("2000.000000")),
                new ExchangeRateData(Currency.USD, Currency.EUR, new BigDecimal("0.900000")),
                
                // EUR to other currencies
                new ExchangeRateData(Currency.EUR, Currency.CDF, new BigDecimal("2200.000000")),
                new ExchangeRateData(Currency.EUR, Currency.USD, new BigDecimal("1.100000")),
                
                // CDF to other currencies
                new ExchangeRateData(Currency.CDF, Currency.USD, new BigDecimal("0.000500")),
                new ExchangeRateData(Currency.CDF, Currency.EUR, new BigDecimal("0.000450"))
        );

        // Save default rates if they don't exist
        for (ExchangeRateData rateData : defaultRates) {
            if (!exchangeRateRepository.existsByFromCurrencyAndToCurrency(
                    rateData.getFromCurrency(), rateData.getToCurrency())) {
                
                ExchangeRate rate = ExchangeRate.builder()
                        .fromCurrency(rateData.getFromCurrency())
                        .toCurrency(rateData.getToCurrency())
                        .rate(rateData.getRate())
                        .build();
                
                exchangeRateRepository.save(rate);
            }
        }
    }

    private static class ExchangeRateData {
        private final Currency fromCurrency;
        private final Currency toCurrency;
        private final BigDecimal rate;

        public ExchangeRateData(Currency fromCurrency, Currency toCurrency, BigDecimal rate) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.rate = rate;
        }

        public Currency getFromCurrency() {
            return fromCurrency;
        }

        public Currency getToCurrency() {
            return toCurrency;
        }

        public BigDecimal getRate() {
            return rate;
        }
    }
}