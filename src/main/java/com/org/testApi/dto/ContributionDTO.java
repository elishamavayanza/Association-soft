package com.org.testApi.dto;

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
    private LocalDate contributionDate;
    private String status;
    private Long memberId;
    private Long roundId;
}