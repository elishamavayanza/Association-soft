package com.org.testApi.services;

import com.org.testApi.models.Currency;
import com.org.testApi.models.MembershipFee;
import com.org.testApi.models.Contribution;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.Round;
import com.org.testApi.util.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyConversionService {

    @Autowired
    private CurrencyConverter currencyConverter;

    /**
     * Convert membership fee amount to specified currency
     */
    public BigDecimal convertMembershipFee(MembershipFee fee, Currency targetCurrency) {
        return currencyConverter.convert(fee.getAmount(), fee.getCurrency(), targetCurrency);
    }

    /**
     * Convert contribution amount to specified currency
     */
    public BigDecimal convertContribution(Contribution contribution, Currency targetCurrency) {
        return currencyConverter.convert(contribution.getAmount(), contribution.getCurrency(), targetCurrency);
    }

    /**
     * Convert rotating group contribution amount to specified currency
     */
    public BigDecimal convertRotatingGroupAmount(RotatingGroup group, Currency targetCurrency) {
        return currencyConverter.convert(group.getContributionAmount(), group.getCurrency(), targetCurrency);
    }

    /**
     * Convert round total distributed amount to specified currency
     */
    public BigDecimal convertRoundTotalAmount(Round round, Currency targetCurrency) {
        if (round.getTotalAmountDistributed() == null) {
            return BigDecimal.ZERO;
        }
        return currencyConverter.convert(round.getTotalAmountDistributed(), round.getCurrency(), targetCurrency);
    }

    /**
     * Convert all contributions in a round to specified currency
     */
    public List<BigDecimal> convertRoundContributions(Round round, Currency targetCurrency) {
        return round.getContributions().stream()
                .map(contribution -> convertContribution(contribution, targetCurrency))
                .collect(Collectors.toList());
    }

    /**
     * Get total contributions in a round converted to specified currency
     */
    public BigDecimal getTotalContributionsInCurrency(Round round, Currency targetCurrency) {
        return round.getContributions().stream()
                .map(contribution -> convertContribution(contribution, targetCurrency))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Convert amount to default currency (CDF)
     */
    public BigDecimal convertToDefaultCurrency(BigDecimal amount, Currency currency) {
        return currencyConverter.convertToDefault(amount, currency);
    }
}