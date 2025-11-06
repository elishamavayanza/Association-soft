package com.org.testApi.services;

import com.org.testApi.models.GroupStatus;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.Round;
import com.org.testApi.repository.RotatingGroupRepository;
import com.org.testApi.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupStatusManagementService {

    @Autowired
    private RotatingGroupRepository rotatingGroupRepository;

    @Autowired
    private RoundRepository roundRepository;

    /**
     * Scheduled task that runs daily to update group statuses based on defined criteria
     */
    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void updateGroupStatuses() {
        List<RotatingGroup> allGroups = rotatingGroupRepository.findAll();

        for (RotatingGroup group : allGroups) {
            GroupStatus newStatus = determineGroupStatus(group);
            if (newStatus != group.getStatus()) {
                group.setStatus(newStatus);
                rotatingGroupRepository.save(group);
            }
        }
    }

    /**
     * Determine the appropriate status for a group based on the defined criteria
     *
     * @param group The rotating group to evaluate
     * @return The appropriate GroupStatus
     */
    public GroupStatus determineGroupStatus(RotatingGroup group) {
        // Check if the group has no members
        if (group.getMembers().isEmpty()) {
            return GroupStatus.INACTIVE;
        }

        // Check if the end date has passed
        if (group.getEndDate() != null && group.getEndDate().isBefore(LocalDate.now())) {
            return GroupStatus.COMPLETED;
        }

        // Check for inactivity - if no rounds have been updated in the last 30 days
        if (isGroupInactive(group)) {
            return GroupStatus.SUSPENDED;
        }

        // If none of the above conditions apply, the group is active
        return GroupStatus.ACTIVE;
    }

    /**
     * Check if a group has been inactive for a certain period
     *
     * @param group The rotating group to check
     * @return true if the group is inactive, false otherwise
     */
    private boolean isGroupInactive(RotatingGroup group) {
        // Find the most recently updated round for this group
        List<Round> rounds = roundRepository.findByRotatingGroupOrderByLastModifiedDateDesc(group);

        if (rounds.isEmpty()) {
            // If there are no rounds, check when the group was created
            LocalDateTime createdAt = group.getCreatedDate();
            return createdAt != null && createdAt.isBefore(LocalDateTime.now().minusDays(30));
        } else {
            // Check if the most recent round was updated more than 30 days ago
            Round mostRecentRound = rounds.get(0);
            LocalDateTime lastUpdated = mostRecentRound.getLastModifiedDate();
            return lastUpdated != null && lastUpdated.isBefore(LocalDateTime.now().minusDays(30));
        }
    }
}