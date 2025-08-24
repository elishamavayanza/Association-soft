package com.org.testApi.services;

import com.org.testApi.models.Loan;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanService {

    /**
     * Crée un nouveau prêt pour un membre.
     *
     * @param memberId l'identifiant du membre
     * @param amount le montant du prêt
     * @param interestRate le taux d'intérêt annuel
     * @param penaltyRate le taux de pénalité journalier
     * @param dueDate la date d'échéance du prêt
     * @return le prêt créé
     */
    Loan createLoan(Long memberId, BigDecimal amount, BigDecimal interestRate, BigDecimal penaltyRate, LocalDate dueDate);

    /**
     * Trouve un prêt par son identifiant.
     *
     * @param id l'identifiant du prêt
     * @return le prêt s'il existe
     */
    Optional<Loan> findLoanById(Long id);

    /**
     * Trouve tous les prêts d'un membre.
     *
     * @param memberId l'identifiant du membre
     * @return la liste des prêts du membre
     */
    List<Loan> findLoansByMemberId(Long memberId);

    /**
     * Rembourse un prêt.
     *
     * @param loanId l'identifiant du prêt
     * @param amount le montant remboursé
     * @return le prêt mis à jour
     */
    Loan repayLoan(Long loanId, BigDecimal amount);

    /**
     * Calcule le montant total dû pour un prêt.
     *
     * @param loanId l'identifiant du prêt
     * @return le montant total dû
     */
    BigDecimal calculateTotalAmountDue(Long loanId);

    /**
     * Vérifie le statut d'un prêt.
     *
     * @param loanId l'identifiant du prêt
     * @return true si le prêt est en retard, false sinon
     */
    boolean isLoanOverdue(Long loanId);

    /**
     * Trouve tous les prêts en retard.
     *
     * @return la liste des prêts en retard
     */
    List<Loan> findOverdueLoans();

    /**
     * Trouve tous les prêts actifs.
     *
     * @return la liste des prêts actifs
     */
    List<Loan> findActiveLoans();

    /**
     * Recherche des prêts avec des filtres complexes.
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
     * Trouve les prêts en retard avec le nombre de jours de retard.
     *
     * @return Liste des prêts en retard avec les jours de retard
     */
    List<Object[]> findOverdueLoansWithDaysOverdue();


    /**
     * Vérifie si un membre est éligible pour emprunter.
     *
     * @param memberId ID du membre
     * @return true si le membre est éligible, false sinon
     */
    boolean isMemberEligibleForLoan(Long memberId);

    /**
     * Calcule le montant maximum qu'un membre peut emprunter.
     * Basé sur les cotisations payées par le membre.
     *
     * @param memberId ID du membre
     * @return le montant maximum pouvant être emprunté
     */
    BigDecimal calculateMaxLoanAmount(Long memberId);

    /**
     * Met à jour un prêt existant.
     *
     * @param id l'identifiant du prêt
     * @param loan le prêt mis à jour
     * @return le prêt mis à jour
     */
    Loan updateLoan(Long id, Loan loan);
}
