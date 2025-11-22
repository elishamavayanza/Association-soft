package com.org.testApi;

import com.org.testApi.models.Member;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.Round;
import com.org.testApi.models.RotationFrequency;
import com.org.testApi.models.GroupStatus;
import com.org.testApi.repository.MemberRepository;
import com.org.testApi.repository.RotatingGroupRepository;
import com.org.testApi.repository.RoundRepository;
import com.org.testApi.services.RotatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestFairBeneficiarySelection {

    @Autowired
    private RotatingService rotatingService;

    @Autowired
    private RotatingGroupRepository rotatingGroupRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Test
    public void testFairBeneficiarySelection() {
        // Create a rotating group
        RotatingGroup group = new RotatingGroup();
        group.setName("Test Group");
        group.setDescription("Test group for fair beneficiary selection");
        group.setContributionAmount(new BigDecimal("100.00"));
        group.setMaxMembers(5);
        group.setRotationFrequency(RotationFrequency.MONTHLY);
        group.setStartDate(LocalDate.now());
        group.setStatus(GroupStatus.ACTIVE);
        group.setAutoGenerateRounds(true);

        group = rotatingGroupRepository.save(group);

        // Create some test members
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Member member = new Member();
            member.setMemberCode("TEST-" + i);
            // Note: In a real test, you would set the user and association
            members.add(member);
        }

        members = memberRepository.saveAll(members);

        // Add members to the group
        List<Long> memberIds = new ArrayList<>();
        for (Member member : members) {
            memberIds.add(member.getId());
        }

        rotatingService.addMembersToGroup(group.getId(), memberIds);

        // Refresh group to get updated members
        Optional<RotatingGroup> updatedGroupOpt = rotatingGroupRepository.findById(group.getId());
        assertTrue(updatedGroupOpt.isPresent());
        RotatingGroup updatedGroup = updatedGroupOpt.get();

        // Generate rounds
        List<Round> rounds = rotatingService.createAutomaticRounds(updatedGroup);

        // Test automatic beneficiary selection for each round
        for (int i = 0; i < rounds.size(); i++) {
            Round round = rounds.get(i);
            Round updatedRound = rotatingService.selectBeneficiariesAutomatically(round.getId());
            
            // Check that exactly one beneficiary is selected
            assertEquals(1, updatedRound.getBeneficiaries().size());
            
            // Check that the beneficiary is one of the group members
            Member beneficiary = updatedRound.getBeneficiaries().get(0);
            boolean isMemberInGroup = false;
            for (Member member : updatedGroup.getMembers()) {
                if (member.getId().equals(beneficiary.getId())) {
                    isMemberInGroup = true;
                    break;
                }
            }
            assertTrue(isMemberInGroup, "Beneficiary should be a member of the rotating group");
        }

        // Test that beneficiaries rotate fairly
        Member firstRoundBeneficiary = rounds.get(0).getBeneficiaries().get(0);
        Member secondRoundBeneficiary = rounds.get(1).getBeneficiaries().get(0);
        
        // With our simple rotation algorithm, these should be different members
        assertNotEquals(firstRoundBeneficiary.getId(), secondRoundBeneficiary.getId(), 
            "Beneficiaries should rotate between rounds");
    }
}