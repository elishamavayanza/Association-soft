package com.org.testApi.services;

import com.org.testApi.models.Loan;
import com.org.testApi.models.PenaltyCalculation;
import java.time.LocalDate;
import java.util.List;

public interface AutomaticPenaltyService {
    
    /**
     * Applique automatiquement les pénalités aux prêts éligibles.
     *
     * @param date la date de référence pour le calcul des pénalités
     * @return le nombre de prêts auxquels des pénalités ont été appliquées
     */
    int applyAutomaticPenalties(LocalDate date);
    
    /**
     * Trouve les prêts éligibles aux pénalités à une date donnée.
     *
     * @param date la date de référence
     * @return la liste des prêts éligibles
     */
    List<Loan> findEligibleLoans(LocalDate date);
    
    /**
     * Calcule et applique les pénalités pour un prêt spécifique.
     *
     * @param loan le prêt
     * @return le calcul de pénalité s'il a été appliqué, null sinon
     */
    PenaltyCalculation calculateAndApplyPenalty(Loan loan);
}