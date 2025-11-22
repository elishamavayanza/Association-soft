package com.org.testApi.repository;

import com.org.testApi.models.PenaltyCalculation;
import com.org.testApi.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PenaltyCalculationRepository extends JpaRepository<PenaltyCalculation, Long> {
    
    /**
     * Trouve tous les calculs de pénalité d'un prêt.
     *
     * @param loan le prêt
     * @return la liste des calculs de pénalité du prêt
     */
    List<PenaltyCalculation> findByLoan(Loan loan);
    
    /**
     * Trouve tous les calculs de pénalité appliqués.
     *
     * @param applied indique si les pénalités ont été appliquées
     * @return la liste des calculs de pénalité
     */
    List<PenaltyCalculation> findByApplied(Boolean applied);
    
    /**
     * Trouve les calculs de pénalité entre deux dates.
     *
     * @param startDate date de début
     * @param endDate date de fin
     * @return la liste des calculs de pénalité entre ces dates
     */
    List<PenaltyCalculation> findByCalculationDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Calcule le montant total des pénalités pour un prêt.
     *
     * @param loan le prêt
     * @return le montant total des pénalités
     */
    @Query("SELECT COALESCE(SUM(pc.penaltyAmount), 0) FROM PenaltyCalculation pc WHERE pc.loan = :loan")
    BigDecimal calculateTotalPenaltiesByLoan(@Param("loan") Loan loan);
    
    /**
     * Trouve le dernier calcul de pénalité pour un prêt.
     *
     * @param loan le prêt
     * @return le dernier calcul de pénalité
     */
    @Query("SELECT pc FROM PenaltyCalculation pc WHERE pc.loan = :loan ORDER BY pc.calculationDate DESC")
    List<PenaltyCalculation> findLatestByLoan(@Param("loan") Loan loan);
}