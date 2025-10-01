package com.org.testApi.controllers;

import com.org.testApi.models.Document;
import com.org.testApi.models.Loan;
import com.org.testApi.models.Member;
import com.org.testApi.payload.LoanPayload;
import com.org.testApi.services.LoanService;
import com.org.testApi.mapper.LoanMapper;
import com.org.testApi.repository.DocumentRepository;
import com.org.testApi.repository.LoanRepository;
import com.org.testApi.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Prêt", description = "Gestion des prêts")
public class LoanController{

   @Autowired
    private LoanService loanService;

    @Autowired
    private LoanMapper loanMapper;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private LoanRepository loanRepository;

 /**
* Crée un nouveau prêt pour un membre.
     */
    @PostMapping
    @Operation(summary = "Créer un prêt", description = "Crée un nouveau prêt pour un membre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prêtcrééavecsuccès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500",description="Erreurinterne du serveur")
    })
    public ResponseEntity<?>createLoan(
@Parameter(description = "ID du membre") @RequestParam Long memberId,
            @Parameter(description = "Montant du prêt") @RequestParam BigDecimal amount,
            @Parameter(description = "Taux d'intérêt") @RequestParam BigDecimal interestRate,
@Parameter(description = "Taux de pénalité") @RequestParam BigDecimal penaltyRate,
            @Parameter(description = "Date d'échéance") @RequestParam LocalDate dueDate) {
        try {
            Loan loan = loanService.createLoan(memberId, amount, interestRate, penaltyRate, dueDate);
return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

/**
     * Crée un prêt à partir d'un payload.
     */
    @PostMapping("/payload")
    @Operation(summary = "Créer un prêtàpartird'un payload", description = "Crée un prêt en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prêt créé avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema=@Schema(implementation =Loan.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payloadinvalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> createLoanFromPayload(
         @Parameter(description="Donnéesdu payload pour créer le prêt") 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
             description = "Exemple de payload pour créer un prêt",
             content = @Content(
                 mediaType = "application/json",
                 examples = @ExampleObject(
                     value = "{\n" +
                         "  \"memberId\": 1,\n"+
                          "  \"amount\": 10,\n" +
                          "  \"interestRate\": 0.05,\n" +
                          "  \"penaltyRate\": 0.1,\n" +
                          "  \"dueDate\": \"2025-12-31\",\n" +
                          "  \"repaymentDate\": \"2025-12-31\",\n" +
                          "  \"amountRepaid\": 0,\n" +
                          "  \"status\": \"ACTIVE\"\n"+
                          "}"
                 )
             )
        )
         @org.springframework.web.bind.annotation.RequestBody LoanPayload payload) {
        try {
            // Create a new Loan entity
            Loan loan = new Loan();
            
            // Explicitly set ID to null to ensure it's not causing issues
            // This is a new entity, so ID should be null to allow auto-generation
            loan.setId(null);
            
            // Set the properties from the payload
           loan.setAmount(payload.getAmount());
            loan.setInterestRate(payload.getInterestRate());
            loan.setPenaltyRate(payload.getPenaltyRate());
            loan.setDueDate(payload.getDueDate());
            loan.setRepaymentDate(payload.getRepaymentDate());
            loan.setAmountRepaid(payload.getAmountRepaid());
            
            // Settheloan date to today if not provided
            loan.setLoanDate(LocalDate.now());
            
            if (payload.getStatus() != null) {
               try {
                  loan.setStatus(Loan.LoanStatus.valueOf(payload.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Statut de prêt invalide: "+ payload.getStatus());
                }
            }
            
            // Associer le membre
            Member member = memberRepository.findById(payload.getMemberId())
                   .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + payload.getMemberId()));
            
            // Set the member without triggering cascade operations
loan.setMember(member);
            
            // Associer le document si fourni
            if (payload.getDocumentId() != null) {
                Document document = documentRepository.findById(payload.getDocumentId())
                        .orElseThrow(() -> new RuntimeException("Document non trouvé avec l'ID: " + payload.getDocumentId()));
                loan.setDocument(document);
            }
            
            // Save the loan without cascading to the member's loans collection
            Loan savedLoan = loanRepository.saveAndFlush(loan);
            
            return ResponseEntity.ok(savedLoan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
   }

/**
     * Récupère un prêt par son identifiant.
     */
@GetMapping("/{id}")
    @Operation(summary = "Récupérer un prêt par ID", description = "Retourne un prêt spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description="Prêt trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt non trouvé"),
            @ApiResponse(responseCode = "500", description="Erreur interne duserveur")
})
    public ResponseEntity<Loan> getLoan(
            @Parameter(description = "ID du prêt à récupérer") @PathVariable Long id) {
       return loanService.findLoanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
}

/**
     * Récupère tous les prêtsd'un membre.
     */
    @GetMapping("/member/{memberId}")
    @Operation(summary = "Récupérer les prêts d'un membre", description= "Retourne une liste de tous les prêts d'un membre spécifique")
    @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "Liste des prêts du membre récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
@ApiResponse(responseCode = "500", description ="Erreur interne du serveur")
    })
    public ResponseEntity<List<Loan>> getLoansByMember(
            @Parameter(description = "ID du membre") @PathVariable Long memberId) {
        List<Loan> loans = loanService.findLoansByMemberId(memberId);
        return ResponseEntity.ok(loans);
    }

   /**
     * Rembourse un prêt.
     */
    @PostMapping("/{id}/repay")
    @Operation(summary = "Rembourser un prêt", description = "Effectue un remboursement partiel ou total d'un prêt")
    @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "Prêt remboursé avec succès",
content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode = "400", description = "Montant de remboursementinvalide"),
            @ApiResponse(responseCode = "404", description = "Prêt nontrouvé"),
            @ApiResponse(responseCode = "500", description= "Erreur interne du serveur")
    })
    public ResponseEntity<Loan> repayLoan(
            @Parameter(description = "ID duprêtà rembourser") @PathVariable Long id,
            @Parameter(description = "Montant du remboursement") @RequestParam BigDecimal amount) {
        Loan loan = loanService.repayLoan(id, amount);
        return ResponseEntity.ok(loan);
    }

    /**
     * Calcule lemontant total dûpourun prêt.
     */
   @GetMapping("/{id}/amount-due")
    @Operation(summary = "Calculer le montant dû", description = "Calcule le montant total dû pour un prêt spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="Montantdû calculéavec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt non trouvé"),
            @ApiResponse(responseCode = "500", description ="Erreurinterne du serveur")
   })
    public ResponseEntity<BigDecimal> calculateAmountDue(
            @Parameter(description = "ID du prêt") @PathVariable Long id) {
        BigDecimal amountDue = loanService.calculateTotalAmountDue(id);
        return ResponseEntity.ok(amountDue);
    }

    /**
     * Vérifiesiun prêt est en retard.
     */
    @GetMapping("/{id}/overdue")
    @Operation(summary = "Vérifier si un prêt est en retard", description = "Indique si un prêt est en retard de remboursement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statut de retard du prêt déterminé avecsuccès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt non trouvé"),
            @ApiResponse(responseCode = "500", description ="Erreur interne du serveur")
    })
    public ResponseEntity<Boolean> isLoanOverdue(
            @Parameter(description = "ID du prêt") @PathVariable Long id) {
        boolean overdue = loanService.isLoanOverdue(id);
        return ResponseEntity.ok(overdue);
    }

    /**
     * Récupère tous lesprêts en retard.
     */
    @GetMapping("/overdue")
    @Operation(summary = "Récupérer les prêts en retard", description = "Retourne une liste de tous les prêts en retard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prêtsenretard récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        List<Loan> overdueLoans = loanService.findOverdueLoans();
        return ResponseEntity.ok(overdueLoans);
    }

    /**
     * Récupère tous les prêts actifs.
     */
    @GetMapping("/active")
    @Operation(summary = "Récupérer les prêts actifs", description = "Retourne une liste de tous les prêts actifs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des prêts actifs récupérée avecsuccès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Loan>> getActiveLoans(){
        List<Loan> activeLoans = loanService.findActiveLoans();
        return ResponseEntity.ok(activeLoans);
    }

    /**
     * Recherche des prêts avec des filtres complexes.
     */
    @GetMapping("/search")
    @Operation(summary = "Rechercher des prêts", description = "Recherchedes prêts avec desfiltres complexes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode="500", description ="Erreur interne du serveur")
    })
    public ResponseEntity<List<Loan>> searchLoans(
            @Parameter(description = "ID du membre (optionnel)") @RequestParam(required = false) Long memberId,
            @Parameter(description = "Montant minimum (optionnel)") @RequestParam(required= false) BigDecimal minAmount,
            @Parameter(description = "Montant maximum (optionnel)") @RequestParam(required = false) BigDecimal maxAmount,
            @Parameter(description = "Statut du prêt (optionnel)") @RequestParam(required = false) Loan.LoanStatus status,
            @Parameter(description = "Date dedébut (optionnel)") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Date de fin (optionnel)") @RequestParam(required = false) LocalDate endDate) {
        List<Loan> loans = loanService.searchLoansComplexQuery(memberId, minAmount, maxAmount, status, startDate, endDate);
        return ResponseEntity.ok(loans);
    }

    /**
     * Calcule le montant total des prêts pour un membre.
     */
    @GetMapping("/member/{memberId}/total")
    @Operation(summary = "Calculer le total des prêtsd'un membre", description = "Calcule le montant total des prêts pour un membre spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant total des prêts calculé avec succès",
                    content = {@Content(mediaType ="application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<BigDecimal> getTotalLoansForMember(
            @Parameter(description = "ID du membre") @PathVariable Long memberId) {
        BigDecimal total = loanService.calculateTotalLoansForMember(memberId);
        return ResponseEntity.ok(total);
    }

    /**
     * Récupère les prêts en retard avec le nombre de jours de retard.
*/
    @GetMapping("/overdue-with-days")
@Operation(summary = "Récupérer les prêts en retard avec jours de retard", description = "Retourne une liste des prêts en retard avec le nombre de jours de retard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listedesprêts enretard avec jours récupérée avec succès",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Object[]>> getOverdueLoansWithDaysOverdue() {
       List<Object[]> overdueLoans = loanService.findOverdueLoansWithDaysOverdue();
        return ResponseEntity.ok(overdueLoans);
    }

    /**
     * Vérifie si un membre est éligible pour emprunter.
*/
    @GetMapping("/member/{memberId}/eligible")
    @Operation(summary = "Vérifier l'éligibilité d'un membre", description = "Indique si un membre est éligible pour emprunter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Éligibilité dumembre déterminéeavec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description= "Erreurinterne duserveur")
    })
    public ResponseEntity<Boolean> isMemberEligibleForLoan(
            @Parameter(description = "ID du membre") @PathVariable Long memberId) {
        boolean eligible = loanService.isMemberEligibleForLoan(memberId);
        return ResponseEntity.ok(eligible);
    }

/**
    * Calcule le montant maximum qu'un membre peut emprunter.
    */
    @GetMapping("/member/{memberId}/max-amount")
    @Operation(summary = "Calculer le montant maximum d'emprunt", description = "Calcule le montant maximum qu'un membre peutemprunter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montantmaximumcalculé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "400",description = "Impossible de calculer le montantmaximum"),
            @ApiResponse(responseCode = "404", description ="Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<BigDecimal> getMaxLoanAmountForMember(
@Parameter(description = "ID du membre")@PathVariable Long memberId) {
        try {
            BigDecimal maxAmount =loanService.calculateMaxLoanAmount(memberId);
            return ResponseEntity.ok(maxAmount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     *Metà jour un prêt avec un payload.
     */
@PutMapping("/{id}/payload")
    @Operation(summary ="Mettre à jour un prêt avec payload", description = "Met à jour un prêt existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prêt misàjour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt non trouvé"),
            @ApiResponse(responseCode = "400", description ="Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Loan> updateLoanWithPayload(
            @Parameter(description = "ID du prêt à mettre à jour") @PathVariable Long id,
            @Parameter(description="Données du payload pour mettre à jour leprêt") @RequestBody LoanPayload payload) {
        return loanService.findLoanById(id)
                .map(loan -> {
                    loanMapper.updateEntityFromPayload(payload, loan);
                    // Pour les prêts, certaines propriétés doivent êtremises à jour via les méthodes du service
                    Loan updatedLoan = loanService.updateLoan(id, loan);
                    return ResponseEntity.ok(updatedLoan);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}