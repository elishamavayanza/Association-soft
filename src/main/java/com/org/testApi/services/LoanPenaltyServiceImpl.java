package com.org.testApi.services;

import com.org.testApi.models.Loan;
import com.org.testApi.models.LoanType;
import com.org.testApi.models.LoanPenaltyConfig;
import com.org.testApi.models.PenaltyCalculation;
import com.org.testApi.repository.LoanPenaltyConfigRepository;
import com.org.testApi.repository.PenaltyCalculationRepository;
import com.org.testApi.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanPenaltyServiceImpl implements LoanPenaltyService {
    
    @Autowired
    private LoanPenaltyConfigRepository loanPenaltyConfigRepository;
    
    @Autowired
    private PenaltyCalculationRepository penaltyCalculationRepository;
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Override
    public PenaltyCalculation calculatePenalty(Loan loan) {
        if (!isEligibleForPenalty(loan)) {
            throw new RuntimeException("Le prêt n'est pas éligible aux pénalités");
        }
        
        LoanType loanType = loan.getLoanType();
        if (loanType == null) {
            throw new RuntimeException("Le prêt n'a pas de type associé");
        }
        
        Optional<LoanPenaltyConfig> penaltyConfigOpt = loanPenaltyConfigRepository.findByLoanType(loanType);
        if (!penaltyConfigOpt.isPresent()) {
            throw new RuntimeException("Aucune configuration de pénalité trouvée pour ce type de prêt");
        }
        
        LoanPenaltyConfig penaltyConfig = penaltyConfigOpt.get();
        
        // Calculer le nombre de jours de retard
        LocalDate dueDate = loan.getDueDate();
        LocalDate currentDate = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, currentDate);
        
        // Vérifier le délai avant application des pénalités
        if (daysOverdue < penaltyConfig.getPenaltyDelayDays()) {
            throw new RuntimeException("Le délai minimal avant application des pénalités n'est pas atteint");
        }
        
        // Calculer le montant de base pour les pénalités
        BigDecimal baseAmount = loan.getAmount();
        
        // Calculer le taux de pénalité selon le type de prêt
        BigDecimal penaltyRate = calculatePenaltyRate(loan, penaltyConfig, daysOverdue);
        
        // Calculer le montant des pénalités
        BigDecimal penaltyAmount = baseAmount.multiply(penaltyRate).multiply(BigDecimal.valueOf(daysOverdue));
        
        // Appliquer le plafond si défini
        if (penaltyConfig.getMaxPenaltyRate() != null && penaltyConfig.getMaxPenaltyRate().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal maxPenalty = baseAmount.multiply(penaltyConfig.getMaxPenaltyRate());
            if (penaltyAmount.compareTo(maxPenalty) > 0) {
                penaltyAmount = maxPenalty;
            }
        }
        
        // Obtenir les pénalités accumulées
        BigDecimal accumulatedPenalties = calculateTotalPenalties(loan);
        
        // Créer le calcul de pénalité
        PenaltyCalculation penaltyCalculation = new PenaltyCalculation();
        penaltyCalculation.setLoan(loan);
        penaltyCalculation.setCalculationDate(currentDate);
        penaltyCalculation.setDaysOverdue((int) daysOverdue);
        penaltyCalculation.setBaseAmount(baseAmount);
        penaltyCalculation.setPenaltyRate(penaltyRate);
        penaltyCalculation.setPenaltyAmount(penaltyAmount);
        penaltyCalculation.setAccumulatedPenalties(accumulatedPenalties);
        penaltyCalculation.setTotalPenalties(accumulatedPenalties.add(penaltyAmount));
        penaltyCalculation.setApplied(false);
        penaltyCalculation.setPenaltyConfig(penaltyConfig);
        
        return penaltyCalculation;
    }
    
    /**
     * Calcule le taux de pénalité selon le type de prêt et sa configuration
     */
    private BigDecimal calculatePenaltyRate(Loan loan, LoanPenaltyConfig penaltyConfig, long daysOverdue) {
        BigDecimal baseRate = penaltyConfig.getDailyPenaltyRate();
        
        // Pour les pénalités progressives, augmenter le taux en fonction du nombre de jours de retard
        if (penaltyConfig.getProgressivePenalty() != null && penaltyConfig.getProgressivePenalty()) {
            // Augmenter le taux de 10% tous les 30 jours de retard
            long periods = daysOverdue / 30;
            if (periods > 0) {
                BigDecimal increaseFactor = BigDecimal.valueOf(1.1).pow((int) periods);
                baseRate = baseRate.multiply(increaseFactor);
            }
        }
        
        return baseRate;
    }
    
    @Override
    public Loan applyPenalty(Loan loan, PenaltyCalculation penaltyCalculation) {
        // Sauvegarder le calcul de pénalité
        PenaltyCalculation savedPenaltyCalculation = penaltyCalculationRepository.save(penaltyCalculation);
        
        // Marquer comme appliqué
        savedPenaltyCalculation.setApplied(true);
        penaltyCalculationRepository.save(savedPenaltyCalculation);
        
        // Mettre à jour le prêt si nécessaire
        return loan;
    }
    
    @Override
    public LoanPenaltyConfig createPenaltyConfig(LoanPenaltyConfig penaltyConfig) {
        return loanPenaltyConfigRepository.save(penaltyConfig);
    }
    
    @Override
    public LoanPenaltyConfig updatePenaltyConfig(Long id, LoanPenaltyConfig penaltyConfig) {
        LoanPenaltyConfig existingConfig = loanPenaltyConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration de pénalité non trouvée avec l'ID: " + id));
        
        existingConfig.setLoanType(penaltyConfig.getLoanType());
        existingConfig.setPenaltyDelayDays(penaltyConfig.getPenaltyDelayDays());
        existingConfig.setDailyPenaltyRate(penaltyConfig.getDailyPenaltyRate());
        existingConfig.setMaxPenaltyRate(penaltyConfig.getMaxPenaltyRate());
        existingConfig.setProgressivePenalty(penaltyConfig.getProgressivePenalty());
        existingConfig.setCalculationIntervalDays(penaltyConfig.getCalculationIntervalDays());
        existingConfig.setReminderMessage(penaltyConfig.getReminderMessage());
        
        return loanPenaltyConfigRepository.save(existingConfig);
    }
    
    @Override
    public Optional<LoanPenaltyConfig> findPenaltyConfigByLoanType(LoanType loanType) {
        return loanPenaltyConfigRepository.findByLoanType(loanType);
    }
    
    @Override
    public List<PenaltyCalculation> findPenaltyCalculationsByLoan(Loan loan) {
        return penaltyCalculationRepository.findByLoan(loan);
    }
    
    @Override
    public BigDecimal calculateTotalPenalties(Loan loan) {
        return penaltyCalculationRepository.calculateTotalPenaltiesByLoan(loan);
    }
    
    @Override
    public boolean isEligibleForPenalty(Loan loan) {
        // Vérifier que le prêt est actif et en retard
        return loan.getStatus() == Loan.LoanStatus.ACTIVE && loan.isOverdue();
    }
    
    @Override
    public List<Loan> findLoansEligibleForPenalty(LocalDate date) {
        // Trouver tous les prêts en retard à la date donnée
        return loanRepository.findOverdueLoans(date);
    }
}