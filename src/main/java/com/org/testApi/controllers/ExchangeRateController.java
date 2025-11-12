package com.org.testApi.controllers;

import com.org.testApi.models.Currency;
import com.org.testApi.models.ExchangeRate;
import com.org.testApi.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController extends BaseController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    /**
     * Get all exchange rates
     */
    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        List<ExchangeRate> rates = exchangeRateService.getAllExchangeRates();
        return ResponseEntity.ok(rates);
    }

    /**
     * Update or create exchange rate
     */
    @PostMapping("/update")
    public ResponseEntity<ExchangeRate> updateExchangeRate(
            @RequestParam Currency from,
            @RequestParam Currency to,
            @RequestParam BigDecimal rate) {
        
        // Prevent setting rate to same currency
        if (from == to) {
            return ResponseEntity.badRequest().build();
        }
        
        ExchangeRate updatedRate = exchangeRateService.updateExchangeRate(from, to, rate);
        return ResponseEntity.ok(updatedRate);
    }

    /**
     * Convert amount from one currency to another
     */
    @GetMapping("/convert")
    public ResponseEntity<BigDecimal> convertAmount(
            @RequestParam BigDecimal amount,
            @RequestParam Currency from,
            @RequestParam Currency to) {
        
        try {
            BigDecimal converted = exchangeRateService.convertAmount(amount, from, to);
            return ResponseEntity.ok(converted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get exchange rate between two currencies
     */
    @GetMapping("/rate")
    public ResponseEntity<BigDecimal> getExchangeRate(
            @RequestParam Currency from,
            @RequestParam Currency to) {
        
        return exchangeRateService.getExchangeRate(from, to)
                .map(rate -> ResponseEntity.ok(rate))
                .orElse(ResponseEntity.notFound().build());
    }
}