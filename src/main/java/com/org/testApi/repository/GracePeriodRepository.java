package com.org.testApi.repository;

import com.org.testApi.models.GracePeriod;
import com.org.testApi.models.Loan;
import com.org.testApi.models.LoanType;
import com.org.testApi.models.GracePeriod.GracePeriodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GracePeriodRepository extends JpaRepository<GracePeriod, Long> {
    
    /**
     * Trouve toutes les périodes de grâce d'un prêt.
     *
     * @param loan le prêt
     * @return la liste des périodes de grâce du prêt
     */
    List<GracePeriod> findByLoan(Loan loan);
    
    /**
     * Trouve toutes les périodes de grâce par statut.
     *
     * @param status le statut des périodes de grâce
     * @return la liste des périodes de grâce avec ce statut
     */
    List<GracePeriod> findByStatus(GracePeriodStatus status);
    
    /**
     * Trouve toutes les périodes de grâce d'un type de prêt.
     *
     * @param loanType le type de prêt
     * @return la liste des périodes de grâce de ce type
     */
    List<GracePeriod> findByLoanType(LoanType loanType);
    
    /**
     * Trouve les périodes de grâce actives pour une date donnée.
     *
     * @param date la date de référence
     * @return la liste des périodes de grâce actives
     */
    @Query("SELECT gp FROM GracePeriod gp WHERE gp.startDate <= :date AND gp.endDate >= :date AND gp.status = 'ACTIVE'")
    List<GracePeriod> findActiveByDate(@Param("date") LocalDate date);
    
    /**
     * Trouve les périodes de grâce expirées non utilisées.
     *
     * @return la liste des périodes de grâce expirées
     */
    @Query("SELECT gp FROM GracePeriod gp WHERE gp.endDate < CURRENT_DATE AND gp.status = 'ACTIVE'")
    List<GracePeriod> findExpiredGracePeriods();
}