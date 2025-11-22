package com.org.testApi.controllers;

import com.org.testApi.models.Currency;
import com.org.testApi.services.ExchangeRateService;
import com.org.testApi.util.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currency-demo")
public class CurrencyDemoController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private CurrencyConverter currencyConverter;

    /**
     * Get all current exchange rates
     */
    @GetMapping("/rates")
    public ResponseEntity<List<?>> getAllExchangeRates() {
        return ResponseEntity.ok(exchangeRateService.getAllExchangeRates());
    }

    /**
     * Demo endpoint showing how to convert an amount between currencies
     */
    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "USD") Currency from,
            @RequestParam(defaultValue = "CDF") Currency to) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            BigDecimal convertedAmount = currencyConverter.convert(amount, from, to);
            
            result.put("originalAmount", amount);
            result.put("fromCurrency", from);
            result.put("toCurrency", to);
            result.put("convertedAmount", convertedAmount);
            
            // Also include exchange rate
            exchangeRateService.getExchangeRate(from, to)
                    .ifPresent(rate -> result.put("exchangeRate", rate));
                    
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("error", "Conversion failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * Demo endpoint to show conversion to default currency (CDF)
     */
    @GetMapping("/convert-to-default")
    public ResponseEntity<Map<String, Object>> convertToDefaultCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam Currency currency) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            BigDecimal convertedAmount = currencyConverter.convertToDefault(amount, currency);
            
            result.put("originalAmount", amount);
            result.put("originalCurrency", currency);
            result.put("defaultCurrency", Currency.CDF);
            result.put("convertedAmount", convertedAmount);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("error", "Conversion failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}