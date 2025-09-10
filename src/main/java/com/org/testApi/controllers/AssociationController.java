package com.org.testApi.controllers;

import com.org.testApi.models.Association;
import com.org.testApi.payload.AssociationPayload;
import com.org.testApi.services.AssociationService;
import com.org.testApi.mapper.AssociationMapper;
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
@RequestMapping("/api/associations")
@Tag(name = "Association", description = "Gestion des associations")
public class AssociationController {

    @Autowired
    private AssociationService associationService;

    @Autowired
    private AssociationMapper associationMapper;

    @GetMapping
    @Operation(summary = "Récupérer toutes les associations", description = "Retourne une liste de toutes les associations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des associations récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Association.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Association>> getAllAssociations() {
        List<Association> associations = associationService.getAllAssociations();
        return ResponseEntity.ok(associations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une association par ID", description = "Retourne une association spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Association trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Association.class))}),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Association> getAssociationById(
            @Parameter(description = "ID de l'association à récupérer") @PathVariable Long id) {
        return associationService.getAssociationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle association", description = "Crée une nouvelle association avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Association créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Association.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Association> createAssociation(
            @Parameter(description = "Données de l'association à créer") @RequestBody Association association) {
        Association savedAssociation = associationService.saveAssociation(association);
        return ResponseEntity.ok(savedAssociation);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer une association à partir d'un payload", description = "Crée une association en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Association créée avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Association.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Association> createAssociationFromPayload(
            @Parameter(description = "Données du payload pour créer l'association") @RequestBody AssociationPayload payload) {
        Association association = associationMapper.toEntityFromPayload(payload);
        Association savedAssociation = associationService.saveAssociation(association);
        return ResponseEntity.ok(savedAssociation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une association", description = "Met à jour une association existante avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Association mise à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Association.class))}),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Association> updateAssociation(
            @Parameter(description = "ID de l'association à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour de l'association") @RequestBody Association association) {
        try {
            Association updatedAssociation = associationService.updateAssociation(id, association);
            return ResponseEntity.ok(updatedAssociation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour une association avec payload", description = "Met à jour une association existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Association mise à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Association.class))}),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Association> updateAssociationWithPayload(
            @Parameter(description = "ID de l'association à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour l'association") @RequestBody AssociationPayload payload) {
        return associationService.getAssociationById(id)
                .map(association -> {
                    associationMapper.updateEntityFromPayload(payload, association);
                    Association updatedAssociation = associationService.updateAssociation(id, association);
                    return ResponseEntity.ok(updatedAssociation);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une association", description = "Supprime définitivement une association")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Association supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteAssociation(
            @Parameter(description = "ID de l'association à supprimer") @PathVariable Long id) {
        associationService.deleteAssociation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement une association", description = "Marque une association comme supprimée sans la retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Association supprimée logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteAssociation(
            @Parameter(description = "ID de l'association à supprimer logiquement") @PathVariable Long id) {
        associationService.softDeleteAssociation(id);
        return ResponseEntity.noContent().build();
    }
}
