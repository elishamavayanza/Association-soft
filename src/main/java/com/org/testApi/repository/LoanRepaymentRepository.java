package com.org.testApi.repository;

import com.org.testApi.models.LoanRepayment;
import com.org.testApi.models.Loan;
import com.org.testApi.models.LoanRepayment.RepaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {
    
    /**
     * Trouve tous les remboursements d'un prêt.
     *
     * @param loan le prêt
     * @return la liste des remboursements du prêt
     */
    List<LoanRepayment> findByLoan(Loan loan);
    
    /**
     * Trouve tous les remboursements d'un prêt triés par date.
     *
     * @param loan le prêt
     * @return la liste des remboursements du prêt triés par date
     */
    List<LoanRepayment> findByLoanOrderByRepaymentDateAsc(Loan loan);
    
    /**
     * Trouve tous les remboursements par type.
     *
     * @param type le type de remboursement
     * @return la liste des remboursements de ce type
     */
    List<LoanRepayment> findByType(RepaymentType type);
    
    /**
     * Calcule le montant total remboursé pour un prêt.
     *
     * @param loan le prêt
     * @return le montant total remboursé
     */
    @Query("SELECT COALESCE(SUM(lr.amount), 0) FROM LoanRepayment lr WHERE lr.loan = :loan")
    BigDecimal calculateTotalRepaidByLoan(@Param("loan") Loan loan);
    
    /**
     * Trouve les remboursements entre deux dates.
     *
     * @param startDate date de début
     * @param endDate date de fin
     * @return la liste des remboursements entre ces dates
     */
    List<LoanRepayment> findByRepaymentDateBetween(LocalDate startDate, LocalDate endDate);
}