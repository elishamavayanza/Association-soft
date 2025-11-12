package com.org.testApi.repository.custom;

import com.org.testApi.models.Member;
import com.org.testApi.models.MemberType;
import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> searchMembersComplexQuery(String name, String email, MemberType memberType, Long associationId, Boolean isActive);
    
    // Added missing methods that are implemented in MemberRepositoryImpl
    List<Member> findMembersWithAssociations(Long associationId, int limit);
    List<Object[]> findActiveMembersWithFeeCount(Long associationId);
}