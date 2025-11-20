package com.org.testApi.services;

import com.org.testApi.models.Loan;
import com.org.testApi.models.PenaltyCalculation;
import com.org.testApi.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AutomaticPenaltyServiceImpl implements AutomaticPenaltyService {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private LoanPenaltyService loanPenaltyService;
    
    @Override
    public int applyAutomaticPenalties(LocalDate date) {
        List<Loan> eligibleLoans = findEligibleLoans(date);
        int count = 0;
        
        for (Loan loan : eligibleLoans) {
            try {
                PenaltyCalculation penaltyCalculation = calculateAndApplyPenalty(loan);
                if (penaltyCalculation != null) {
                    count++;
                }
            } catch (Exception e) {
                // Log l'exception mais continue avec les autres prêts
                e.printStackTrace();
            }
        }
        
        return count;
    }
    
    @Override
    public List<Loan> findEligibleLoans(LocalDate date) {
        // Trouver tous les prêts en retard à la date donnée
        return loanRepository.findOverdueLoans(date);
    }
    
    @Override
    public PenaltyCalculation calculateAndApplyPenalty(Loan loan) {
        if (loanPenaltyService.isEligibleForPenalty(loan)) {
            try {
                // Calculer les pénalités
                PenaltyCalculation penaltyCalculation = loanPenaltyService.calculatePenalty(loan);
                
                // Appliquer les pénalités
                loanPenaltyService.applyPenalty(loan, penaltyCalculation);
                
                return penaltyCalculation;
            } catch (Exception e) {
                // Log l'exception
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}