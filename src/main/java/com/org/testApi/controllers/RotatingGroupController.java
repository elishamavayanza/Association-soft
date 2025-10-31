package com.org.testApi.controllers;

import com.org.testApi.models.RotatingGroup;
import com.org.testApi.payload.ResponsePayload;
import com.org.testApi.payload.RotatingGroupPayload;
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
            RotatingGroup updatedGroup = rotatingService.removeMembersFromGroup(groupId, memberIds);
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
}