package com.org.testApi.services;

import com.org.testApi.models.MembershipFee;
import java.util.List;
import java.util.Optional;

public interface MembershipFeeService extends ObservableService<MembershipFee> {
    List<MembershipFee> getAllMembershipFees();
    Optional<MembershipFee> getMembershipFeeById(Long id);
    MembershipFee saveMembershipFee(MembershipFee membershipFee);
    MembershipFee updateMembershipFee(Long id, MembershipFee membershipFee);
    void deleteMembershipFee(Long id);
    void softDeleteMembershipFee(Long id);
}
