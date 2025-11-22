package com.org.testApi.repository;

import com.org.testApi.models.LoanPenaltyConfig;
import com.org.testApi.models.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanPenaltyConfigRepository extends JpaRepository<LoanPenaltyConfig, Long> {
    
    /**
     * Trouve la configuration de pénalité par type de prêt.
     *
     * @param loanType le type de prêt
     * @return la configuration de pénalité trouvée
     */
    Optional<LoanPenaltyConfig> findByLoanType(LoanType loanType);
    
    /**
     * Trouve toutes les configurations de pénalité actives.
     *
     * @return la liste des configurations de pénalité actives
     */
    List<LoanPenaltyConfig> findByLoanTypeActiveTrue();
}