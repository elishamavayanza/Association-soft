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
public class PenaltyDTO extends BaseEntityDTO {
    private BigDecimal amount;
    private String reason;
    private String penaltyType;
    private LocalDate penaltyDate;
    private String status;
    private Long memberId;
    private Long roundId;
}