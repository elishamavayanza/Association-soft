package com.org.testApi.services;

import com.org.testApi.models.*;
import com.org.testApi.repository.*;
import com.org.testApi.services.GroupStatusManagementService;
import com.org.testApi.services.NotificationServiceImpl;
import com.org.testApi.services.RotatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
    
    @Autowired
    private NotificationServiceImpl notificationService;
    
    @Autowired
    private GroupStatusManagementService groupStatusManagementService;

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
                .autoGenerateRounds(Boolean.TRUE)
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

    @Override
    public RotatingGroup addMembersToGroup(Long groupId, List<Long> memberIds) {
        // Find the rotating group
        Optional<RotatingGroup> groupOpt = rotatingGroupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            throw new RuntimeException("Groupe de rotation non trouvé avec l'ID : " + groupId);
        }
        
        RotatingGroup group = groupOpt.get();
        
        // Check if the group has reached maximum members
        if (group.getMaxMembers() != null && 
            (group.getMembers().size() + memberIds.size()) > group.getMaxMembers()) {
            throw new RuntimeException("Impossible d'ajouter des membres : le nombre maximum de membres serait dépassé");
        }
        
        // Find all members by their IDs
        List<Member> membersToAdd = memberRepository.findAllById(memberIds);
        
        // Check if all members were found
        if (membersToAdd.size() != memberIds.size()) {
            List<Long> foundMemberIds = membersToAdd.stream()
                    .map(Member::getId)
                    .collect(Collectors.toList());
            List<Long> notFoundMemberIds = memberIds.stream()
                    .filter(id -> !foundMemberIds.contains(id))
                    .collect(Collectors.toList());
            throw new RuntimeException("Certains membres n'ont pas été trouvés : " + notFoundMemberIds);
        }
        
        // Add members to the group
        group.getMembers().addAll(membersToAdd);
        
        // Save the updated group
        RotatingGroup savedGroup = rotatingGroupRepository.save(group);
        
        // Update group status based on new member count
        GroupStatus newStatus = groupStatusManagementService.determineGroupStatus(savedGroup);
        if (newStatus != savedGroup.getStatus()) {
            savedGroup.setStatus(newStatus);
            savedGroup = rotatingGroupRepository.save(savedGroup);
        }
        
        // If auto-generate is enabled, recreate the rounds
        if (savedGroup.getAutoGenerateRounds() != null && savedGroup.getAutoGenerateRounds()) {
            createAutomaticRounds(savedGroup);
        }
        
        return savedGroup;
    }

    @Override
    public RotatingGroup removeMembersFromGroup(Long groupId, List<Long> memberIds) {
        // Find the rotating group
        Optional<RotatingGroup> groupOpt = rotatingGroupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            throw new RuntimeException("Groupe de rotation non trouvé avec l'ID: " + groupId);
        }
        
        RotatingGroup group = groupOpt.get();
        
        // Find members to remove by their IDs
        List<Member> membersToRemove = memberRepository.findAllById(memberIds);
        
        // Check if all members were found
        if (membersToRemove.size() != memberIds.size()) {
            List<Long> foundMemberIds = membersToRemove.stream()
                    .map(Member::getId)
                    .collect(Collectors.toList());
            List<Long> notFoundMemberIds = memberIds.stream()
                    .filter(id -> !foundMemberIds.contains(id))
                    .collect(Collectors.toList());
            throw new RuntimeException("Certains membres n'ont pas été trouvés : " + notFoundMemberIds);
        }
        
        // Remove members from the group
        group.getMembers().removeAll(membersToRemove);
        
        // Save the updated group
        RotatingGroup savedGroup = rotatingGroupRepository.save(group);
        
        // Update group status based on new member count
        GroupStatus newStatus = groupStatusManagementService.determineGroupStatus(savedGroup);
        if (newStatus != savedGroup.getStatus()) {
            savedGroup.setStatus(newStatus);
            savedGroup = rotatingGroupRepository.save(savedGroup);
        }
        
        // If auto-generate is enabled, recreate the rounds
        if (savedGroup.getAutoGenerateRounds() != null && savedGroup.getAutoGenerateRounds()) {
            createAutomaticRounds(savedGroup);
        }
        
        return savedGroup;
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
    public void deleteRound(Long id){
        roundRepository.deleteById(id);
    }

    // Contribution methods
    @Override
    public Contribution makeContribution(Long memberId, Long roundId, BigDecimal amount, LocalDate contributionDate) {
        if (memberId == null || roundId == null || amount == null || contributionDate == null) {
            throw new IllegalArgumentException("Tous les paramètres sont requis : memberId, roundId, amount, contributionDate");
        }
        
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        // Use the new method to fetch round with rotating group and members
        Optional<Round> roundOpt = roundRepository.findWithRotatingGroupAndMembersById(roundId);
        
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("Membre non trouvé avec l'ID : " + memberId);
        }
        
        if (!roundOpt.isPresent()) {
            throw new RuntimeException("Tour non trouvé avec l'ID : " + roundId);
        }
        
        Member member = memberOpt.get();
        Round round = roundOpt.get();
        RotatingGroup group = round.getRotatingGroup();
        
        // Check if member belongs to the rotating group
        // Fix: properly check if member belongs to the rotating group
        boolean isMemberInGroup = group.getMembers().stream()
                .anyMatch(m -> m.getId().equals(memberId));
        if (!isMemberInGroup) {
            throw new RuntimeException("Le membre n'appartient pas au groupe de rotation");
        }
        
        // Check if contribution already exists for this member and round
        List<Contribution> existingContributions = contributionRepository.findByMemberAndRound(member, round);
        if (!existingContributions.isEmpty()) {
            throw new RuntimeException("Une contribution existe déjà pour ce membre et ce tour");
        }
        
        Contribution contribution = Contribution.builder()
                .amount(amount)
                .contributionDate(contributionDate)
                .status(ContributionStatus.PAID)
                .member(member)
                .round(round)
                .build();
        
        return contributionRepository.save(contribution);
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
    public List<Penalty> findPenaltiesByRound(Long roundId){
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

    // Beneficiary methods for Likelemba system
    @Override
    public Round assignBeneficiariesToRound(Long roundId, List<Long> beneficiaryIds) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (!roundOpt.isPresent()) {
            throw new RuntimeException("Tour non trouvé avec l'ID : " + roundId);
        }
        
        Round round = roundOpt.get();
        
        // Find all beneficiaries by their IDs
        List<Member> beneficiaries = memberRepository.findAllById(beneficiaryIds);
        
        // Check if all beneficiaries were found
        if (beneficiaries.size() != beneficiaryIds.size()) {
            List<Long> foundBeneficiaryIds = beneficiaries.stream()
                    .map(Member::getId)
                    .collect(Collectors.toList());
            List<Long> notFoundBeneficiaryIds = beneficiaryIds.stream()
                    .filter(id -> !foundBeneficiaryIds.contains(id))
                    .collect(Collectors.toList());
            throw new RuntimeException("Certains bénéficiaires n'ont pas été trouvés : " + notFoundBeneficiaryIds);
        }
        
        // Validate that beneficiaries belong to the rotating group
        RotatingGroup group = round.getRotatingGroup();
        for (Member beneficiary : beneficiaries) {
            boolean isMemberInGroup = group.getMembers().stream()
                    .anyMatch(m -> m.getId().equals(beneficiary.getId()));
            if (!isMemberInGroup) {
                throw new RuntimeException("Le membre " + beneficiary.getId() + " n'appartient pas au groupe de rotation");
            }
        }
        
        // Assign beneficiaries to the round
        round.getBeneficiaries().clear();
        round.getBeneficiaries().addAll(beneficiaries);
        
        // Calculate amount per beneficiary
        if (!beneficiaries.isEmpty()) {
            BigDecimal totalAmount = calculateTotalRoundContributions(roundId);
            round.setTotalAmountDistributed(totalAmount);
            round.setAmountPerBeneficiary(totalAmount.divide(new BigDecimal(beneficiaries.size()), 2, BigDecimal.ROUND_HALF_UP));
        }
        
        // Save and return the updated round
        return roundRepository.save(round);
    }
    
    @Override
    public List<Member> getRoundBeneficiaries(Long roundId) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (!roundOpt.isPresent()) {
            throw new RuntimeException("Tour non trouvé avec l'ID : " + roundId);
        }
        
        return roundOpt.get().getBeneficiaries();
    }
    
    /**
     * Select beneficiaries for a round using a fair rotation algorithm.
     * This method ensures that each member gets a turn in a predetermined order.
     * 
     * @param roundId The ID of the round to select beneficiaries for
     * @return The updated round with beneficiaries assigned
     */
    @Override
    public Round selectBeneficiariesAutomatically(Long roundId) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (!roundOpt.isPresent()) {
            throw new RuntimeException("Tour non trouvé avec l'ID: " + roundId);
        }
        
        Round round = roundOpt.get();
        RotatingGroup group = round.getRotatingGroup();
        
        // Get all active members of the rotating group
        List<Member> members = group.getMembers();
        if (members.isEmpty()) {
            throw new RuntimeException("Aucun membre dans le groupe de rotation");
        }
        
        // Sort members by ID to ensure consistent ordering
        members.sort((m1, m2) -> m1.getId().compareTo(m2.getId()));
        
        // Determine beneficiaries based on round number and fair rotation algorithm
        List<Member> beneficiaries = determineBeneficiariesByFairRotation(members, round);
        
        // Assign beneficiaries to the round
        round.getBeneficiaries().clear();
        round.getBeneficiaries().addAll(beneficiaries);
        
        // Calculate amount per beneficiary
        if (!beneficiaries.isEmpty()) {
            BigDecimal totalAmount = calculateTotalRoundContributions(roundId);
            round.setTotalAmountDistributed(totalAmount);
            round.setAmountPerBeneficiary(totalAmount.divide(new BigDecimal(beneficiaries.size()), 2, BigDecimal.ROUND_HALF_UP));
        }
        
        return roundRepository.save(round);
    }
    
    /**
     * Determine beneficiaries using a fair rotation algorithm.
     * This algorithm ensures that each member gets a turn in a predetermined order.
     * 
     * @param members List of all members in the rotating group
     * @param round The round for which beneficiaries are being determined
     * @return List of beneficiaries for this round
     */
    private List<Member> determineBeneficiariesByFairRotation(List<Member> members, Round round) {
        int roundNumber = round.getRoundNumber();
        int totalMembers = members.size();
        
        // Simple rotation: each round benefits the next member in line
        // Member index is determined by (round number - 1) modulo total members
        int beneficiaryIndex = (roundNumber - 1) % totalMembers;
        Member beneficiary = members.get(beneficiaryIndex);
        
        List<Member> beneficiaries = new ArrayList<>();
        beneficiaries.add(beneficiary);
        
        return beneficiaries;
    }
    
    /**
     * Enhanced fair selection algorithm that considers member history.
     * This method prioritizes members who have not received funds recently.
     * 
     * @param rotatingGroupId The ID of the rotating group
     * @param numberOfBeneficiaries Number of beneficiaries to select
     * @return List of selected beneficiaries
     */
    public List<Member> selectBeneficiariesWithHistory(Long rotatingGroupId, int numberOfBeneficiaries) {
        Optional<RotatingGroup> groupOpt = rotatingGroupRepository.findById(rotatingGroupId);
        if (!groupOpt.isPresent()) {
            throw new RuntimeException("Groupe de rotation non trouvé avec l'ID: " + rotatingGroupId);
        }
        
        RotatingGroup group = groupOpt.get();
        List<Member> members = new ArrayList<>(group.getMembers());
        
        if (members.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Sort members by ID to ensure consistent ordering
        members.sort((m1, m2) -> m1.getId().compareTo(m2.getId()));
        
        // Get the recent beneficiaries from previous rounds
        List<Round> previousRounds = roundRepository.findByRotatingGroupOrderByRoundNumberDesc(group);
        List<Member> recentBeneficiaries = new ArrayList<>();
        
        // Consider beneficiaries from the last N rounds (where N = number of members)
        int roundsToConsider = Math.min(members.size(), previousRounds.size());
        for (int i = 0; i < roundsToConsider; i++) {
            recentBeneficiaries.addAll(previousRounds.get(i).getBeneficiaries());
        }
        
        // Create a list of candidates weighted by how recently they received funds
        List<Member> candidates = new ArrayList<>();
        for (Member member : members) {
            // Count how many times this member appears in recent beneficiaries
            long recentCount = recentBeneficiaries.stream()
                    .filter(m -> m.getId().equals(member.getId()))
                    .count();
            
            // Add member multiple times inversely proportional to their recent selection count
            // Members not recently selected get added more times, increasing their chances
            int weight = Math.max(1, members.size() - (int) recentCount);
            for (int i = 0; i < weight; i++) {
                candidates.add(member);
            }
        }
        
        // Shuffle candidates and select unique beneficiaries
        List<Member> selectedBeneficiaries = new ArrayList<>();
        while (selectedBeneficiaries.size() < numberOfBeneficiaries && !candidates.isEmpty()) {
            // Pick a random candidate
            Member selected = candidates.get((int) (Math.random() * candidates.size()));
            
            // Add to beneficiaries if not already selected
            if (!selectedBeneficiaries.contains(selected)) {
                selectedBeneficiaries.add(selected);
            }
            
            // Remove all instances of this member from candidates
            candidates.removeIf(m -> m.getId().equals(selected.getId()));
        }
        
        return selectedBeneficiaries;
    }
    
    @Override
    public Round distributeFundsToBeneficiaries(Long roundId) {
        Optional<Round> roundOpt = roundRepository.findById(roundId);
        if (!roundOpt.isPresent()) {
            throw new RuntimeException("Tour non trouvé avec l'ID: " + roundId);
        }
        
        Round round = roundOpt.get();
        
        // Set distribution date
        round.setDistributionDate(LocalDateTime.now());
        
        // Calculate total amount
        BigDecimal totalAmount = calculateTotalRoundContributions(roundId);
        round.setTotalAmountDistributed(totalAmount);
        
        // Calculate amount per beneficiary
        if (!round.getBeneficiaries().isEmpty()) {
            round.setAmountPerBeneficiary(totalAmount.divide(
                new BigDecimal(round.getBeneficiaries().size()), 2, BigDecimal.ROUND_HALF_UP));
        }
        
        // Send notifications to beneficiaries
        sendDistributionNotifications(round);
        
        // Update round status
        round.setStatus(RoundStatus.COMPLETED);
        
        // Save and return the updated round
        return roundRepository.save(round);
    }
    
    private BigDecimal calculateTotalRoundContributions(Long roundId) {
        List<Contribution> contributions = findContributionsByRound(roundId);
        return contributions.stream()
                .map(Contribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private void sendDistributionNotifications(Round round) {
        String subject = "Distribution de fonds - Groupe de rotation: " + round.getRotatingGroup().getName();
        String message = String.format(
                "Bonjour,\n\n" +
                "Vous recevez ce message pour vous informer que la distribution des fonds du tour #%d " +
                "du groupe \"%s\" a été effectuée.\n" +
                "Montant total distribué: %s\n" +
                "Votre part: %s\n" +
                "Date de distribution: %s\n\n" +
                "Cordialement,\nL'équipe Likelemba",
                round.getRoundNumber(),
                round.getRotatingGroup().getName(),
                round.getTotalAmountDistributed() != null ? round.getTotalAmountDistributed().toString() : "N/A",
                round.getAmountPerBeneficiary() != null ? round.getAmountPerBeneficiary().toString() : "N/A",
                round.getDistributionDate() != null ? round.getDistributionDate().toString() : "N/A");
        
        for (Member beneficiary : round.getBeneficiaries()) {
            // Send email notification
            if (beneficiary.getUser() != null && beneficiary.getUser().getEmail() != null) {
                notificationService.sendEmailNotification(
                        beneficiary.getUser().getEmail(),
                        subject,
                        message);
            }
            
            // Send SMS notification (if phone number is available)
            if (beneficiary.getUser() != null && beneficiary.getUser().getPhoneNumber() != null) {
                notificationService.sendSmsNotification(
                        beneficiary.getUser().getPhoneNumber(),
                        message);
            }
        }
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
    
    @Override
    public void generateRoundsForGroup(Long groupId) {
        Optional<RotatingGroup> groupOpt = rotatingGroupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            throw new RuntimeException("Rotating group not found with id: " + groupId);
        }
        
        RotatingGroup group = groupOpt.get();
        createAutomaticRounds(group);
    }
    
    @Override
    public List<Round> createAutomaticRounds(RotatingGroup group) {
        List<Round> createdRounds = new ArrayList<>();
        
        // Determine the number of rounds based on group members count
        int numberOfRounds = group.getMembers().size();
        if (numberOfRounds <= 0) {
            return createdRounds; // No members, no rounds to create
        }
        
        LocalDate currentDate = group.getStartDate();
        RotationFrequency frequency = group.getRotationFrequency();
        
        // Delete existing rounds if any
        if (!group.getRounds().isEmpty()) {
            roundRepository.deleteAll(group.getRounds());
            group.getRounds().clear();
        }
        
        for (int i = 1; i <= numberOfRounds; i++) {
            LocalDate startDate = currentDate;
            LocalDate endDate = calculateEndDate(currentDate, frequency);
            
            Round round = Round.builder()
                    .roundNumber(i)
                    .startDate(startDate)
                    .endDate(endDate)
                    .status(RoundStatus.UPCOMING)
                    .rotatingGroup(group)
                    .build();
            
            Round savedRound = roundRepository.save(round);
            createdRounds.add(savedRound);
            
            // Move to the next period
            currentDate = endDate.plusDays(1);
        }
        
        // Update group end date based on last round
        if (!createdRounds.isEmpty()) {
            Round lastRound = createdRounds.get(createdRounds.size() - 1);
            group.setEndDate(lastRound.getEndDate());
            rotatingGroupRepository.save(group);
        }
        
        return createdRounds;
    }
    
    private LocalDate calculateEndDate(LocalDate startDate, RotationFrequency frequency) {
        switch (frequency) {
            case DAILY:
                return startDate;
            case WEEKLY:
                return startDate.plusDays(6); // Week is 7 days, so end date is 6 days after start
            case MONTHLY:
                return startDate.plusMonths(1).minusDays(1); // Last day of the month
            case QUARTERLY:
                return startDate.plusMonths(3).minusDays(1); // Last day of the quarter
            case YEARLY:
                return startDate.plusYears(1).minusDays(1); // Last day of the year
            default:
                return startDate.plusMonths(1).minusDays(1); // Default to monthly
        }
    }
    
    @Override
    public RotatingGroup setAutoGenerateRounds(Long groupId, Boolean autoGenerate) {
        Optional<RotatingGroup> groupOpt = rotatingGroupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            throw new RuntimeException("Rotating group not found with id: " + groupId);
        }
        
        RotatingGroup group = groupOpt.get();
        group.setAutoGenerateRounds(autoGenerate);
        return rotatingGroupRepository.save(group);
    }
}