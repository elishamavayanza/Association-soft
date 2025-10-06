package com.org.testApi.services;

import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.Round;
import com.org.testApi.models.Contribution;
import com.org.testApi.models.Penalty;
import com.org.testApi.models.Member;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RotatingService {
    
    // Rotating Group methods
    RotatingGroup createRotatingGroup(String name, String description, BigDecimal contributionAmount, 
                                     Integer maxMembers, String rotationFrequency, LocalDate startDate);
    
    Optional<RotatingGroup> findRotatingGroupById(Long id);
    
    List<RotatingGroup> findAllRotatingGroups();
    
    RotatingGroup updateRotatingGroup(Long id, RotatingGroup rotatingGroup);
    
    void deleteRotatingGroup(Long id);
    
    List<RotatingGroup> findActiveRotatingGroups();
    
    // Round methods
    Round createRound(Long rotatingGroupId, Integer roundNumber, LocalDate startDate, LocalDate endDate);
    
    Optional<Round> findRoundById(Long id);
    
    List<Round> findRoundsByRotatingGroup(Long rotatingGroupId);
    
    Round updateRound(Long id, Round round);
    
    void deleteRound(Long id);
    
    // Contribution methods
    Contribution makeContribution(Long memberId, Long roundId, BigDecimal amount, LocalDate contributionDate);
    
    Optional<Contribution> findContributionById(Long id);
    
    List<Contribution> findContributionsByMember(Long memberId);
    
    List<Contribution> findContributionsByRound(Long roundId);
    
    Contribution updateContribution(Long id, Contribution contribution);
    
    void deleteContribution(Long id);
    
    // Penalty methods
    Penalty applyPenalty(Long memberId, Long roundId, BigDecimal amount, String reason, 
                        String penaltyType, LocalDate penaltyDate);
    
    Optional<Penalty> findPenaltyById(Long id);
    
    List<Penalty> findPenaltiesByMember(Long memberId);
    
    List<Penalty> findPenaltiesByRound(Long roundId);
    
    Penalty updatePenalty(Long id, Penalty penalty);
    
    void deletePenalty(Long id);
    
    // Utility methods
    BigDecimal calculateTotalContributionsForMember(Long memberId);
    
    BigDecimal calculateTotalPenaltiesForMember(Long memberId);
    
    BigDecimal calculateRemainingAmountForMember(Long memberId);
}