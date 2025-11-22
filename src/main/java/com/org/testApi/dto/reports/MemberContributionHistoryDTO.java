package com.org.testApi.dto.reports;

import com.org.testApi.dto.MemberDTO;
import com.org.testApi.dto.ContributionDTO;
import com.org.testApi.dto.PenaltyDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MemberContributionHistoryDTO {
    private MemberDTO member;
    private List<ContributionDTO> contributions;
    private List<PenaltyDTO> penalties;
    private BigDecimal totalContributions;
    private BigDecimal totalPenalties;
    private BigDecimal netBalance;
}