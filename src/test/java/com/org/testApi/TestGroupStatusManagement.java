package com.org.testApi;

import com.org.testApi.models.GroupStatus;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.RotationFrequency;
import com.org.testApi.services.GroupStatusManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestGroupStatusManagement {

    @Autowired
    private GroupStatusManagementService groupStatusManagementService;

    @Test
    public void testGroupWithNoMembersShouldBeInactive() {
        RotatingGroup group = new RotatingGroup();
        group.setMembers(java.util.Collections.emptyList());
        group.setStartDate(LocalDate.now().minusDays(10));
        group.setEndDate(LocalDate.now().plusDays(10));

        GroupStatus status = groupStatusManagementService.determineGroupStatus(group);
        assertEquals(GroupStatus.INACTIVE, status, "Group with no members should be INACTIVE");
    }

    @Test
    public void testGroupWithPastEndDateShouldBeCompleted() {
        RotatingGroup group = new RotatingGroup();
        group.setMembers(java.util.Arrays.asList(new com.org.testApi.models.Member()));
        group.setStartDate(LocalDate.now().minusDays(20));
        group.setEndDate(LocalDate.now().minusDays(10));

        GroupStatus status = groupStatusManagementService.determineGroupStatus(group);
        assertEquals(GroupStatus.COMPLETED, status, "Group with past end date should be COMPLETED");
    }

    @Test
    public void testActiveGroupWithMembersAndFutureEndDate() {
        RotatingGroup group = new RotatingGroup();
        group.setMembers(java.util.Arrays.asList(new com.org.testApi.models.Member()));
        group.setStartDate(LocalDate.now().minusDays(10));
        group.setEndDate(LocalDate.now().plusDays(10));

        GroupStatus status = groupStatusManagementService.determineGroupStatus(group);
        assertEquals(GroupStatus.ACTIVE, status, "Group with members and future end date should be ACTIVE");
    }
}