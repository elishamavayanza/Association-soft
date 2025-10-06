package com.org.testApi.payload;

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
    private LocalDate contributionDate;
    private String status;
    private Long memberId;
    private Long roundId;
}