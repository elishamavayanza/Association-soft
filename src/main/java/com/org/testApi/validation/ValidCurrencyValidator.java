package com.org.testApi.validation;

import com.org.testApi.models.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCurrencyValidator implements ConstraintValidator<ValidCurrency, Currency> {
    
    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext context) {
        // La validation est faite automatiquement par le fait que Currency est un enum
        // Cette validation supplémentaire garantit que la devise est l'une des valeurs autorisées
        return currency == null || 
               currency == Currency.USD || 
               currency == Currency.CDF || 
               currency == Currency.EUR;
    }
}