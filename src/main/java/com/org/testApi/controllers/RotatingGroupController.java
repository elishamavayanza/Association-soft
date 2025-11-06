package com.org.testApi.controllers;

import com.org.testApi.models.Member;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.Round;
import com.org.testApi.payload.ResponsePayload;
import com.org.testApi.services.GroupStatusManagementService;
import com.org.testApi.services.RotatingService;
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
@RequestMapping("/api/rotating-groups")
@Tag(name = "Groupes de rotation", description = "Gestion des groupes de rotation financière")
public class RotatingGroupController {

    @Autowired
    private RotatingService rotatingService;
    
    @Autowired
    private GroupStatusManagementService groupStatusManagementService;

    @PostMapping("/{groupId}/members")
    @Operation(summary = "Ajouter des membres à un groupe de rotation", 
               description = "Ajoute un ou plusieurs membres à un groupe de rotation financière existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Membres ajoutés avec succès au groupe",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = RotatingGroup.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<RotatingGroup>> addMembersToGroup(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId,
            @Parameter(description = "Liste des IDs des membres à ajouter") @RequestBody List<Long> memberIds) {
        
        try {
            RotatingGroup updatedGroup = rotatingService.addMembersToGroup(groupId, memberIds);
            ResponsePayload<RotatingGroup> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Membres ajoutés avec succès au groupe");
            response.setData(updatedGroup);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<RotatingGroup> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/{groupId}/members")
    @Operation(summary = "Supprimer des membres d'un groupe de rotation", 
               description = "Supprime un ou plusieurs membres d'un groupe de rotation financière existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Membres supprimés avec succès du groupe",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = RotatingGroup.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<RotatingGroup>> removeMembersFromGroup(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId,
            @Parameter(description = "Liste des IDs des membres à supprimer") @RequestBody List<Long> memberIds) {
        
        try {
            RotatingGroup updatedGroup = rotatingService.removeMembersFromGroup(groupId,memberIds);
            ResponsePayload<RotatingGroup> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Membres supprimés avec succès du groupe");
            response.setData(updatedGroup);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<RotatingGroup> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/{groupId}/auto-generate")
    @Operation(summary = "Configurer la génération automatique des tours", 
               description = "Active ou désactive la génération automatique des tours pour un groupe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paramètre mis à jour avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = RotatingGroup.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<RotatingGroup>> setAutoGenerateRounds(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId,
            @Parameter(description = "Activation/désactivation de la génération automatique") @RequestBody Boolean autoGenerate) {
        
        try {
            RotatingGroup updatedGroup = rotatingService.setAutoGenerateRounds(groupId, autoGenerate);
            ResponsePayload<RotatingGroup> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Paramètre de génération automatique mis à jour avec succès");
            response.setData(updatedGroup);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<RotatingGroup> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Round beneficiary management endpoints
    @PostMapping("/rounds/{roundId}/beneficiaries")
    @Operation(summary = "Attribuer des bénéficiaires à un tour", 
               description = "Attribue un ou plusieurs membres comme bénéficiaires d'un tour de rotation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bénéficiaires attribués avec succès au tour",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Round.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Tour non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<Round>> assignBeneficiariesToRound(
            @Parameter(description = "ID du tour") @PathVariable Long roundId,
            @Parameter(description = "Liste des IDs des membres bénéficiaires") @RequestBody List<Long> beneficiaryIds) {
        
        try {
            Round updatedRound = rotatingService.assignBeneficiariesToRound(roundId, beneficiaryIds);
            ResponsePayload<Round> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Bénéficiaires attribués avec succès au tour");
            response.setData(updatedRound);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<Round> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/rounds/{roundId}/beneficiaries")
    @Operation(summary = "Obtenir les bénéficiaires d'un tour", 
               description = "Récupère la liste des membres bénéficiaires d'un tour de rotation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bénéficiaires récupérés avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Member.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Tour non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<List<Member>>> getRoundBeneficiaries(
            @Parameter(description = "ID du tour") @PathVariable Long roundId) {
        
        try {
            List<Member> beneficiaries = rotatingService.getRoundBeneficiaries(roundId);
            ResponsePayload<List<Member>> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Bénéficiaires récupérés avec succès");
            response.setData(beneficiaries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<List<Member>> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/rounds/{roundId}/distribute")
    @Operation(summary = "Distribuer les fonds aux bénéficiaires", 
               description = "Distribue les fonds collectés aux bénéficiaires et envoie des notifications")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fonds distribués avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Round.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Tour non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<Round>> distributeFundsToBeneficiaries(
            @Parameter(description = "ID du tour") @PathVariable Long roundId) {
        
        try {
            Round updatedRound = rotatingService.distributeFundsToBeneficiaries(roundId);
            ResponsePayload<Round> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Fonds distribués avec succès aux bénéficiaires");
            response.setData(updatedRound);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<Round> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/rounds/{roundId}/auto-select-beneficiaries")
    @Operation(summary = "Sélectionner automatiquement les bénéficiaires", 
               description = "Sélectionne automatiquement les bénéficiaires selon un algorithme équitable")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bénéficiaires sélectionnés avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Round.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Tour non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<Round>> autoSelectBeneficiaries(
            @Parameter(description = "ID du tour") @PathVariable Long roundId) {
        
        try {
            Round updatedRound = rotatingService.selectBeneficiariesAutomatically(roundId);
            ResponsePayload<Round> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Bénéficiaires sélectionnés automatiquement avec succès");
            response.setData(updatedRound);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<Round> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/{groupId}/generate-rounds")
    @Operation(summary = "Générer automatiquement les tours", 
               description = "Génère automatiquement les tours en fonction de la fréquence de rotation du groupe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tours générés avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Round.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<List<Round>>> generateRoundsForGroup(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId) {
        
        try {
            rotatingService.generateRoundsForGroup(groupId);
            List<Round> rounds = rotatingService.findRoundsByRotatingGroup(groupId);
            ResponsePayload<List<Round>> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Tours générés avec succès pour le groupe");
            response.setData(rounds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<List<Round>> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/update-statuses")
    @Operation(summary = "Mettre à jour les statuts des groupes", 
               description = "Met à jour automatiquement les statuts de tous les groupes selon les critères définis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statuts mis à jour avec succès"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<String>> updateAllGroupStatuses() {
        try {
            groupStatusManagementService.updateGroupStatuses();
            ResponsePayload<String> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Statuts des groupes mis à jour avec succès");
            response.setData("Mise à jour terminée");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<String> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage("Erreur lors de la mise à jour des statuts: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(500).body(response);
        }
    }
}