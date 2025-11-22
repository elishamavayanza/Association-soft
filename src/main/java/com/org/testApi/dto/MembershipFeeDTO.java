package com.org.testApi.dto;

import com.org.testApi.models.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour l'entité MembershipFee.
 */
@Getter
@Setter
@SuperBuilder
public class MembershipFeeDTO extends BaseEntityDTO {

    private Long memberId;
    private BigDecimal amount;
    private Currency currency;
    private LocalDate paymentDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String paymentMethod;
    private String feeType;
}