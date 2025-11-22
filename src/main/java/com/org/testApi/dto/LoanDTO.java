package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour l'entité Loan.
 */
@Getter
@Setter
@SuperBuilder
public class LoanDTO extends BaseEntityDTO {

    private Long memberId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal penaltyRate;
    private LocalDate dueDate;
    private LocalDate repaymentDate;
    private BigDecimal amountRepaid;
    private String status;
}
