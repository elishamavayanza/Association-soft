package com.org.testApi.services;

import com.org.testApi.models.Member;
import com.org.testApi.models.MemberType;
import com.org.testApi.services.ObservableService;
import java.util.List;
import java.util.Optional;

public interface MemberService extends ObservableService<Member> {
    List<Member> searchMembersComplexQuery(String name, String email, MemberType memberType, Long associationId, Boolean isActive);

    List<Member> getAllMembers();
    Optional<Member> getMemberById(Long id);
    Member saveMember(Member member);
    Member updateMember(Long id, Member member);
    void deleteMember(Long id);
    void softDeleteMember(Long id);

    /**
     * Vérifie si un membre est éligible pour emprunter.
     * @param memberId ID du membre
     * @return true si le membre est éligible, false sinon
     */
    boolean isMemberEligibleForLoan(Long memberId);
    
    /**
     * Trouve un membre par son code membre.
     * @param memberCode le code membre
     * @return le membre correspondant ou null si non trouvé
     */
    Optional<Member> findByMemberCode(String memberCode);
}