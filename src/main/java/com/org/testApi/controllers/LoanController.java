package com.org.testApi.controllers;

import com.org.testApi.models.Loan;
import com.org.testApi.payload.LoanPayload;
import com.org.testApi.services.LoanService;
import com.org.testApi.mapper.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanMapper loanMapper;

    /**
     * Crée un nouveau prêt pour un membre.
     */
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestParam Long memberId,
                                           @RequestParam BigDecimal amount,
                                           @RequestParam BigDecimal interestRate,
                                           @RequestParam BigDecimal penaltyRate,
                                           @RequestParam LocalDate dueDate) {
        Loan loan = loanService.createLoan(memberId, amount, interestRate, penaltyRate, dueDate);
        return ResponseEntity.ok(loan);
    }

    /**
     * Crée un prêt à partir d'un payload.
     */
    @PostMapping("/payload")
    public ResponseEntity<Loan> createLoanFromPayload(@RequestBody LoanPayload payload) {
        Loan loan = loanMapper.toEntityFromPayload(payload);
        // Extraire les paramètres nécessaires du payload
        Loan savedLoan = loanService.createLoan(
                payload.getMemberId(),
                payload.getAmount(),
                payload.getInterestRate(),
                payload.getPenaltyRate(),
                payload.getDueDate()
        );
        return ResponseEntity.ok(savedLoan);
    }

    /**
     * Récupère un prêt par son identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoan(@PathVariable Long id) {
        return loanService.findLoanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les prêts d'un membre.
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getLoansByMember(@PathVariable Long memberId) {
        List<Loan> loans = loanService.findLoansByMemberId(memberId);
        return ResponseEntity.ok(loans);
    }

    /**
     * Rembourse un prêt.
     */
    @PostMapping("/{id}/repay")
    public ResponseEntity<Loan> repayLoan(@PathVariable Long id,
                                          @RequestParam BigDecimal amount) {
        Loan loan = loanService.repayLoan(id, amount);
        return ResponseEntity.ok(loan);
    }

    /**
     * Calcule le montant total dû pour un prêt.
     */
    @GetMapping("/{id}/amount-due")
    public ResponseEntity<BigDecimal> calculateAmountDue(@PathVariable Long id) {
        BigDecimal amountDue = loanService.calculateTotalAmountDue(id);
        return ResponseEntity.ok(amountDue);
    }

    /**
     * Vérifie si un prêt est en retard.
     */
    @GetMapping("/{id}/overdue")
    public ResponseEntity<Boolean> isLoanOverdue(@PathVariable Long id) {
        boolean overdue = loanService.isLoanOverdue(id);
        return ResponseEntity.ok(overdue);
    }

    /**
     * Récupère tous les prêts en retard.
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        List<Loan> overdueLoans = loanService.findOverdueLoans();
        return ResponseEntity.ok(overdueLoans);
    }

    /**
     * Récupère tous les prêts actifs.
     */
    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        List<Loan> activeLoans = loanService.findActiveLoans();
        return ResponseEntity.ok(activeLoans);
    }

    /**
     * Recherche des prêts avec des filtres complexes.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Loan>> searchLoans(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) Loan.LoanStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<Loan> loans = loanService.searchLoansComplexQuery(memberId, minAmount, maxAmount, status, startDate, endDate);
        return ResponseEntity.ok(loans);
    }

    /**
     * Calcule le montant total des prêts pour un membre.
     */
    @GetMapping("/member/{memberId}/total")
    public ResponseEntity<BigDecimal> getTotalLoansForMember(@PathVariable Long memberId) {
        BigDecimal total = loanService.calculateTotalLoansForMember(memberId);
        return ResponseEntity.ok(total);
    }

    /**
     * Récupère les prêts en retard avec le nombre de jours de retard.
     */
    @GetMapping("/overdue-with-days")
    public ResponseEntity<List<Object[]>> getOverdueLoansWithDaysOverdue() {
        List<Object[]> overdueLoans = loanService.findOverdueLoansWithDaysOverdue();
        return ResponseEntity.ok(overdueLoans);
    }

    /**
     * Vérifie si un membre est éligible pour emprunter.
     */
    @GetMapping("/member/{memberId}/eligible")
    public ResponseEntity<Boolean> isMemberEligibleForLoan(@PathVariable Long memberId) {
        boolean eligible = loanService.isMemberEligibleForLoan(memberId);
        return ResponseEntity.ok(eligible);
    }

    /**
     * Calcule le montant maximum qu'un membre peut emprunter.
     */
    @GetMapping("/member/{memberId}/max-amount")
    public ResponseEntity<BigDecimal> getMaxLoanAmountForMember(@PathVariable Long memberId) {
        try {
            BigDecimal maxAmount = loanService.calculateMaxLoanAmount(memberId);
            return ResponseEntity.ok(maxAmount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un prêt avec un payload.
     */
    @PutMapping("/{id}/payload")
    public ResponseEntity<Loan> updateLoanWithPayload(@PathVariable Long id, @RequestBody LoanPayload payload) {
        return loanService.findLoanById(id)
                .map(loan -> {
                    loanMapper.updateEntityFromPayload(payload, loan);
                    // Pour les prêts, certaines propriétés doivent être mises à jour via les méthodes du service
                    Loan updatedLoan = loanService.updateLoan(id, loan);
                    return ResponseEntity.ok(updatedLoan);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
