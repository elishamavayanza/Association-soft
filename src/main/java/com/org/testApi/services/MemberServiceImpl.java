package com.org.testApi.services;

import com.org.testApi.models.Member;
import com.org.testApi.repository.MemberRepository;
import com.org.testApi.services.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberRepository memberRepository;

    private List<Observer<Member>> observers = new ArrayList<>();

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findWithUserAndAssociationById(id);
    }

    @Override
    public Member saveMember(Member member) {
        try {
            logger.info("Saving member: {}", member);
            // Générer un code membre unique si ce n'est pas déjà fait
            if (member.getMemberCode() == null) {
                member.setMemberCode(generateUniqueMemberCode());
                logger.info("Generated member code: {}", member.getMemberCode());
            }
            
            logger.info("Calling memberRepository.save()");
            Member savedMember = memberRepository.save(member);
            logger.info("Member saved successfully with ID: {}", savedMember.getId());
            notifyObservers("SAVE", savedMember);
            return savedMember;
        } catch (Exception e) {
            logger.error("Error saving member: ", e);
            throw new RuntimeException("Failed to save member: " + e.getMessage(), e);
        }
    }

    @Override
    public Member updateMember(Long id, Member member) {
        if (memberRepository.existsById(id)) {
            member.setId(id);
            Member updatedMember = memberRepository.save(member);
            notifyObservers("UPDATE", updatedMember);
            return updatedMember;
        }
        throw new RuntimeException("Member not found with id: " + id);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        memberRepository.deleteById(id);
        if (member != null) {
            notifyObservers("DELETE", member);
        }
    }

    @Override
    public void softDeleteMember(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        memberRepository.softDelete(id);
        if (member != null) {
            notifyObservers("SOFT_DELETE", member);
        }
    }

    @Override
    public List<Member> searchMembersComplexQuery(String name, String email, Member.MemberType memberType, Long associationId, Boolean isActive) {
        return memberRepository.searchMembersComplexQuery(name, email, memberType, associationId, isActive);
    }

    @Override
    public boolean isMemberEligibleForLoan(Long memberId) {
        return memberRepository.findById(memberId)
                .map(Member::isEligibleForLoan)
                .orElse(false);
    }

    @Override
    public void addObserver(Observer<Member> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Member> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, Member entity) {
        for (Observer<Member> observer : observers) {
            observer.update(event, entity);
        }
    }
    
    /**
     * Génère un code membre unique
     * @return Un code membre unique
     */
    private String generateUniqueMemberCode() {
        return "MBR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}