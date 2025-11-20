package com.org.testApi.services;

import com.org.testApi.models.Loan;
import com.org.testApi.models.LoanType;
import com.org.testApi.models.LoanPenaltyConfig;
import com.org.testApi.models.PenaltyCalculation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanPenaltyService {
    
    /**
     * Calcule les pénalités pour un prêt en retard.
     *
     * @param loan le prêt
     * @return le calcul de pénalité
     */
    PenaltyCalculation calculatePenalty(Loan loan);
    
    /**
     * Applique les pénalités à un prêt.
     *
     * @param loan le prêt
     * @param penaltyCalculation le calcul de pénalité
     * @return le prêt mis à jour
     */
    Loan applyPenalty(Loan loan, PenaltyCalculation penaltyCalculation);
    
    /**
     * Crée une configuration de pénalité pour un type de prêt.
     *
     * @param penaltyConfig la configuration de pénalité
     * @return la configuration de pénalité créée
     */
    LoanPenaltyConfig createPenaltyConfig(LoanPenaltyConfig penaltyConfig);
    
    /**
     * Met à jour une configuration de pénalité.
     *
     * @param id l'identifiant de la configuration
     * @param penaltyConfig la configuration mise à jour
     * @return la configuration mise à jour
     */
    LoanPenaltyConfig updatePenaltyConfig(Long id, LoanPenaltyConfig penaltyConfig);
    
    /**
     * Trouve la configuration de pénalité par type de prêt.
     *
     * @param loanType le type de prêt
     * @return la configuration de pénalité
     */
    Optional<LoanPenaltyConfig> findPenaltyConfigByLoanType(LoanType loanType);
    
    /**
     * Trouve tous les calculs de pénalité pour un prêt.
     *
     * @param loan le prêt
     * @return la liste des calculs de pénalité
     */
    List<PenaltyCalculation> findPenaltyCalculationsByLoan(Loan loan);
    
    /**
     * Trouve le montant total des pénalités pour un prêt.
     *
     * @param loan le prêt
     * @return le montant total des pénalités
     */
    BigDecimal calculateTotalPenalties(Loan loan);
    
    /**
     * Vérifie si un prêt est éligible aux pénalités.
     *
     * @param loan le prêt
     * @return true si le prêt est éligible aux pénalités
     */
    boolean isEligibleForPenalty(Loan loan);
    
    /**
     * Trouve les prêts éligibles aux pénalités.
     *
     * @param date la date de référence
     * @return la liste des prêts éligibles
     */
    List<Loan> findLoansEligibleForPenalty(LocalDate date);
}