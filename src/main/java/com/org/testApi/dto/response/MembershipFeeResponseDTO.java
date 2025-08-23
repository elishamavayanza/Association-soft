package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de réponse pour l'entité MembershipFee.
 */
@Getter
@Setter
public class MembershipFeeResponseDTO extends BaseEntityDTO {

    private Long memberId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String paymentMethod;
}
