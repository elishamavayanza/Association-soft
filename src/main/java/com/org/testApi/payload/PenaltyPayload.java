package com.org.testApi.payload;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PenaltyPayload extends BasePayload {
    private BigDecimal amount;
    private String reason;
    private String penaltyType;
    private LocalDate penaltyDate;
    private String status;
    private Long memberId;
    private Long roundId;
}