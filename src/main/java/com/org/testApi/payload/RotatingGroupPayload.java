package com.org.testApi.payload;

import com.org.testApi.models.Currency;
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
    private Currency currency;
    private Integer maxMembers;
    private String rotationFrequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Boolean autoGenerateRounds;
    @Builder.Default
    private List<Long> memberIds = new ArrayList<>();
    
    // Method to add member IDs
    public void addMemberId(Long memberId) {
        if (this.memberIds == null) {
            this.memberIds = new ArrayList<>();
        }
        this.memberIds.add(memberId);
    }
    
    // Method to remove member IDs
    public void removeMemberId(Long memberId) {
        if (this.memberIds != null) {
            this.memberIds.remove(memberId);
        }
    }
}