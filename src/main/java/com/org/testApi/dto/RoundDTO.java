package com.org.testApi.dto;

import com.org.testApi.models.Currency;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RoundDTO extends BaseEntityDTO {
    private Integer roundNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long rotatingGroupId;
    private BigDecimal totalAmountDistributed;
    private Currency currency;
    private List<Long> contributionIds;
    private List<Long> penaltyIds;
}