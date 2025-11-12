package com.org.testApi.dto;

import com.org.testApi.models.Currency;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ContributionDTO extends BaseEntityDTO {
    private BigDecimal amount;
    private Currency currency;
    private LocalDate contributionDate;
    private String status;
    private Long memberId;
    private Long roundId;
}