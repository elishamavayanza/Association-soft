package com.org.testApi.dto;

import com.org.testApi.models.Currency;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LoanEligibilityResult {
    private boolean eligible;
    private List<String> reasons = new ArrayList<>();
    private BigDecimal maxLoanAmount;
    private Currency currency;

    public void addReason(String reason) {
        this.reasons.add(reason);
    }
}