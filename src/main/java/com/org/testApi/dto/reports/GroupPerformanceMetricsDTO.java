package com.org.testApi.dto.reports;

import com.org.testApi.dto.RotatingGroupDTO;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class GroupPerformanceMetricsDTO {
    private RotatingGroupDTO group;
    private Long totalMembers;
    private Long totalRounds;
    private Long completedRounds;
    private BigDecimal totalContributions;
    private BigDecimal totalDistributed;
    private BigDecimal averageContributionPerMember;
    private Double contributionRate; // Pourcentage de contributions par rapport aux attentes
}