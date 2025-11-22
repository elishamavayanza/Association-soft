package com.org.testApi;

import com.org.testApi.models.*;
import com.org.testApi.repository.*;
import com.org.testApi.services.RotatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestRoundBeneficiaries {

    @Autowired
    private RotatingService rotatingService;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private RotatingGroupRepository rotatingGroupRepository;
    
    @Autowired
    private RoundRepository roundRepository;

    @Test
    public void testAssignAndRetrieveBeneficiaries() {
        // Create test members
        Member member1Temp = Member.builder()
                .memberCode("TEST001")
                .joinDate(LocalDate.now())
                .build();
        Member member1 = memberRepository.save(member1Temp);
        
        Member member2Temp = Member.builder()
                .memberCode("TEST002")
                .joinDate(LocalDate.now())
                .build();
        Member member2 = memberRepository.save(member2Temp);
        
        // Create a rotating group
        RotatingGroup group = RotatingGroup.builder()
                .name("Test Group")
                .contributionAmount(new BigDecimal("10000"))
                .rotationFrequency(RotationFrequency.MONTHLY)
                .startDate(LocalDate.now())
                .status(GroupStatus.ACTIVE)
                .build();
        group = rotatingGroupRepository.save(group);
        
        // Add members to the group
        group.getMembers().add(member1);
        group.getMembers().add(member2);
        group = rotatingGroupRepository.save(group);
        
        // Create a round
        Round round = Round.builder()
                .roundNumber(1)
                .startDate(LocalDate.now())
                .status(RoundStatus.CURRENT)
                .rotatingGroup(group)
                .build();
        round = roundRepository.save(round);
        
        // Assign beneficiaries to the round
        List<Long> beneficiaryIds = Arrays.asList(member1.getId(), member2.getId());
        Round updatedRound = rotatingService.assignBeneficiariesToRound(round.getId(), beneficiaryIds);
        
        // Verify beneficiaries were assigned
        assertNotNull(updatedRound);
        assertEquals(2, updatedRound.getBeneficiaries().size());
        
        // Retrieve beneficiaries
        List<Member> beneficiaries = rotatingService.getRoundBeneficiaries(round.getId());
        
        // Verify retrieved beneficiaries
        assertNotNull(beneficiaries);
        assertEquals(2, beneficiaries.size());
        assertTrue(beneficiaries.stream().anyMatch(m -> m.getId().equals(member1.getId())));
        assertTrue(beneficiaries.stream().anyMatch(m -> m.getId().equals(member2.getId())));
        
        // Clean up
        roundRepository.delete(round);
        rotatingGroupRepository.delete(group);
        memberRepository.delete(member1);
        memberRepository.delete(member2);
    }
    
    @Test
    public void testDistributeFundsToBeneficiaries() {
        // Create test members
        Member member1Temp = Member.builder()
                .memberCode("TEST003")
                .joinDate(LocalDate.now())
                .build();
        Member member1 = memberRepository.save(member1Temp);
        
        Member member2Temp = Member.builder()
                .memberCode("TEST004")
                .joinDate(LocalDate.now())
                .build();
        Member member2 = memberRepository.save(member2Temp);
        
        // Create a rotating group
        RotatingGroup group = RotatingGroup.builder()
                .name("Test Distribution Group")
                .contributionAmount(new BigDecimal("10000"))
                .rotationFrequency(RotationFrequency.MONTHLY)
                .startDate(LocalDate.now())
                .status(GroupStatus.ACTIVE)
                .build();
        group = rotatingGroupRepository.save(group);
        
        // Add members to the group
        group.getMembers().add(member1);
        group.getMembers().add(member2);
        group = rotatingGroupRepository.save(group);
        
        // Create a round
        Round round = Round.builder()
                .roundNumber(1)
                .startDate(LocalDate.now())
                .status(RoundStatus.CURRENT)
                .rotatingGroup(group)
                .build();
        round = roundRepository.save(round);
        
        // Assign beneficiaries to the round
        List<Long> beneficiaryIds = Arrays.asList(member1.getId(), member2.getId());
        round = rotatingService.assignBeneficiariesToRound(round.getId(), beneficiaryIds);
        
        // Distribute funds to beneficiaries
        Round updatedRound = rotatingService.distributeFundsToBeneficiaries(round.getId());
        
        // Verify distribution
        assertNotNull(updatedRound.getDistributionDate());
        assertNotNull(updatedRound.getTotalAmountDistributed());
        assertNotNull(updatedRound.getAmountPerBeneficiary());
        assertEquals(RoundStatus.COMPLETED, updatedRound.getStatus());
        
        // Clean up
        roundRepository.delete(round);
        rotatingGroupRepository.delete(group);
        memberRepository.delete(member1);
        memberRepository.delete(member2);
    }
}