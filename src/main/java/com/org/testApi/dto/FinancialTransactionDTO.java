package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour l'entité FinancialTransaction.
 */
@Getter
@Setter
@SuperBuilder
public class FinancialTransactionDTO extends BaseEntityDTO {

    private BigDecimal amount;
    private LocalDate transactionDate;
    private String description;
    private String type;
    private Long activityId;
    private Long projectId;
    private Long associationId;
    private Long categoryId;
}
