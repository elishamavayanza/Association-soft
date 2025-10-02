package com.org.testApi.services;

import com.org.testApi.models.Document;
import com.org.testApi.models.Loan;
import com.org.testApi.models.Member;
import com.org.testApi.models.Loan.LoanStatus;
import com.org.testApi.repository.DocumentRepository;
import com.org.testApi.repository.LoanRepository;
import com.org.testApi.repository.MemberRepository;
import com.org.testApi.models.MembershipFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Loan createLoan(Long memberId, BigDecimal amount, BigDecimal interestRate, BigDecimal penaltyRate, LocalDate dueDate) {
        // Vérifier que le membre existe
        Member member = memberRepository.findWithLoansById(memberId)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + memberId));

        // Vérifier l'éligibilitédu membre
        if (!member.isEligibleForLoan()) {
            // Fournir des détails sur pourquoi le membre n'est pas éligible
            StringBuilder ineligibilityReason = new StringBuilder("Le membre n'est pas éligible pour emprunter. ");
            
            if (!member.isActive()) {
                ineligibilityReason.append("Le membre n'est pas actif. ");
            }
            
            if (member.getFees() == null || member.getFees().isEmpty()) {
                ineligibilityReason.append("Le membre n'a payé aucune cotisation. ");
            }
boolean hasOverdueLoans = member.getLoans().stream()
                    .filter(loan -> loan != null)
                    .anyMatch(loan -> loan.getStatus() == Loan.LoanStatus.OVERDUE);
            if (hasOverdueLoans) {
                ineligibilityReason.append("Le membre a des prêts en retard. ");
            }
            
            throw new RuntimeException(ineligibilityReason.toString());
        }

        // Vérifier que le montant ne dépasse pas le maximum autorisé
        BigDecimal maxLoanAmount = calculateMaxLoanAmount(memberId);
        if (amount.compareTo(maxLoanAmount)> 0) {
            throw new RuntimeException("Le montant demandé (" + amount + ") dépasse le maximum autorisé de " + maxLoanAmount);
        }

        // Créer le prêt
        Loan loan = new Loan();
        loan.setMember(member);
        loan.setAmount(amount);
        loan.setInterestRate(interestRate);
        loan.setPenaltyRate(penaltyRate);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(dueDate);

        return loanRepository.save(loan);
    }

    @Override
    public Optional<Loan> findLoanById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public List<Loan> findLoansByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    @Override
    public Loan repayLoan(Long loanId, BigDecimal amount) {
        Loan loan = loanRepository.findById(loanId)
               .orElseThrow(() -> new RuntimeException("Prêt non trouvé avec l'ID: " + loanId));

        // Vérifier que le prêt n'est pas déjà remboursé
        if (loan.getStatus() == Loan.LoanStatus.REPAID) {
            throw new RuntimeException("Ce prêt est déjà remboursé");
        }

        // Vérifier que le montant remboursé n'est pas supérieur au montant dû
        BigDecimal totalAmountDue = loan.getTotalAmountDue();
        if (amount.compareTo(totalAmountDue) > 0) {
            throw new RuntimeException("Le montant remboursé ne peut pas être supérieur au montant dû");
        }

        // Mettre à jour le prêt
        loan.setAmountRepaid(amount);
        loan.setRepaymentDate(LocalDate.now());

        // Si le montant remboursé est égal au montant dû, marquer comme remboursé
        if (amount.compareTo(totalAmountDue) == 0) {
            loan.setStatus(Loan.LoanStatus.REPAID);
        }

        return loanRepository.save(loan);
    }

    @Override
    public BigDecimal calculateTotalAmountDue(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Prêt non trouvé avec l'ID: " + loanId));

        return loan.getTotalAmountDue();
    }

    @Override
    public boolean isLoanOverdue(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Prêt non trouvé avec l'ID: " + loanId));

        return loan.isOverdue();
    }

    @Override
    public List<Loan> findOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now());
    }

    @Override
    public List<Loan> findActiveLoans() {
        return loanRepository.findActiveLoans();
    }

    @Override
    public List<Loan> searchLoansComplexQuery(Long memberId, BigDecimal minAmount, BigDecimal maxAmount,
                                              Loan.LoanStatus status, LocalDate startDate, LocalDate endDate) {
        return loanRepository.searchLoansComplexQuery(memberId, minAmount, maxAmount, status, startDate, endDate);
    }

   @Override
    public BigDecimal calculateTotalLoansForMember(Long memberId) {
        return loanRepository.calculateTotalLoansForMember(memberId);
    }

    @Override
    public List<Object[]> findOverdueLoansWithDaysOverdue() {
        return loanRepository.findOverdueLoansWithDaysOverdue();
    }

@Override
    public boolean isMemberEligibleForLoan(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + memberId));

        return member.isEligibleForLoan();
    }

    @Override public BigDecimal calculateMaxLoanAmount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + memberId));

        // Vérifier l'éligibilité
        if (!member.isEligibleForLoan()) {
            throw new RuntimeException("Le membre n'est pas éligible pour emprunter");
        }

        // Calculer le montant maximum basé sur les cotisations payées
        // Par exemple, le montant maximum est 3 fois la somme des cotisations payées
        BigDecimal totalFees = member.getFees().stream()
                .filter(fee -> fee != null && fee.getAmount() != null)
                .map(MembershipFee::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalFees.multiply(BigDecimal.valueOf(3))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Loan updateLoan(Long id, Loan loan) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prêt non trouvé avec l'ID: " + id));

        // Mettre à jour toutesles propriétés du prêt
        existingLoan.setAmount(loan.getAmount());
        existingLoan.setInterestRate(loan.getInterestRate());
        existingLoan.setPenaltyRate(loan.getPenaltyRate());
        existingLoan.setDueDate(loan.getDueDate());
        existingLoan.setRepaymentDate(loan.getRepaymentDate());
        existingLoan.setAmountRepaid(loan.getAmountRepaid());
        existingLoan.setStatus(loan.getStatus());
        existingLoan.setLoanDate(loan.getLoanDate());
        existingLoan.setReturnDate(loan.getReturnDate());
        existingLoan.setDepositAmount(loan.getDepositAmount());
        existingLoan.setDepositRefunded(loan.getDepositRefunded());
        existingLoan.setNotes(loan.getNotes());
        
        // Mettre à jour les associations si elles existent
        if (loan.getMember() != null) {
            existingLoan.setMember(loan.getMember());
        }
        
        if (loan.getDocument() != null) {
            existingLoan.setDocument(loan.getDocument());
        }

        return loanRepository.save(existingLoan);
    }
}