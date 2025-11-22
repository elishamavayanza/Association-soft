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
public class RotatingGroupDTO extends BaseEntityDTO {
    private String name;
    private String description;
    private BigDecimal contributionAmount;
    private Currency currency;
    private Integer maxMembers;
    private String rotationFrequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private List<Long> memberIds;
    private List<Long> roundIds;
}