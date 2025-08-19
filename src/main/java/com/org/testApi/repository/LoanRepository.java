package com.org.testApi.repository;

import com.org.testApi.models.Loan;
import com.org.testApi.repository.custom.LoanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>, LoanRepositoryCustom {

    /**
     * Trouve tous les prêts d'un membre spécifique.
     *
     * @param memberId l'identifiant du membre
     * @return la liste des prêts du membre
     */
    List<Loan> findByMemberId(Long memberId);

    /**
     * Trouve tous les prêts en retard.
     *
     * @param currentDate la date de référence pour déterminer si un prêt est en retard
     * @return la liste des prêts en retard
     */
    @Query("SELECT l FROM Loan l WHERE l.dueDate < :currentDate AND l.status != 'REPAID'")
    List<Loan> findOverdueLoans(@Param("currentDate") LocalDate currentDate);

    /**
     * Trouve tous les prêts actifs (non remboursés).
     *
     * @return la liste des prêts actifs
     */
    @Query("SELECT l FROM Loan l WHERE l.status != 'REPAID'")
    List<Loan> findActiveLoans();
}
