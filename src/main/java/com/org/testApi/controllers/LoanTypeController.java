package com.org.testApi.controllers;

import com.org.testApi.models.LoanType;
import com.org.testApi.services.LoanTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-types")
@Tag(name = "Type de Prêt", description = "Gestion des types de prêts")
public class LoanTypeController {

    @Autowired
    private LoanTypeService loanTypeService;

    /**
     * Crée un nouveau type de prêt.
     */
    @PostMapping
    @Operation(summary = "Créer un type de prêt", description = "Crée un nouveau type de prêt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type de prêt créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanType.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<LoanType> createLoanType(
            @Parameter(description = "Données du type de prêt")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Exemple de données pour créer un type de prêt",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                @ExampleObject(
                                    name = "Type de prêt agricole",
                                    summary = "Exemple de type de prêt agricole",
                                    value = "{\n" +
                                            "  \"name\": \"Prêt Agricole\",\n" +
                                            "  \"description\": \"Prêt destiné aux activités agricoles\",\n" +
                                            "  \"monthlyInterestRate\": 1.8,\n" +
                                            "  \"maxPenaltyRate\": 0.3,\n" +
                                            "  \"gracePeriodDays\": 7,\n" +
                                            "  \"category\": \"Agricole\",\n" +
                                            "  \"subCategory\": \"Saison principal\",\n" +
                                            "  \"active\": true\n" +
                                            "}"
                                )
                            }
                    )
            )
            @RequestBody LoanType loanType) {
        LoanType createdLoanType = loanTypeService.createLoanType(loanType);
        return ResponseEntity.ok(createdLoanType);
    }

    /**
     * Récupère un type de prêt par son ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un type de prêt par ID", description = "Retourne un type de prêt spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type de prêt trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanType.class))}),
            @ApiResponse(responseCode = "404", description = "Type de prêt non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<LoanType> getLoanType(
            @Parameter(description = "ID du type de prêt à récupérer") @PathVariable Long id) {
        return loanTypeService.findLoanTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les types de prêt.
     */
    @GetMapping
    @Operation(summary = "Récupérer tous les types de prêt", description = "Retourne une liste de tous les types de prêt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des types de prêt récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanType.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<LoanType>> getAllLoanTypes() {
        List<LoanType> loanTypes = loanTypeService.findAllLoanTypes();
        return ResponseEntity.ok(loanTypes);
    }

    /**
     * Récupère tous les types de prêt actifs.
     */
    @GetMapping("/active")
    @Operation(summary = "Récupérer les types de prêt actifs", description = "Retourne une liste de tous les types de prêt actifs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des types de prêt actifs récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanType.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<LoanType>> getActiveLoanTypes() {
        List<LoanType> loanTypes = loanTypeService.findActiveLoanTypes();
        return ResponseEntity.ok(loanTypes);
    }

    /**
     * Met à jour un type de prêt.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un type de prêt", description = "Met à jour un type de prêt existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type de prêt mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanType.class))}),
            @ApiResponse(responseCode = "404", description = "Type de prêt non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<LoanType> updateLoanType(
            @Parameter(description = "ID du type de prêt à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du type de prêt mis à jour") @RequestBody LoanType loanType) {
        LoanType updatedLoanType = loanTypeService.updateLoanType(id, loanType);
        return ResponseEntity.ok(updatedLoanType);
    }

    /**
     * Supprime un type de prêt.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un type de prêt", description = "Supprime un type de prêt (le marque comme inactif)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Type de prêt supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Type de prêt non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteLoanType(
            @Parameter(description = "ID du type de prêt à supprimer") @PathVariable Long id) {
        loanTypeService.deleteLoanType(id);
        return ResponseEntity.noContent().build();
    }
}