package com.org.testApi.services;

import com.org.testApi.models.MembershipFee;
import com.org.testApi.repository.MembershipFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class MembershipFeeServiceImpl implements MembershipFeeService {

    @Autowired
    private MembershipFeeRepository membershipFeeRepository;

    private List<Observer<MembershipFee>> observers = new ArrayList<>();

    @Override
    public List<MembershipFee> getAllMembershipFees() {
        return membershipFeeRepository.findAll();
    }

    @Override
    public Optional<MembershipFee> getMembershipFeeById(Long id) {
        return membershipFeeRepository.findById(id);
    }

    @Override
    public MembershipFee saveMembershipFee(MembershipFee membershipFee) {
        MembershipFee savedMembershipFee = membershipFeeRepository.save(membershipFee);
        notifyObservers("SAVE", savedMembershipFee);
        return savedMembershipFee;
    }

    @Override
    public MembershipFee updateMembershipFee(Long id, MembershipFee membershipFee) {
        if (membershipFeeRepository.existsById(id)) {
            membershipFee.setId(id);
            MembershipFee updatedMembershipFee = membershipFeeRepository.save(membershipFee);
            notifyObservers("UPDATE", updatedMembershipFee);
            return updatedMembershipFee;
        }
        throw new RuntimeException("MembershipFee not found with id: " + id);
    }

    @Override
    public void deleteMembershipFee(Long id) {
        MembershipFee membershipFee = membershipFeeRepository.findById(id).orElse(null);
        membershipFeeRepository.deleteById(id);
        if (membershipFee != null) {
            notifyObservers("DELETE", membershipFee);
        }
    }

    @Override
    public void softDeleteMembershipFee(Long id) {
        MembershipFee membershipFee = membershipFeeRepository.findById(id).orElse(null);
        membershipFeeRepository.softDelete(id);
        if (membershipFee != null) {
            notifyObservers("SOFT_DELETE", membershipFee);
        }
    }

    @Override
    public void addObserver(Observer<MembershipFee> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<MembershipFee> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, MembershipFee entity) {
        for (Observer<MembershipFee> observer : observers) {
            observer.update(event, entity);
        }
    }
}
