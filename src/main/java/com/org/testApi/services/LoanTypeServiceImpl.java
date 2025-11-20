package com.org.testApi.services;

import com.org.testApi.models.LoanType;
import com.org.testApi.repository.LoanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanTypeServiceImpl implements LoanTypeService {
    
    @Autowired
    private LoanTypeRepository loanTypeRepository;
    
    @Override
    public LoanType createLoanType(LoanType loanType) {
        return loanTypeRepository.save(loanType);
    }
    
    @Override
    public LoanType updateLoanType(Long id, LoanType loanType) {
        LoanType existingLoanType = loanTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de prêt non trouvé avec l'ID: " + id));
        
        existingLoanType.setName(loanType.getName());
        existingLoanType.setDescription(loanType.getDescription());
        existingLoanType.setMonthlyInterestRate(loanType.getMonthlyInterestRate());
        existingLoanType.setMaxPenaltyRate(loanType.getMaxPenaltyRate());
        existingLoanType.setGracePeriodDays(loanType.getGracePeriodDays());
        existingLoanType.setCategory(loanType.getCategory());
        existingLoanType.setSubCategory(loanType.getSubCategory());
        existingLoanType.setActive(loanType.getActive());
        
        return loanTypeRepository.save(existingLoanType);
    }
    
    @Override
    public Optional<LoanType> findLoanTypeById(Long id) {
        return loanTypeRepository.findById(id);
    }
    
    @Override
    public Optional<LoanType> findLoanTypeByName(String name) {
        return loanTypeRepository.findByName(name);
    }
    
    @Override
    public List<LoanType> findAllLoanTypes() {
        return loanTypeRepository.findAll();
    }
    
    @Override
    public List<LoanType> findActiveLoanTypes() {
        return loanTypeRepository.findByActiveTrue();
    }
    
    @Override
    public void deleteLoanType(Long id) {
        LoanType loanType = loanTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de prêt non trouvé avec l'ID: " + id));
        
        // Marquer comme inactif au lieu de supprimer physiquement
        loanType.setActive(false);
        loanTypeRepository.save(loanType);
    }
}