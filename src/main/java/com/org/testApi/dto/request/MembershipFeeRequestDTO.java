package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'une MembershipFee.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipFeeRequestDTO extends BaseEntityDTO {

    private Long memberId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String paymentMethod;
}
