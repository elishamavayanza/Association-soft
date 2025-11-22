package com.org.testApi.services;

import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class AutomaticRoundGenerationService {
    
    private static final Logger logger = Logger.getLogger(AutomaticRoundGenerationService.class.getName());
    
    @Autowired
    private RotatingService rotatingService;
    
    /**
     * Scheduled task to automatically generate rounds for active rotating groups
     * Runs every day at midnight (00:00)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateRoundsForAllActiveGroups() {
        logger.info("Starting automatic round generation for active rotating groups");
        
        try {
            List<RotatingGroup> activeGroups = rotatingService.findActiveRotatingGroups();
            logger.info("Found " + activeGroups.size() + " active rotating groups");
            
            for (RotatingGroup group : activeGroups) {
                try {
                    // Check if automatic round generation is enabled for this group
                    if (group.getAutoGenerateRounds() != null && !group.getAutoGenerateRounds()) {
                        logger.info("Automatic round generation disabled for group: " + group.getName() + " (ID: " + group.getId() + ")");
                        continue;
                    }
                    
                    // Check if the group already has rounds
                    List<Round> existingRounds = rotatingService.findRoundsByRotatingGroup(group.getId());
                    
                    // If no rounds exist, generate them
                    if (existingRounds.isEmpty()) {
                        logger.info("Generating rounds for group: " + group.getName() + " (ID: " + group.getId() + ")");
                        rotatingService.generateRoundsForGroup(group.getId());
                    }
                } catch (Exception e) {
                    logger.severe("Error generating rounds for group " + group.getId() + ": " + e.getMessage());
                }
            }
            
            logger.info("Automatic round generation completed");
        } catch (Exception e) {
            logger.severe("Error in automatic round generation: " + e.getMessage());
        }
    }
}