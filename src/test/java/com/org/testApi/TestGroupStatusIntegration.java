package com.org.testApi;

import com.org.testApi.models.GroupStatus;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.RotationFrequency;
import com.org.testApi.repository.RotatingGroupRepository;
import com.org.testApi.services.GroupStatusManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TestGroupStatusIntegration {

    @Autowired
    private GroupStatusManagementService groupStatusManagementService;

    @Autowired
    private RotatingGroupRepository rotatingGroupRepository;

    @Test
    public void testAutomaticStatusUpdateForCompletedGroup() {
        // Create a group with an end date in the past
        RotatingGroup group = RotatingGroup.builder()
                .name("Test Group")
                .description("Test group for status management")
                .contributionAmount(java.math.BigDecimal.valueOf(100))
                .rotationFrequency(RotationFrequency.MONTHLY)
                .startDate(LocalDate.now().minusMonths(2))
                .endDate(LocalDate.now().minusDays(1)) // Ended yesterday
                .status(GroupStatus.ACTIVE)
                .build();

        RotatingGroup savedGroup = rotatingGroupRepository.save(group);

        // Run the status update
        groupStatusManagementService.updateGroupStatuses();

        // Check that the status was updated to COMPLETED
        Optional<RotatingGroup> updatedGroup = rotatingGroupRepository.findById(savedGroup.getId());
        assertEquals(GroupStatus.COMPLETED, updatedGroup.get().getStatus(), 
            "Group with past end date should be marked as COMPLETED");
    }

    @Test
    public void testAutomaticStatusUpdateForInactiveGroup() {
        // Create a group with no members
        RotatingGroup group = RotatingGroup.builder()
                .name("Empty Group")
                .description("Test group with no members")
                .contributionAmount(java.math.BigDecimal.valueOf(100))
                .rotationFrequency(RotationFrequency.MONTHLY)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusMonths(2))
                .status(GroupStatus.ACTIVE)
                .build();

        // Make sure members list is empty
        group.setMembers(new java.util.ArrayList<>());

        RotatingGroup savedGroup = rotatingGroupRepository.save(group);

        // Run the status update
        groupStatusManagementService.updateGroupStatuses();

        // Check that the status was updated to INACTIVE
        Optional<RotatingGroup> updatedGroup = rotatingGroupRepository.findById(savedGroup.getId());
        assertEquals(GroupStatus.INACTIVE, updatedGroup.get().getStatus(), 
            "Group with no members should be marked as INACTIVE");
    }
}