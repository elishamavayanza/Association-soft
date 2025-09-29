package com.org.testApi.services;

import com.org.testApi.models.MembershipFee;
import com.org.testApi.repository.MembershipFeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class MembershipFeeServiceImpl implements MembershipFeeService {

    private static final Logger logger = LoggerFactory.getLogger(MembershipFeeServiceImpl.class);

    @Autowired
    private MembershipFeeRepository membershipFeeRepository;

    private List<Observer<MembershipFee>> observers = new ArrayList<>();

    @Override
    public List<MembershipFee> getAllMembershipFees() {
        logger.info("Fetching all membership fees");
        return membershipFeeRepository.findAll();
    }

    @Override
    public Optional<MembershipFee> getMembershipFeeById(Long id) {
        logger.info("Fetching membership fee by id: {}", id);
        return membershipFeeRepository.findById(id);
    }

    @Override
    public MembershipFee saveMembershipFee(MembershipFee membershipFee) {
        logger.info("Saving membership fee: {}", membershipFee);
        try {
            MembershipFee savedMembershipFee = membershipFeeRepository.save(membershipFee);
            logger.info("Successfully saved membership fee with id: {}", savedMembershipFee.getId());
            notifyObservers("SAVE", savedMembershipFee);
            return savedMembershipFee;
        } catch (Exception e) {
            logger.error("Error saving membership fee: ", e);
            throw new RuntimeException("Failed to save membership fee", e);
        }
    }

    @Override
    public MembershipFee updateMembershipFee(Long id, MembershipFee membershipFee) {
        logger.info("Updating membership fee with id: {}, data: {}", id, membershipFee);
        
        Optional<MembershipFee> existingFeeOptional = membershipFeeRepository.findById(id);
        if (existingFeeOptional.isPresent()) {
            MembershipFee existingFee = existingFeeOptional.get();
            logger.info("Found existing membership fee: {}", existingFee);
            
            // Update only the fields that are provided (not null)
            if (membershipFee.getAmount() != null) {
                existingFee.setAmount(membershipFee.getAmount());
                logger.info("Updated amount to: {}", membershipFee.getAmount());
            }
            if (membershipFee.getPaymentDate() != null) {
                existingFee.setPaymentDate(membershipFee.getPaymentDate());
                logger.info("Updated paymentDate to: {}", membershipFee.getPaymentDate());
            }
            if (membershipFee.getMember() != null) {
                existingFee.setMember(membershipFee.getMember());
                logger.info("Updated member to: {}", membershipFee.getMember());
            }
            if (membershipFee.getPaymentMethod() != null) {
                existingFee.setPaymentMethod(membershipFee.getPaymentMethod());
                logger.info("Updated paymentMethod to: {}", membershipFee.getPaymentMethod());
            }
            if (membershipFee.getReference() != null) {
                existingFee.setReference(membershipFee.getReference());
                logger.info("Updated reference to: {}", membershipFee.getReference());
            }
            if (membershipFee.getStartDate() != null) {
                existingFee.setStartDate(membershipFee.getStartDate());
                logger.info("Updated startDate to: {}", membershipFee.getStartDate());
            }
            if (membershipFee.getEndDate() != null) {
                existingFee.setEndDate(membershipFee.getEndDate());
                logger.info("Updated endDate to: {}", membershipFee.getEndDate());
            }
            
            logger.info("About to save updated membership fee: {}", existingFee);
            try {
                MembershipFee updatedMembershipFee = membershipFeeRepository.save(existingFee);
                logger.info("Successfully updated membership fee with id: {}", updatedMembershipFee.getId());
                notifyObservers("UPDATE", updatedMembershipFee);
                return updatedMembershipFee;
            } catch (Exception e) {
                logger.error("Error saving updated membership fee: ", e);
                throw new RuntimeException("Failed to update membership fee", e);
            }
        }
        logger.warn("Membership fee not found with id: {}", id);
        throw new RuntimeException("MembershipFee not found with id: " + id);
    }

    @Override
    public void deleteMembershipFee(Long id) {
        logger.info("Deleting membership fee with id: {}", id);
        MembershipFee membershipFee = membershipFeeRepository.findById(id).orElse(null);
        membershipFeeRepository.deleteById(id);
        if (membershipFee != null) {
            notifyObservers("DELETE", membershipFee);
        }
    }

    @Override
    public void softDeleteMembershipFee(Long id) {
        logger.info("Soft deleting membership fee with id: {}", id);
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