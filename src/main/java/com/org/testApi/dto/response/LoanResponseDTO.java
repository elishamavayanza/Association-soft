package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de réponse pour l'entité Loan.
 */
@Getter
@Setter
public class LoanResponseDTO extends BaseEntityDTO {

    private Long memberId;
    private Long documentId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal penaltyRate;
    private LocalDate dueDate;
    private LocalDate repaymentDate;
    private BigDecimal amountRepaid;
    private String status;
}