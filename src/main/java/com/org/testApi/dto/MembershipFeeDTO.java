package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour l'entité MembershipFee.
 */
@Getter
@Setter
public class MembershipFeeDTO extends BaseEntityDTO {

    private Long memberId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String paymentMethod;
}
