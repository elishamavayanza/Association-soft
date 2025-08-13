package com.org.testApi.services;

import com.org.testApi.models.Member;
import java.util.List;
import java.util.Optional;

public interface MemberService extends ObservableService<Member> {
    List<Member> getAllMembers();
    Optional<Member> getMemberById(Long id);
    Member saveMember(Member member);
    Member updateMember(Long id, Member member);
    void deleteMember(Long id);
    void softDeleteMember(Long id);
}
