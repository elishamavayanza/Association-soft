package com.org.testApi.controllers;

import com.org.testApi.models.MembershipFee;
import com.org.testApi.payload.MembershipFeePayload;
import com.org.testApi.services.MembershipFeeService;
import com.org.testApi.mapper.MembershipFeeMapper;
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
@RequestMapping("/api/membership-fees")
@Tag(name = "Cotisation", description = "Gestion des cotisations des membres")
public class MembershipFeeController {

    @Autowired
    private MembershipFeeService membershipFeeService;

    @Autowired
    private MembershipFeeMapper membershipFeeMapper;

    @GetMapping
    @Operation(summary = "Récupérer toutes les cotisations", description = "Retourne une liste de toutes les cotisations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des cotisations récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<MembershipFee>> getAllMembershipFees() {
        List<MembershipFee> membershipFees = membershipFeeService.getAllMembershipFees();
        return ResponseEntity.ok(membershipFees);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une cotisation par ID", description = "Retourne une cotisation spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "404", description = "Cotisation non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> getMembershipFeeById(
            @Parameter(description = "ID de la cotisation à récupérer") @PathVariable Long id) {
        return membershipFeeService.getMembershipFeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle cotisation", description = "Crée une nouvelle cotisation avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> createMembershipFee(
            @Parameter(description = "Données de la cotisation à créer") @RequestBody MembershipFee membershipFee) {
        MembershipFee savedMembershipFee = membershipFeeService.saveMembershipFee(membershipFee);
        return ResponseEntity.ok(savedMembershipFee);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer une cotisation à partir d'un payload", description = "Crée une cotisation en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation créée avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> createMembershipFeeFromPayload(
            @Parameter(description = "Données du payload pour créer la cotisation") @RequestBody MembershipFeePayload payload) {
        MembershipFee membershipFee = membershipFeeMapper.toEntityFromPayload(payload);
        MembershipFee savedMembershipFee = membershipFeeService.saveMembershipFee(membershipFee);
        return ResponseEntity.ok(savedMembershipFee);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une cotisation", description = "Met à jour une cotisation existante avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation mise à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "404", description = "Cotisation non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> updateMembershipFee(
            @Parameter(description = "ID de la cotisation à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour de la cotisation") @RequestBody MembershipFee membershipFee) {
        try {
            MembershipFee updatedMembershipFee = membershipFeeService.updateMembershipFee(id, membershipFee);
            return ResponseEntity.ok(updatedMembershipFee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour une cotisation avec payload", description = "Met à jour une cotisation existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation mise à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "404", description = "Cotisation non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> updateMembershipFeeWithPayload(
            @Parameter(description = "ID de la cotisation à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour la cotisation") @RequestBody MembershipFeePayload payload) {
        return membershipFeeService.getMembershipFeeById(id)
                .map(membershipFee -> {
                    membershipFeeMapper.updateEntityFromPayload(payload, membershipFee);
                    MembershipFee updatedMembershipFee = membershipFeeService.updateMembershipFee(id, membershipFee);
                    return ResponseEntity.ok(updatedMembershipFee);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une cotisation", description = "Supprime définitivement une cotisation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cotisation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Cotisation non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteMembershipFee(
            @Parameter(description = "ID de la cotisation à supprimer") @PathVariable Long id) {
        membershipFeeService.deleteMembershipFee(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement une cotisation", description = "Marque une cotisation comme supprimée sans la retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cotisation supprimée logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Cotisation non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteMembershipFee(
            @Parameter(description = "ID de la cotisation à supprimer logiquement") @PathVariable Long id) {
        membershipFeeService.softDeleteMembershipFee(id);
        return ResponseEntity.noContent().build();
    }
}
