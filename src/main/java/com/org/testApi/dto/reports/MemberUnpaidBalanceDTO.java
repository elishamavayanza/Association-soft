package com.org.testApi.dto.reports;

import com.org.testApi.dto.MemberDTO;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MemberUnpaidBalanceDTO {
    private MemberDTO member;
    private BigDecimal expectedContributions;
    private BigDecimal actualContributions;
    private BigDecimal unpaidBalance;
    private BigDecimal totalPenalties;
}