package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'un Loan.
 */
@Getter
@Setter
@SuperBuilder
public class LoanRequestDTO extends BaseEntityDTO {

    private Long memberId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal penaltyRate;
    private LocalDate dueDate;
    private LocalDate repaymentDate;
    private BigDecimal amountRepaid;
    private String status;
}
