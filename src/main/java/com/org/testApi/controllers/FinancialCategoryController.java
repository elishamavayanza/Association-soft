package com.org.testApi.controllers;

import com.org.testApi.models.FinancialCategory;
import com.org.testApi.payload.FinancialCategoryPayload;
import com.org.testApi.services.FinancialCategoryService;
import com.org.testApi.mapper.FinancialCategoryMapper;
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

import java.util.List;

@RestController
@RequestMapping("/api/financial-categories")
@Tag(name = "Catégorie financière", description = "Gestion des catégories financières")
public class FinancialCategoryController {

    @Autowired
    private FinancialCategoryService financialCategoryService;

    @Autowired
    private FinancialCategoryMapper financialCategoryMapper;

    @GetMapping
    @Operation(summary = "Récupérer toutes les catégories financières", description = "Retourne une liste de toutes les catégories financières")
    @ApiResponses(value = {
@ApiResponse(responseCode = "200", description = "Liste des catégories financières récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialCategory.class))}),
                                    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<FinancialCategory>> getAllFinancialCategories() {
        List<FinancialCategory> financialCategories = financialCategoryService.getAllFinancialCategories();
        return ResponseEntity.ok(financialCategories);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Récupérer une catégorie financière par ID", description = "Retourne une catégorie financière spécifique en fonction de son ID")
    @ApiResponses(value = {
                                    @ApiResponse(responseCode = "200", description = "Catégorie financière trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialCategory.class))}),
            @ApiResponse(responseCode = "404", description = "Catégorie financière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<FinancialCategory> getFinancialCategoryById(
            @Parameter(description = "ID de la catégorie financière à récupérer") @PathVariable Long id) {
        return financialCategoryService.getFinancialCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

           @PostMapping
    @Operation(summary = "Créer une nouvelle catégorie financière", description = "Crée une nouvelle catégorie financière avec les données fournies")
    @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Catégorie financière créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialCategory.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
                        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<FinancialCategory> createFinancialCategory(
            @Parameter(description = "Données de la catégorie financière à créer") @RequestBody FinancialCategory financialCategory) {
        FinancialCategory savedFinancialCategory = financialCategoryService.saveFinancialCategory(financialCategory);
        return ResponseEntity.ok(savedFinancialCategory);
    }

    @PostMapping("/payload")
            @Operation(summary = "Créer une catégorie financière à partir d'un payload", description = "Crée une catégorie financière en utilisant un objet payload")
    @ApiResponses(value = {
                                                @ApiResponse(responseCode = "200", description = "Catégorie financière créée avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialCategory.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
                public ResponseEntity<?> createFinancialCategoryFromPayload(
            @Parameter(description = "Données du payload pour créer la catégorie financière") @RequestBody FinancialCategoryPayload payload) {
        try {
            FinancialCategory financialCategory = financialCategoryMapper.toEntityFromPayload(payload);
            
            // Check if association exists
                        if (financialCategory.getAssociation() == null && payload.getAssociationId() != null) {
                return ResponseEntity.status(404).body("Association not found with ID: " + payload.getAssociationId());
            }
            
            FinancialCategory savedFinancialCategory = financialCategoryService.saveFinancialCategory(financialCategory);
            return ResponseEntity.ok(savedFinancialCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

            @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une catégorie financière", description = "Met à jour une catégorie financière existante avec les données fournies")
    @ApiResponses(value = {
                                                @ApiResponse(responseCode = "200", description = "Catégorie financière mise à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialCategory.class))}),
                                    @ApiResponse(responseCode = "404", description = "Catégorie financière non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<FinancialCategory> updateFinancialCategory(
            @Parameter(description = "ID de la catégorie financière à mettre à jour") @PathVariable Long id,
                                                           @Parameter(description = "Données de mise à jour de la catégorie financière") @RequestBody FinancialCategory financialCategory) {
        try {
            FinancialCategory updatedFinancialCategory = financialCategoryService.updateFinancialCategory(id, financialCategory);
            return ResponseEntity.ok(updatedFinancialCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour une catégorie financière avec payload", description = "Met à jour une catégorie financière existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie financière mise à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                           schema = @Schema(implementation = FinancialCategory.class))}),
            @ApiResponse(responseCode = "404", description = "Catégorie financière non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> updateFinancialCategoryWithPayload(
            @Parameter(description = "ID de la catégorie financière à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour la catégorie financière") @RequestBody FinancialCategoryPayload payload) {
        try {
            return financialCategoryService.getFinancialCategoryById(id)
                    .map(financialCategory -> {
                        financialCategoryMapper.updateEntityFromPayload(payload, financialCategory);
                        
                        // Check if association exists
                        if (financialCategory.getAssociation() == null && payload.getAssociationId() != null) {
                            return ResponseEntity.status(404).body("Association not found with ID: " + payload.getAssociationId());
                        }
                        
                        FinancialCategory updatedFinancialCategory = financialCategoryService.updateFinancialCategory(id, financialCategory);
                        return ResponseEntity.ok(updatedFinancialCategory);
                    })
                    .orElse(ResponseEntity.notFound().build());
                } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie financière", description = "Supprime définitivement une catégorie financière")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Catégorie financière supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Catégorie financière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
                   public ResponseEntity<Void> deleteFinancialCategory(
            @Parameter(description = "ID de la catégorie financière à supprimer") @PathVariable Long id) {
        financialCategoryService.deleteFinancialCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement une catégorie financière", description = "Marque une catégorie financière comme supprimée sans la retirer de la base de données")
    @ApiResponses(value = {
                                    @ApiResponse(responseCode = "204", description = "Catégorie financière supprimée logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Catégorie financière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteFinancialCategory(
            @Parameter(description = "ID de la catégorie financière à supprimer logiquement") @PathVariable Long id) {
        financialCategoryService.softDeleteFinancialCategory(id);
        return ResponseEntity.noContent().build();
    }
}
