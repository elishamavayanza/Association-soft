package com.org.testApi.services;

import com.org.testApi.models.MembershipFee;
import com.org.testApi.models.MembershipFeeType;
import java.util.List;
import java.util.Optional;

public interface MembershipFeeService extends ObservableService<MembershipFee> {
    List<MembershipFee> getAllMembershipFees();
    Optional<MembershipFee> getMembershipFeeById(Long id);
    MembershipFee saveMembershipFee(MembershipFee membershipFee);
    MembershipFee updateMembershipFee(Long id, MembershipFee membershipFee);
    void deleteMembershipFee(Long id);
    void softDeleteMembershipFee(Long id);
    List<MembershipFee> getMembershipFeesByType(MembershipFeeType feeType);
    List<MembershipFee> getMembershipFeesByMemberIdAndType(Long memberId, MembershipFeeType feeType);
    void deleteMembershipFeesByMemberIdAndType(Long memberId, MembershipFeeType feeType);
    void softDeleteMembershipFeesByMemberIdAndType(Long memberId, MembershipFeeType feeType);
}