package com.org.testApi.payload;

import lombok.*;

import java.time.LocalDate;
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
    @Builder.Default
    private List<Long> contributionIds = new ArrayList<>();
    @Builder.Default
    private List<Long> penaltyIds = new ArrayList<>();
}