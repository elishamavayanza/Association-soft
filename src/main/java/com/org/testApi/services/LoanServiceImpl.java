package com.org.testApi.services;

import com.org.testApi.dto.LoanEligibilityResult;
import com.org.testApi.models.Document;
import com.org.testApi.models.Loan;
import com.org.testApi.models.Member;
import com.org.testApi.models.Loan.LoanStatus;
import com.org.testApi.repository.DocumentRepository;
import com.org.testApi.repository.LoanRepository;
import com.org.testApi.repository.MemberRepository;
import com.org.testApi.models.MembershipFee;
import com.org.testApi.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private NotificationService notificationService;

    @Override
    public Loan createLoan(Long memberId, BigDecimal amount, BigDecimal interestRate, BigDecimal penaltyRate, LocalDate dueDate) {
        // Vérifier que le membre existe
        Member member = memberRepository.findWithLoansById(memberId)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + memberId));

        // Vérifier l'éligibilité du membre
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
        if (amount.compareTo(maxLoanAmount) > 0) {
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

        Loan savedLoan = loanRepository.save(loan);
        
        // Envoyer une notification SMS au membre
        sendLoanCreationNotification(member, savedLoan);
        
        return savedLoan;
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

        // Vérifier que le montant remboursé est positif
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Le montant remboursé doit être supérieur à zéro");
        }

        // Calculer le montant total dû
        BigDecimal totalAmountDue = loan.getTotalAmountDue();
        
        // Calculer le montant déjà remboursé (ou 0 si null)
        BigDecimal amountAlreadyRepaid = loan.getAmountRepaid() != null ? loan.getAmountRepaid() : BigDecimal.ZERO;
        
        // Vérifier que le montant remboursé n'est pas supérieur au montant restant dû
        BigDecimal amountRemaining = totalAmountDue.subtract(amountAlreadyRepaid);
        if (amount.compareTo(amountRemaining) > 0) {
            throw new RuntimeException("Le montant remboursé ne peut pas être supérieur au montant restant dû (" + amountRemaining + ")");
        }

        // Mettre à jour le montant remboursé
        BigDecimal newAmountRepaid = amountAlreadyRepaid.add(amount);
        loan.setAmountRepaid(newAmountRepaid);
        loan.setRepaymentDate(LocalDate.now());

        // Si le montant remboursé est égal ou supérieur au montant dû, marquer comme remboursé
        if (newAmountRepaid.compareTo(totalAmountDue) >= 0) {
            loan.setStatus(Loan.LoanStatus.REPAID);
        }
        
        // Sauvegarder le prêt - cela déclenchera automatiquement updateStatus() via les callbacks JPA
        Loan repaidLoan = loanRepository.save(loan);
        
        // Envoyer une notification SMS au membre
        sendLoanRepaymentNotification(loan.getMember(), repaidLoan, amount);
        
        return repaidLoan;
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
        Member member = memberRepository.findWithLoansById(memberId)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + memberId));

        return member.isEligibleForLoan();
    }

    @Override
    public LoanEligibilityResult getMemberLoanEligibilityDetails(Long memberId) {
        Member member = memberRepository.findWithLoansById(memberId)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + memberId));

        return member.checkLoanEligibility();
    }

    @Override 
    public BigDecimal calculateMaxLoanAmount(Long memberId) {
        Member member = memberRepository.findWithLoansById(memberId)
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

        // Mettre à jour toutes les propriétés du prêt
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
        
        if (loan.getLoanType() != null) {
            existingLoan.setLoanType(loan.getLoanType());
        }
        
        if (loan.getLoanApplication() != null) {
            existingLoan.setLoanApplication(loan.getLoanApplication());
        }

        return loanRepository.save(existingLoan);
    }
    
    @Override
    public Loan createLoanFromApplication(Long loanApplicationId) {
        // Cette méthode nécessiterait une implémentation complète avec le repository de LoanApplication
        // Pour l'instant, nous lançons une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }
    
    @Override
    public List<Loan> findLoansByLoanTypeId(Long loanTypeId) {
        return loanRepository.findByLoanTypeId(loanTypeId);
    }
    
    /**
     * Envoie une notification de création de prêt par SMS au membre
     * @param member Le membre qui reçoit le prêt
     * @param loan Le prêt créé
     */
    private void sendLoanCreationNotification(Member member, Loan loan) {
        try {
            // Vérifier si le membre a un numéro de téléphone
            if (member.getPhone() != null && !member.getPhone().isEmpty()) {
                String message = String.format(
                    "Bonjour %s %s, votre prêt de %s %s a été approuvé et est maintenant actif. " +
                    "Date d'échéance: %s. Merci de votre confiance.",
                    member.getFirstName(),
                    member.getLastName(),
                    loan.getAmount(),
                    loan.getCurrency(),
                    loan.getDueDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                
                notificationService.sendSmsNotification(member.getPhone(), message);
            }
        } catch (Exception e) {
            System.err.println("Failed to send loan creation SMS notification to member " + member.getId() + ": " + e.getMessage());
        }
    }
    
    /**
     * Envoie une notification de remboursement de prêt par SMS au membre
     * @param member Le membre qui rembourse le prêt
     * @param loan Le prêt remboursé
     * @param amount Le montant remboursé
     */
    private void sendLoanRepaymentNotification(Member member, Loan loan, BigDecimal amount) {
        try {
            // Vérifier si le membre a un numéro de téléphone
            if (member.getPhone() != null && !member.getPhone().isEmpty()) {
                String message;
                
                // Vérifier si le prêt est entièrement remboursé
                if (loan.getStatus() == Loan.LoanStatus.REPAID) {
                    message = String.format(
                        "Bonjour %s %s, votre remboursement de %s %s pour le prêt #%d a été enregistré avec succès. " +
                        "Félicitations, vous n'avez plus de dette. Merci pour votre confiance.",
                        member.getFirstName(),
                        member.getLastName(),
                        amount,
                        loan.getCurrency(),
                        loan.getId()
                    );
                } else {
                    // Calculer le montant restant à rembourser
                    BigDecimal totalAmountDue = loan.getTotalAmountDue();
                    BigDecimal amountRepaid = loan.getAmountRepaid() != null ? loan.getAmountRepaid() : BigDecimal.ZERO;
                    BigDecimal amountRemaining = totalAmountDue.subtract(amountRepaid);
                    
                    message = String.format(
                        "Bonjour %s %s, votre remboursement de %s %s pour le prêt #%d a été enregistré avec succès. " +
                        "Reste à rembourser: %s %s. Merci pour votre confiance.",
                        member.getFirstName(),
                        member.getLastName(),
                        amount,
                        loan.getCurrency(),
                        loan.getId(),
                        amountRemaining,
                        loan.getCurrency()
                    );
                }
                
                notificationService.sendSmsNotification(member.getPhone(), message);
            }
        } catch (Exception e) {
            System.err.println("Failed to send loan repayment SMS notification to member " + member.getId() + ": " + e.getMessage());
        }
    }
}