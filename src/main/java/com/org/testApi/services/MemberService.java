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

    /**
     * Recherche des membres avec des filtres complexes
     * @param name nom d'utilisateur (recherche partielle)
     * @param email adresse email (recherche partielle)
     * @param memberType type de membre
     * @param associationId ID de l'association
     * @param isActive indique si le membre est actif
     * @return Liste des membres correspondant aux critères
     */
    List<Member> searchMembersComplexQuery(String name, String email, Member.MemberType memberType, Long associationId, Boolean isActive);

    /**
     * Vérifie si un membre est éligible pour emprunter.
     * @param memberId ID du membre
     * @return true si le membre est éligible, false sinon
     */
    boolean isMemberEligibleForLoan(Long memberId);
}
