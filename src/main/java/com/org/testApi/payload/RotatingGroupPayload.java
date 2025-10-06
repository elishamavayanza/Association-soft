package com.org.testApi.payload;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RotatingGroupPayload extends BasePayload {
    private String name;
    private String description;
    private BigDecimal contributionAmount;
    private Integer maxMembers;
    private String rotationFrequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    @Builder.Default
    private List<Long> memberIds = new ArrayList<>();
}