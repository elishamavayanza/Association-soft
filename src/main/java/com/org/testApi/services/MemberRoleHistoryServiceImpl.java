package com.org.testApi.services;

import com.org.testApi.models.MemberRoleHistory;
import com.org.testApi.repository.MemberRoleHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class MemberRoleHistoryServiceImpl implements MemberRoleHistoryService {

    @Autowired
    private MemberRoleHistoryRepository memberRoleHistoryRepository;

    private List<Observer<MemberRoleHistory>> observers = new ArrayList<>();

    @Override
    public List<MemberRoleHistory> getAllMemberRoleHistories() {
        return memberRoleHistoryRepository.findAll();
    }

    @Override
    public Optional<MemberRoleHistory> getMemberRoleHistoryById(Long id) {
        return memberRoleHistoryRepository.findById(id);
    }

    @Override
    public MemberRoleHistory saveMemberRoleHistory(MemberRoleHistory memberRoleHistory) {
        MemberRoleHistory savedMemberRoleHistory = memberRoleHistoryRepository.save(memberRoleHistory);
        notifyObservers("SAVE", savedMemberRoleHistory);
        return savedMemberRoleHistory;
    }

    @Override
    public MemberRoleHistory updateMemberRoleHistory(Long id, MemberRoleHistory memberRoleHistory) {
        if (memberRoleHistoryRepository.existsById(id)) {
            memberRoleHistory.setId(id);
            MemberRoleHistory updatedMemberRoleHistory = memberRoleHistoryRepository.save(memberRoleHistory);
            notifyObservers("UPDATE", updatedMemberRoleHistory);
            return updatedMemberRoleHistory;
        }
        throw new RuntimeException("MemberRoleHistory not found with id: " + id);
    }

    @Override
    public void deleteMemberRoleHistory(Long id) {
        MemberRoleHistory memberRoleHistory = memberRoleHistoryRepository.findById(id).orElse(null);
        memberRoleHistoryRepository.deleteById(id);
        if (memberRoleHistory != null) {
            notifyObservers("DELETE", memberRoleHistory);
        }
    }

    @Override
    public void softDeleteMemberRoleHistory(Long id) {
        MemberRoleHistory memberRoleHistory = memberRoleHistoryRepository.findById(id).orElse(null);
        memberRoleHistoryRepository.softDelete(id);
        if (memberRoleHistory != null) {
            notifyObservers("SOFT_DELETE", memberRoleHistory);
        }
    }

    @Override
    public void addObserver(Observer<MemberRoleHistory> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<MemberRoleHistory> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, MemberRoleHistory entity) {
        for (Observer<MemberRoleHistory> observer : observers) {
            observer.update(event, entity);
        }
    }
}
