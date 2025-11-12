package com.org.testApi.payload;

import com.org.testApi.models.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RoundPayload extends BasePayload {
    private Integer roundNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long rotatingGroupId;
    private BigDecimal totalAmountDistributed;
    private Currency currency;
    private LocalDateTime distributionDate;
    private BigDecimal amountPerBeneficiary;
    @Builder.Default
    private List<Long> contributionIds = new ArrayList<>();
    @Builder.Default
    private List<Long> penaltyIds = new ArrayList<>();
    @Builder.Default
    private List<Long> beneficiaryIds = new ArrayList<>();
}