package com.org.testApi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class PenaltySchedulerService {
    
    @Autowired
    private AutomaticPenaltyService automaticPenaltyService;
    
    /**
     * Tâche planifiée qui s'exécute tous les jours à minuit pour appliquer automatiquement les pénalités.
     * 
     * Exemple de cron expression:
     * "0 0 0 * * ?" - Tous les jours à 00:00:00
     * "0 0 0 * * SUN" - Tous les dimanches à 00:00:00
     * "0 0 0 1 * ?" - Le 1er de chaque mois à 00:00:00
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void applyDailyPenalties() {
        LocalDate today = LocalDate.now();
        int appliedCount = automaticPenaltyService.applyAutomaticPenalties(today);
        
        // Logguer le nombre de pénalités appliquées
        System.out.println("Pénalités automatiques appliquées le " + today + " : " + appliedCount + " prêts");
    }
}