package com.org.testApi.dto.reports;

import lombok.Data;
import java.util.List;

@Data
public class FinancialReportDTO {
    private List<MemberContributionHistoryDTO> memberContributions;
    private GroupPerformanceMetricsDTO groupMetrics;
    private List<MemberUnpaidBalanceDTO> memberBalances;
    private List<DistributionHistoryDTO> distributionHistory;
}