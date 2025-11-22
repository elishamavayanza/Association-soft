package com.org.testApi.dto;

import com.org.testApi.models.Currency;
import com.org.testApi.validation.ValidCurrency; // Added import for currency validation
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour l'entité FinancialTransaction.
 */
@Getter
@Setter
@SuperBuilder
public class FinancialTransactionDTO extends BaseEntityDTO {

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal amount;
    
    @ValidCurrency
    private Currency currency;
    
    @NotNull(message = "La date de transaction est obligatoire")
    private LocalDate transactionDate;
    
    @Size(max = 100, message = "La description ne doit pas dépasser 100 caractères")
    private String description;
    
    @NotBlank(message = "Le type est obligatoire")
    private String type;
    
    private Long activityId;
    private Long projectId;
    private Long associationId;
    private Long categoryId;
}