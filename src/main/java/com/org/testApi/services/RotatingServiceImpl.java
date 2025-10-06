package com.org.testApi.services;

import com.org.testApi.models.*;
import com.org.testApi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RotatingServiceImpl implements RotatingService {
    
    @Autowired
    private RotatingGroupRepository rotatingGroupRepository;
    
    @Autowired
    private RoundRepository roundRepository;
    
    @Autowired
    private ContributionRepository contributionRepository;
    
    @Autowired
    private PenaltyRepository penaltyRepository;
    
    @Autowired
    private MemberRepository memberRepository;

    // Rotating Group methods
    @Override
    public RotatingGroup createRotatingGroup(String name, String description, BigDecimal contributionAmount,
                                           Integer maxMembers, String rotationFrequency, LocalDate startDate) {
        RotatingGroup rotatingGroup = RotatingGroup.builder()
                .name(name)
                .description(description)
                .contributionAmount(contributionAmount)
                .maxMembers(maxMembers)
                .rotationFrequency(RotationFrequency.valueOf(rotationFrequency))
                .startDate(startDate)
                .status(GroupStatus.ACTIVE)
                .build();
        
        return rotatingGroupRepository.save(rotatingGroup);
    }

    @Override
    public Optional<RotatingGroup> findRotatingGroupById(Long id) {
        return rotatingGroupRepository.findById(id);
    }

    @Override
    public List<RotatingGroup> findAllRotatingGroups() {
        return rotatingGroupRepository.findAll();
    }

    @Override
    public RotatingGroup updateRotatingGroup(Long id, RotatingGroup rotatingGroup) {
        rotatingGroup.setId(id);
        return rotatingGroupRepository.save(rotatingGroup);
    }

    @Override
    public void deleteRotatingGroup(Long id) {
        rotatingGroupRepository.deleteById(id);
    }

    @Override
    public List<RotatingGroup> findActiveRotatingGroups() {
        return rotatingGroupRepository.findByStatus(GroupStatus.ACTIVE.name());
    }

    // Round methods
    @Override
    public Round createRound(Long rotatingGroupId, Integer roundNumber, LocalDate startDate, LocalDate endDate) {
        Optional<RotatingGroup> rotatingGroupOpt = rotatingGroupRepository.findById(rotatingGroupId);
        if (rotatingGroupOpt.isPresent()) {
            RotatingGroup rotatingGroup = rotatingGroupOpt.get();
            Round round = Round.builder()
                    .roundNumber(roundNumber)
                    .startDate(startDate)
                    .endDate(endDate)
                    .status(RoundStatus.UPCOMING)
                    .rotatingGroup(rotatingGroup)
                    .build();
            
            return roundRepository.save(round);
        }
        throw new RuntimeException("Rotating group not found with id: " + rotatingGroupId);
    }

    @Override
    public Optional<Round> findRoundById(Long id) {
        return roundRepository.findById(id);
    }

    @Override
    public List<Round> findRoundsByRotatingGroup(Long rotatingGroupId) {
        Optional<RotatingGroup> rotatingGroupOpt = rotatingGroupRepository.findById(rotatingGroupId);
        if (rotatingGroupOpt.isPresent()) {
            return roundRepository.findByRotatingGroup(rotatingGroupOpt.get());
        }
        return List.of();
    }

    @Override
    public Round updateRound(Long id, Round round) {
        round.setId(id);
        return roundRepository.save(round);
    }

    @Override
    public void deleteRound(Long id) {
        roundRepository.deleteById(id);
    }

    // Contribution methods
    @Override
    public Contribution makeContribution(Long memberId, Long roundId, BigDecimal amount, LocalDate contributionDate) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        
        if (memberOpt.isPresent() && roundOpt.isPresent()) {
            Contribution contribution = Contribution.builder()
                    .amount(amount)
                    .contributionDate(contributionDate)
                    .status(ContributionStatus.PAID)
                    .member(memberOpt.get())
                    .round(roundOpt.get())
                    .build();
            
            return contributionRepository.save(contribution);
        }
        throw new RuntimeException("Member or Round not found");
    }

    @Override
    public Optional<Contribution> findContributionById(Long id) {
        return contributionRepository.findById(id);
    }

    @Override
    public List<Contribution> findContributionsByMember(Long memberId) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isPresent()) {
            return contributionRepository.findByMember(memberOpt.get());
        }
        return List.of();
    }

    @Override
    public List<Contribution> findContributionsByRound(Long roundId) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (roundOpt.isPresent()) {
            return contributionRepository.findByRound(roundOpt.get());
        }
        return List.of();
    }

    @Override
    public Contribution updateContribution(Long id, Contribution contribution) {
        contribution.setId(id);
        return contributionRepository.save(contribution);
    }

    @Override
    public void deleteContribution(Long id) {
        contributionRepository.deleteById(id);
    }

    // Penalty methods
    @Override
    public Penalty applyPenalty(Long memberId, Long roundId, BigDecimal amount, String reason,
                              String penaltyType, LocalDate penaltyDate) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        
        if (memberOpt.isPresent() && roundOpt.isPresent()) {
            Penalty penalty = Penalty.builder()
                    .amount(amount)
                    .reason(reason)
                    .penaltyType(PenaltyType.valueOf(penaltyType))
                    .penaltyDate(penaltyDate)
                    .status(PenaltyStatus.PENDING)
                    .member(memberOpt.get())
                    .round(roundOpt.get())
                    .build();
            
            return penaltyRepository.save(penalty);
        }
        throw new RuntimeException("Member or Round not found");
    }

    @Override
    public Optional<Penalty> findPenaltyById(Long id) {
        return penaltyRepository.findById(id);
    }

    @Override
    public List<Penalty> findPenaltiesByMember(Long memberId) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isPresent()) {
            return penaltyRepository.findByMember(memberOpt.get());
        }
        return List.of();
    }

    @Override
    public List<Penalty> findPenaltiesByRound(Long roundId) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (roundOpt.isPresent()) {
            return penaltyRepository.findByRound(roundOpt.get());
        }
        return List.of();
    }

    @Override
    public Penalty updatePenalty(Long id, Penalty penalty) {
        penalty.setId(id);
        return penaltyRepository.save(penalty);
    }

    @Override
    public void deletePenalty(Long id) {
        penaltyRepository.deleteById(id);
    }

    // Utility methods
    @Override
    public BigDecimal calculateTotalContributionsForMember(Long memberId) {
        List<Contribution> contributions = findContributionsByMember(memberId);
        return contributions.stream()
                .map(Contribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalPenaltiesForMember(Long memberId) {
        List<Penalty> penalties = findPenaltiesByMember(memberId);
        return penalties.stream()
                .map(Penalty::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateRemainingAmountForMember(Long memberId) {
        BigDecimal totalContributions = calculateTotalContributionsForMember(memberId);
        BigDecimal totalPenalties = calculateTotalPenaltiesForMember(memberId);
        return totalContributions.subtract(totalPenalties);
    }
}