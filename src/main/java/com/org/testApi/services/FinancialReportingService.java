package com.org.testApi.services;

import com.org.testApi.dto.reports.*;
import com.org.testApi.dto.*;
import com.org.testApi.models.*;
import com.org.testApi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

@Service
public class FinancialReportingService {

    @Autowired
    private RotatingGroupRepository rotatingGroupRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    /**
     * Génère l'historique des contributions individuelles des membres
     * @param groupId ID du groupe de rotation
     * @return Liste des historiques de contribution des membres
     */
    public List<MemberContributionHistoryDTO> generateMemberContributionHistory(Long groupId) {
        RotatingGroup group = rotatingGroupRepository.findById(groupId)
.orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        return group.getMembers().stream()
                .map(this::createMemberContributionHistory)
                .collect(Collectors.toList());
    }

    private MemberContributionHistoryDTO createMemberContributionHistory(Member member) {
        MemberContributionHistoryDTO dto =new MemberContributionHistoryDTO();
        
        // Récupérer toutes les contributions du membre
        List<Contribution> contributions = contributionRepository.findByMember(member);
        List<Penalty> penalties = penaltyRepository.findByMember(member);
        
        BigDecimal totalContributions = contributions.stream()
                .map(Contribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalPenalties = penalties.stream()
                .map(Penalty::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        dto.setMember(convertToMemberDTO(member));
        dto.setContributions(contributions.stream()
.map(this::convertToContributionDTO)
                .collect(Collectors.toList()));
        dto.setPenalties(penalties.stream()
                .map(this::convertToPenaltyDTO)
                .collect(Collectors.toList()));
        dto.setTotalContributions(totalContributions);
        dto.setTotalPenalties(totalPenalties);
       dto.setNetBalance(totalContributions.subtract(totalPenalties));
        
        return dto;
    }

    /**
     * Génère les métriques de performance du groupe
     * @param groupId ID du groupe de rotation
     * @return Métriques de performance du groupe
     */
    public GroupPerformanceMetricsDTO generateGroupPerformanceMetrics(Long groupId) {
        RotatingGroup group = rotatingGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        List<Round> rounds = roundRepository.findByRotatingGroup(group);
        
        // Récupérer toutes les contributions pour tous les tours dugroupe
        List<Contribution> allContributions = rounds.stream()
                .map(contributionRepository::findByRound)
                .flatMap(List::stream)
                .collect(Collectors.toList());
                
        List<Round> completedRounds = rounds.stream()
                .filter(round -> round.getStatus() == RoundStatus.COMPLETED)
.collect(Collectors.toList());

        BigDecimal totalContributions = allContributions.stream()
                .map(Contribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDistributed = rounds.stream()
                .map(Round::getTotalAmountDistributed)
                .filter(amount -> amount !=null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalExpectedContributions = (long) group.getMembers().size() * rounds.size();
        long actualContributions = allContributions.size();
        double contributionRate = totalExpectedContributions > 0 
                ? (double) actualContributions /totalExpectedContributions * 100 
                : 0;

        GroupPerformanceMetricsDTO metrics = new GroupPerformanceMetricsDTO();
        metrics.setGroup(convertToRotatingGroupDTO(group));
        metrics.setTotalMembers((long) group.getMembers().size());
        metrics.setTotalRounds((long) rounds.size());
        metrics.setCompletedRounds((long) completedRounds.size());
        metrics.setTotalContributions(totalContributions);
        metrics.setTotalDistributed(totalDistributed);
        metrics.setAverageContributionPerMember(
                group.getMembers().size() > 0 
                        ? totalContributions.divide(new BigDecimal(group.getMembers().size()), 2, BigDecimal.ROUND_HALF_UP)
                        : BigDecimal.ZERO);
        metrics.setContributionRate(contributionRate);

        return metrics;
    }

    /**
     * Génère les soldes impayés et pénalités par membre
     * @param groupId ID du groupe derotation
     * @return Liste des soldes impayés des membres
     */
    public List<MemberUnpaidBalanceDTO> generateMemberUnpaidBalances(Long groupId) {
        RotatingGroup group = rotatingGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        List<Round> rounds = roundRepository.findByRotatingGroup(group);
        
        return group.getMembers().stream()
                .map(member -> createMemberUnpaidBalance(member, rounds))
                .collect(Collectors.toList());
    }

    private MemberUnpaidBalanceDTO createMemberUnpaidBalance(Member member, List<Round> rounds) {
        MemberUnpaidBalanceDTO dto = new MemberUnpaidBalanceDTO();
BigDecimal expectedContributions = new BigDecimal(rounds.size())
                .multiply(rounds.isEmpty() ? BigDecimal.ZERO : rounds.get(0).getRotatingGroup().getContributionAmount());
        
        List<Contribution> memberContributions = contributionRepository.findByMember(member).stream()
                .filter(contribution -> rounds.contains(contribution.getRound()))
                .collect(Collectors.toList());
        
        BigDecimal actualContributions = memberContributions.stream()
                .map(Contribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<Penalty> memberPenalties = penaltyRepository.findByMember(member).stream()
                .filter(penalty -> rounds.contains(penalty.getRound()))
                .collect(Collectors.toList());
        
        BigDecimal totalPenalties = memberPenalties.stream()
                .map(Penalty::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        dto.setMember(convertToMemberDTO(member));
        dto.setExpectedContributions(expectedContributions);
        dto.setActualContributions(actualContributions);
        dto.setUnpaidBalance(expectedContributions.subtract(actualContributions));
        dto.setTotalPenalties(totalPenalties);
        
        return dto;
    }

    /**
     * Génère l'historique des distributions
     * @paramgroupId ID du groupe de rotation
     * @return Liste de l'historique des distributions
     */
    public List<DistributionHistoryDTO> generateDistributionHistory(Long groupId) {
        RotatingGroup group = rotatingGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        List<Round> completedRounds = roundRepository.findByRotatingGroup(group).stream()
                .filter(round -> round.getStatus() == RoundStatus.COMPLETED)
                .collect(Collectors.toList());
        
        return completedRounds.stream()
                .map(this::createDistributionHistory)
                .collect(Collectors.toList());
    }

    private DistributionHistoryDTO createDistributionHistory(Round round) {
        DistributionHistoryDTO dto = new DistributionHistoryDTO();
        
        dto.setRound(convertToRoundDTO(round));
        dto.setBeneficiaries(round.getBeneficiaries().stream()
                .map(this::convertToMemberDTO)
                .collect(Collectors.toList()));
        dto.setTotalAmountDistributed(round.getTotalAmountDistributed());
        dto.setAmountPerBeneficiary(round.getAmountPerBeneficiary());
        dto.setDistributionDate(round.getDistributionDate());
        
        return dto;
    }

    /**
* Génère lesuivi détaillé de la participation des membres
     * @param groupId ID du groupe de rotation
     * @return Liste du suivi de participation des membres
     */
    public List<MemberParticipationTrackingDTO> generateMemberParticipationTracking(Long groupId) {
        RotatingGroup group = rotatingGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        return group.getMembers().stream()
                .map(member -> createMemberParticipationTracking(member, group))
                .collect(Collectors.toList());
    }

    private MemberParticipationTrackingDTO createMemberParticipationTracking(Member member, RotatingGroup group) {
        MemberParticipationTrackingDTO dto = new MemberParticipationTrackingDTO();
        
        // Récupérer toutes les contributions du membre pour ce groupe
        List<Contribution> memberContributions = contributionRepository.findByMember(member);
        List<Penalty> memberPenalties = penaltyRepository.findByMember(member);
        
        // Filtrer pour ne garder que celles liées à ce groupe
        List<Round> groupRounds = roundRepository.findByRotatingGroup(group);
        
        dto.setMember(convertToMemberDTO(member));
        dto.setContributionHistory(createContributionDetails(memberContributions, groupRounds));
dto.setMissingContributions(createMissingContributions(member, groupRounds));
        dto.setLatePayments(createLatePayments(memberPenalties, groupRounds));
        dto.setStatistics(createParticipationStatistics(member, groupRounds, memberContributions, memberPenalties));
        
        return dto;
    }

    private List<MemberParticipationTrackingDTO.ContributionDetail> createContributionDetails(
            List<Contribution> contributions, List<Round> groupRounds) {
        return contributions.stream()
                .filter(contribution -> groupRounds.contains(contribution.getRound()))
                .map(contribution -> {
                    MemberParticipationTrackingDTO.ContributionDetail detail = new MemberParticipationTrackingDTO.ContributionDetail();
                    detail.setRoundId(contribution.getRound().getId());
                    detail.setRoundNumber(contribution.getRound().getRoundNumber());
                    detail.setAmount(contribution.getAmount());
                    detail.setContributionDate(contribution.getContributionDate());
                    detail.setStatus(contribution.getStatus());
                    return detail;
                })
                .collect(Collectors.toList());
    }

    private List<MemberParticipationTrackingDTO.MissingContribution> createMissingContributions(
            Member member, List<Round> groupRounds) {
        List<MemberParticipationTrackingDTO.MissingContribution> missing = new ArrayList<>();
        
for (Round round : groupRounds) {
            // Vérifier si le membre a contribué à ce tour
            List<Contribution> contributionsForRound = contributionRepository.findByMemberAndRound(member, round);
            
            if (contributionsForRound.isEmpty() || 
                contributionsForRound.stream().noneMatch(c -> c.getStatus() == ContributionStatus.PAID || 
                                                             c.getStatus() == ContributionStatus.LATE)) {
                MemberParticipationTrackingDTO.MissingContribution missingContribution = 
                    new MemberParticipationTrackingDTO.MissingContribution();
                missingContribution.setRoundId(round.getId());
                missingContribution.setRoundNumber(round.getRoundNumber());
                missingContribution.setExpectedAmount(round.getRotatingGroup().getContributionAmount());
                missingContribution.setDueDate(round.getEndDate());
                
                // Déterminer si le tour est terminé et la date réelle
                if (round.getEndDate() !=null && round.getEndDate().isBefore(LocalDate.now())) {
                    missingContribution.setActualDate(null); // Pas encore payé
                    missingContribution.setDaysLate(ChronoUnit.DAYS.between(round.getEndDate(), LocalDate.now()));
                }
                
                missing.add(missingContribution);
            }
        }
        
       return missing;
    }

    private List<MemberParticipationTrackingDTO.LatePayment> createLatePayments(
            List<Penalty> penalties, List<Round> groupRounds) {
        return penalties.stream()
                .filter(penalty -> groupRounds.contains(penalty.getRound()))
                .filter(penalty -> penalty.getPenaltyType() == PenaltyType.LATE_PAYMENT || 
                                  penalty.getPenaltyType() == PenaltyType.MISSED_PAYMENT)
                .map(penalty -> {
                    MemberParticipationTrackingDTO.LatePayment latePayment = new MemberParticipationTrackingDTO.LatePayment();
                    latePayment.setRoundId(penalty.getRound().getId());
                    latePayment.setRoundNumber(penalty.getRound().getRoundNumber());
                    // Montant de la contribution est déduit du groupe
                    latePayment.setAmount(penalty.getRound().getRotatingGroup().getContributionAmount());
                    latePayment.setPenaltyAmount(penalty.getAmount());
                    latePayment.setDueDate(penalty.getRound().getEndDate());
                    latePayment.setPaidDate(penalty.getCreatedDate().toLocalDate()); // Approximation
                    latePayment.setPenaltyType(penalty.getPenaltyType());
                    
                    if (penalty.getRound().getEndDate() != null && penalty.getCreatedDate() != null) {
                        latePayment.setDaysLate(ChronoUnit.DAYS.between(
                            penalty.getRound().getEndDate(), 
                            penalty.getCreatedDate().toLocalDate()));
                    }
                    
                    return latePayment;
                })
                .collect(Collectors.toList());
    }

    private MemberParticipationTrackingDTO.ParticipationStatistics createParticipationStatistics(
            Member member, List<Round> groupRounds, List<Contribution> contributions, List<Penalty> penalties) {
        
        MemberParticipationTrackingDTO.ParticipationStatistics stats = new MemberParticipationTrackingDTO.ParticipationStatistics();
        
        long totalExpected = groupRounds.size();
        long totalActual = contributions.stream()
                .filter(c -> groupRounds.contains(c.getRound()))
                .filter(c -> c.getStatus() == ContributionStatus.PAID || c.getStatus() == ContributionStatus.LATE)
                .count();
        
        long totalMissed = contributions.stream()
                .filter(c -> groupRounds.contains(c.getRound()))
                .filter(c -> c.getStatus() == ContributionStatus.PENDING)
                .count();
        
        long totalLate = contributions.stream()
                .filter(c -> groupRounds.contains(c.getRound()))
                .filter(c ->c.getStatus() == ContributionStatus.LATE)
                .count();
        
        BigDecimal totalContributed = contributions.stream()
                .filter(c -> groupRounds.contains(c.getRound()))
                .filter(c -> c.getStatus() == ContributionStatus.PAID || c.getStatus() == ContributionStatus.LATE)
                .map(Contribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalPenalties = penalties.stream()
                .filter(p -> groupRounds.contains(p.getRound()))
                .map(Penalty::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.setTotalExpectedContributions(totalExpected);
        stats.setTotalActualContributions(totalActual);
        stats.setTotalMissedContributions(totalMissed);
        stats.setTotalLateContributions(totalLate);
        stats.setTotalContributedAmount(totalContributed);
        stats.setTotalPenaltyAmount(totalPenalties);
        
        if (totalExpected > 0) {
            stats.setParticipationRate((double) totalActual / totalExpected * 100);
            stats.setPunctualityRate((double) (totalActual - totalLate) / totalExpected * 100);
        }
        
        return stats;
    }

    // Méthodes utilitaires de conversion
    private MemberDTO convertToMemberDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .memberCode(member.getMemberCode())
                .build();
    }

    private ContributionDTO convertToContributionDTO(Contribution contribution) {
        return ContributionDTO.builder()
                .id(contribution.getId())
                .amount(contribution.getAmount())
                .contributionDate(contribution.getContributionDate())
                .status(contribution.getStatus() != null ? contribution.getStatus().name() : null)
                .build();
    }

    private PenaltyDTO convertToPenaltyDTO(Penalty penalty) {
        return PenaltyDTO.builder()
                .id(penalty.getId())
                .amount(penalty.getAmount())
                .reason(penalty.getReason())
                .penaltyType(penalty.getPenaltyType() != null ? penalty.getPenaltyType().name() : null)
                .penaltyDate(penalty.getPenaltyDate())
                .status(penalty.getStatus() != null ? penalty.getStatus().name() : null)
                .build();
    }

    private RotatingGroupDTO convertToRotatingGroupDTO(RotatingGroup group) {
        return RotatingGroupDTO.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .contributionAmount(group.getContributionAmount())
                .rotationFrequency(group.getRotationFrequency() != null ? group.getRotationFrequency().name() : null)
                .status(group.getStatus() != null ? group.getStatus().name() : null)
               .build();
    }

    private RoundDTO convertToRoundDTO(Round round) {
        return RoundDTO.builder()
                .id(round.getId())
                .roundNumber(round.getRoundNumber())
                .startDate(round.getStartDate())
                .endDate(round.getEndDate())
                .status(round.getStatus() != null ? round.getStatus().name() : null)
                .build();
    }
}