package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'une FinancialTransaction.
 */
@Getter
@Setter
public class FinancialTransactionRequestDTO extends BaseEntityDTO {

    private BigDecimal amount;
    private LocalDate transactionDate;
    private String description;
    private String type;
    private Long activityId;
    private Long projectId;
    private Long associationId;
    private Long categoryId;
}
