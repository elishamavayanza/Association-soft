package com.org.testApi.services;

import com.org.testApi.models.Member;
import com.org.testApi.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private List<Observer<Member>> observers = new ArrayList<>();

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member saveMember(Member member) {
        Member savedMember = memberRepository.save(member);
        notifyObservers("SAVE", savedMember);
        return savedMember;
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
}
