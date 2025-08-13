package com.org.testApi.services;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService extends ObservableService<String> {
    List<Object> generateActivityReport(LocalDateTime startDate, LocalDateTime endDate);
    List<Object> generateFinancialReport(LocalDateTime startDate, LocalDateTime endDate);
    List<Object> generateMembershipReport();
}
