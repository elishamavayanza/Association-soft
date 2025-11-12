package com.org.testApi.payload;

import com.org.testApi.models.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ContributionPayload extends BasePayload {
    private BigDecimal amount;
    private Currency currency;
    private LocalDate contributionDate;
    private String status;
    private Long memberId;
    private Long roundId;
}