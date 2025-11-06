package com.org.testApi.dto.reports;

import com.org.testApi.dto.RoundDTO;
import com.org.testApi.dto.MemberDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DistributionHistoryDTO {
    private RoundDTO round;
    private List<MemberDTO> beneficiaries;
    private BigDecimal totalAmountDistributed;
    private BigDecimal amountPerBeneficiary;
    private LocalDateTime distributionDate;
}