 package com.org.testApi.controllers;

import com.org.testApi.models.Loan;
import com.org.testApi.models.LoanType;
import com.org.testApi.models.LoanPenaltyConfig;
import com.org.testApi.models.PenaltyCalculation;
import com.org.testApi.services.LoanPenaltyService;
import com.org.testApi.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loan-penalties")
@Tag(name = "Pénalité de Prêt", description = "Gestion des pénalités de prêts")
public class LoanPenaltyController {

    @Autowired
    private LoanPenaltyService loanPenaltyService;

    @Autowired
    private LoanService loanService;

    /**
     * Calcule les pénalités pour un prêt.
     */
    @PostMapping("/{loanId}/calculate")
    @Operation(summary = "Calculer les pénalités", description = "Calcule les pénalités pour un prêt spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pénalités calculées avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PenaltyCalculation.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt non trouvé"),
            @ApiResponse(responseCode = "400", description = "Prêt non éligible aux pénalités"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<PenaltyCalculation> calculatePenalty(
            @Parameter(description = "ID du prêt") @PathVariable Long loanId) {
        return loanService.findLoanById(loanId)
                .map(loan -> {
                    PenaltyCalculation penaltyCalculation = loanPenaltyService.calculatePenalty(loan);
                    return ResponseEntity.ok(penaltyCalculation);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Applique les pénalités à un prêt.
     */
    @PostMapping("/{loanId}/apply")
    @Operation(summary = "Appliquer les pénalités", description = "Applique les pénalités calculées à un prêt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pénalités appliquées avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Loan.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt ou calcul de pénalité non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Loan> applyPenalty(
            @Parameter(description = "ID du prêt") @PathVariable Long loanId,
            @Parameter(description = "Données du calcul de pénalité") @RequestBody PenaltyCalculation penaltyCalculation) {
        return loanService.findLoanById(loanId)
                .map(loan -> {
                    Loan updatedLoan = loanPenaltyService.applyPenalty(loan, penaltyCalculation);
                    return ResponseEntity.ok(updatedLoan);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une configuration de pénalité.
     */
    @PostMapping("/config")
    @Operation(summary = "Créer une configuration de pénalité", description = "Crée une nouvelle configuration de pénalité pour un type de prêt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration de pénalité créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanPenaltyConfig.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<LoanPenaltyConfig> createPenaltyConfig(
            @Parameter(description = "Données de la configuration de pénalité") @RequestBody LoanPenaltyConfig penaltyConfig) {
        LoanPenaltyConfig createdConfig = loanPenaltyService.createPenaltyConfig(penaltyConfig);
        return ResponseEntity.ok(createdConfig);
    }

    /**
     * Récupère la configuration de pénalité pour un type de prêt.
     */
    @GetMapping("/config/loan-type/{loanTypeId}")
    @Operation(summary = "Récupérer la configuration de pénalité", description = "Retourne la configuration de pénalité pour un type de prêt spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration de pénalité trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanPenaltyConfig.class))}),
            @ApiResponse(responseCode = "404", description = "Configuration de pénalité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<LoanPenaltyConfig> getPenaltyConfigByLoanType(
            @Parameter(description = "ID du type de prêt") @PathVariable Long loanTypeId) {
        // Note: In a full implementation, you would retrieve the LoanType by ID first
        return ResponseEntity.notFound().build();
    }

    /**
     * Récupère tous les calculs de pénalité pour un prêt.
     */
    @GetMapping("/{loanId}/calculations")
    @Operation(summary = "Récupérer les calculs de pénalité", description = "Retourne tous les calculs de pénalité pour un prêt spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculs de pénalité récupérés avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PenaltyCalculation.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<PenaltyCalculation>> getPenaltyCalculationsByLoan(
            @Parameter(description = "ID du prêt") @PathVariable Long loanId) {
        return loanService.findLoanById(loanId)
                .map(loan -> {
                    List<PenaltyCalculation> calculations = loanPenaltyService.findPenaltyCalculationsByLoan(loan);
                    return ResponseEntity.ok(calculations);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère le montant total des pénalités pour un prêt.
     */
    @GetMapping("/{loanId}/total")
    @Operation(summary = "Calculer le total des pénalités", description = "Calcule le montant total des pénalités pour un prêt spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant total des pénalités calculé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Prêt non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<BigDecimal> getTotalPenalties(
            @Parameter(description = "ID du prêt") @PathVariable Long loanId) {
        return loanService.findLoanById(loanId)
                .map(loan -> {
                    BigDecimal totalPenalties = loanPenaltyService.calculateTotalPenalties(loan);
                    return ResponseEntity.ok(totalPenalties);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}