package com.org.testApi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidCurrencyValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrency {
    String message() default "Devise non valide. Les devises autorisées sont USD, CDF et EUR.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}