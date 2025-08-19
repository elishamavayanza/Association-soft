package com.org.testApi.repository.custom;

import com.org.testApi.models.Loan;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LoanRepositoryCustom {

    /**
     * Trouve les prêts avec des filtres complexes.
     *
     * @param memberId ID du membre
     * @param minAmount Montant minimum
     * @param maxAmount Montant maximum
     * @param status Statut du prêt
     * @param startDate Date de début pour la période de prêt
     * @param endDate Date de fin pour la période de prêt
     * @return Liste des prêts correspondant aux critères
     */
    List<Loan> searchLoansComplexQuery(Long memberId, BigDecimal minAmount, BigDecimal maxAmount,
                                       Loan.LoanStatus status, LocalDate startDate, LocalDate endDate);

    /**
     * Calcule le montant total des prêts pour un membre.
     *
     * @param memberId ID du membre
     * @return Montant total des prêts
     */
    BigDecimal calculateTotalLoansForMember(Long memberId);

    /**
     * Trouve les prêts avec toutes les entités associées.
     *
     * @param limit nombre maximum de prêts à retourner
     * @return Liste des prêts avec leurs associations
     */
    List<Loan> findLoansWithMembers(int limit);

    /**
     * Trouve les prêts en retard avec le nombre de jours de retard.
     *
     * @return Liste des prêts en retard avec les jours de retard
     */
    List<Object[]> findOverdueLoansWithDaysOverdue();
}
