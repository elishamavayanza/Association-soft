package com.org.testApi.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

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
    @Builder.Default
    private List<Long> contributionIds = new ArrayList<>();
    @Builder.Default
    private List<Long> penaltyIds = new ArrayList<>();
}