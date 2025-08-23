package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de réponse pour l'entité FinancialTransaction.
 */
@Getter
@Setter
public class FinancialTransactionResponseDTO extends BaseEntityDTO {

    private BigDecimal amount;
    private LocalDate transactionDate;
    private String description;
    private String type;
    private Long activityId;
    private Long projectId;
    private Long associationId;
    private Long categoryId;
}
