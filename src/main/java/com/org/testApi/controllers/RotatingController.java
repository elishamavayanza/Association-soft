package com.org.testApi.controllers;

import com.org.testApi.dto.*;
import com.org.testApi.mapper.*;
import com.org.testApi.models.*;
import com.org.testApi.payload.*;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rotating")
@Tag(name = "Likelemba (Système de rotation financière)", description = "Gestion du système rotatif Likelemba")
public class RotatingController {
    
    @Autowired
    private RotatingService rotatingService;
    
    @Autowired
    private RotatingGroupMapper rotatingGroupMapper;
    
    @Autowired
    private RoundMapper roundMapper;
    
    @Autowired
    private ContributionMapper contributionMapper;
    
    @Autowired
    private PenaltyMapper penaltyMapper;

    // Rotating Group endpoints
    @PostMapping("/groups")
    @Operation(summary = "Créer un groupe de rotation", description = "Crée un nouveau groupe de rotation Likelemba")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RotatingGroupDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RotatingGroupDTO> createRotatingGroup(
            @Parameter(description = "Nom du groupe") @RequestParam String name,
            @Parameter(description = "Description du groupe") @RequestParam String description,
            @Parameter(description = "Montant de contribution") @RequestParam BigDecimal contributionAmount,
            @Parameter(description = "Nombre maximum de membres") @RequestParam Integer maxMembers,
            @Parameter(description = "Fréquence de rotation") @RequestParam String rotationFrequency,
            @Parameter(description = "Date de début") @RequestParam LocalDate startDate) {
        
        RotatingGroup rotatingGroup = rotatingService.createRotatingGroup(
                name, description, contributionAmount, maxMembers, rotationFrequency, startDate);
        return ResponseEntity.ok(rotatingGroupMapper.toDTO(rotatingGroup));
    }

    @GetMapping("/groups/{id}")
    @Operation(summary = "Récupérer un groupe par ID", description = "Retourne un groupe de rotation spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RotatingGroupDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RotatingGroupDTO> getRotatingGroup(@PathVariable Long id) {
        return rotatingService.findRotatingGroupById(id)
                .map(group -> ResponseEntity.ok(rotatingGroupMapper.toDTO(group)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/groups")
    @Operation(summary = "Récupérer tous les groupes", description = "Retourne la liste de tous les groupes de rotation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des groupes récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RotatingGroupDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<RotatingGroupDTO>> getAllRotatingGroups() {
        List<RotatingGroup> groups = rotatingService.findAllRotatingGroups();
        return ResponseEntity.ok(rotatingGroupMapper.toDTOList(groups));
    }

    @PutMapping("/groups/{id}")
    @Operation(summary = "Mettre à jour un groupe", description = "Met à jour un groupe de rotation existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RotatingGroupDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RotatingGroupDTO> updateRotatingGroup(@PathVariable Long id, 
                                                              @RequestBody RotatingGroupDTO rotatingGroupDTO) {
        RotatingGroup rotatingGroup = rotatingGroupMapper.toEntity(rotatingGroupDTO);
        RotatingGroup updatedGroup = rotatingService.updateRotatingGroup(id, rotatingGroup);
        return ResponseEntity.ok(rotatingGroupMapper.toDTO(updatedGroup));
    }

    @DeleteMapping("/groups/{id}")
    @Operation(summary = "Supprimer un groupe", description = "Supprime un groupe de rotation par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Groupe supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteRotatingGroup(@PathVariable Long id) {
        rotatingService.deleteRotatingGroup(id);
        return ResponseEntity.noContent().build();
    }

    // Round endpoints
    @PostMapping("/rounds")
    @Operation(summary = "Créer un tour", description = "Crée un nouveau tour dans un groupe de rotation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoundDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RoundDTO> createRound(
            @Parameter(description = "ID du groupe") @RequestParam Long rotatingGroupId,
            @Parameter(description = "Numéro du tour") @RequestParam Integer roundNumber,
            @Parameter(description = "Date de début") @RequestParam LocalDate startDate,
            @Parameter(description = "Date de fin") @RequestParam LocalDate endDate) {
        
        Round round = rotatingService.createRound(rotatingGroupId, roundNumber, startDate, endDate);
        return ResponseEntity.ok(roundMapper.toDTO(round));
    }

    @GetMapping("/rounds/{id}")
    @Operation(summary = "Récupérer un tour par ID", description = "Retourne un tour spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoundDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Tour non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RoundDTO> getRound(@PathVariable Long id) {
        return rotatingService.findRoundById(id)
                .map(round -> ResponseEntity.ok(roundMapper.toDTO(round)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/rounds/{id}")
    @Operation(summary = "Mettre à jour un tour", description = "Met à jour un tour existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoundDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Tour non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RoundDTO> updateRound(@PathVariable Long id, @RequestBody RoundDTO roundDTO) {
        Round round = roundMapper.toEntity(roundDTO);
        Round updatedRound = rotatingService.updateRound(id, round);
        return ResponseEntity.ok(roundMapper.toDTO(updatedRound));
    }

    @DeleteMapping("/rounds/{id}")
    @Operation(summary = "Supprimer un tour", description = "Supprime un tour par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tour supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Tour non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteRound(@PathVariable Long id) {
        rotatingService.deleteRound(id);
        return ResponseEntity.noContent().build();
    }

    // Contribution endpoints
    @PostMapping("/contributions")
    @Operation(summary = "Faire une contribution", description = "Enregistre une contribution d'un membre pour un tour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contribution créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContributionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ContributionDTO> makeContribution(
            @Parameter(description = "ID du membre") @RequestParam Long memberId,
            @Parameter(description = "ID du tour") @RequestParam Long roundId,
            @Parameter(description = "Montant") @RequestParam BigDecimal amount,
            @Parameter(description = "Date de contribution") @RequestParam LocalDate contributionDate) {
        
        Contribution contribution = rotatingService.makeContribution(memberId, roundId, amount, contributionDate);
        return ResponseEntity.ok(contributionMapper.toDTO(contribution));
    }

    @GetMapping("/contributions/{id}")
    @Operation(summary = "Récupérer une contribution par ID", description = "Retourne une contribution spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contribution trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContributionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Contribution non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ContributionDTO> getContribution(@PathVariable Long id) {
        return rotatingService.findContributionById(id)
                .map(contribution -> ResponseEntity.ok(contributionMapper.toDTO(contribution)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/contributions/{id}")
    @Operation(summary = "Mettre à jour une contribution", description = "Met à jour une contribution existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contribution mise à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContributionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Contribution non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ContributionDTO> updateContribution(@PathVariable Long id, 
                                                            @RequestBody ContributionDTO contributionDTO) {
        Contribution contribution = contributionMapper.toEntity(contributionDTO);
        Contribution updatedContribution = rotatingService.updateContribution(id, contribution);
        return ResponseEntity.ok(contributionMapper.toDTO(updatedContribution));
    }

    @DeleteMapping("/contributions/{id}")
    @Operation(summary = "Supprimer une contribution", description = "Supprime une contribution par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contribution supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Contribution non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteContribution(@PathVariable Long id) {
        rotatingService.deleteContribution(id);
        return ResponseEntity.noContent().build();
    }

    // Penalty endpoints
    @PostMapping("/penalties")
    @Operation(summary = "Appliquer une pénalité", description = "Applique une pénalité à un membre pour un tour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pénalité appliquée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PenaltyDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<PenaltyDTO> applyPenalty(
            @Parameter(description = "ID du membre") @RequestParam Long memberId,
            @Parameter(description = "ID du tour") @RequestParam Long roundId,
            @Parameter(description = "Montant") @RequestParam BigDecimal amount,
            @Parameter(description = "Raison") @RequestParam String reason,
            @Parameter(description = "Type de pénalité") @RequestParam String penaltyType,
            @Parameter(description = "Date de pénalité") @RequestParam LocalDate penaltyDate) {
        
        Penalty penalty = rotatingService.applyPenalty(memberId, roundId, amount, reason, penaltyType, penaltyDate);
        return ResponseEntity.ok(penaltyMapper.toDTO(penalty));
    }

    @GetMapping("/penalties/{id}")
    @Operation(summary = "Récupérer une pénalité par ID", description = "Retourne une pénalité spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pénalité trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PenaltyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Pénalité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<PenaltyDTO> getPenalty(@PathVariable Long id) {
        return rotatingService.findPenaltyById(id)
                .map(penalty -> ResponseEntity.ok(penaltyMapper.toDTO(penalty)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/penalties/{id}")
    @Operation(summary = "Mettre à jour une pénalité", description = "Met à jour une pénalité existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pénalité mise à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PenaltyDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Pénalité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<PenaltyDTO> updatePenalty(@PathVariable Long id, @RequestBody PenaltyDTO penaltyDTO) {
        Penalty penalty = penaltyMapper.toEntity(penaltyDTO);
        Penalty updatedPenalty = rotatingService.updatePenalty(id, penalty);
        return ResponseEntity.ok(penaltyMapper.toDTO(updatedPenalty));
    }

    @DeleteMapping("/penalties/{id}")
    @Operation(summary = "Supprimer une pénalité", description = "Supprime une pénalité par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pénalité supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Pénalité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deletePenalty(@PathVariable Long id) {
        rotatingService.deletePenalty(id);
        return ResponseEntity.noContent().build();
    }

    // Utility endpoints
    @GetMapping("/members/{memberId}/total-contributions")
    @Operation(summary = "Calculer le total des contributions", description = "Calcule le montant total des contributions pour un membre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant total calculé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<BigDecimal> getTotalContributionsForMember(@PathVariable Long memberId) {
        BigDecimal total = rotatingService.calculateTotalContributionsForMember(memberId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/members/{memberId}/total-penalties")
    @Operation(summary = "Calculer le total des pénalités", description = "Calcule le montant total des pénalités pour un membre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant total calculé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<BigDecimal> getTotalPenaltiesForMember(@PathVariable Long memberId) {
        BigDecimal total = rotatingService.calculateTotalPenaltiesForMember(memberId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/members/{memberId}/remaining-amount")
    @Operation(summary = "Calculer le montant restant", description = "Calcule le montant restant pour un membre (contributions - pénalités)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant restant calculé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<BigDecimal> getRemainingAmountForMember(@PathVariable Long memberId) {
        BigDecimal remaining = rotatingService.calculateRemainingAmountForMember(memberId);
        return ResponseEntity.ok(remaining);
    }

    // Payload-based endpoints
    @PostMapping("/groups/payload")
    @Operation(summary = "Créer un groupe avec payload", description = "Crée un groupe de rotation à partir d'un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RotatingGroupDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RotatingGroupDTO> createRotatingGroupWithPayload(@RequestBody RotatingGroupPayload payload) {
        RotatingGroup rotatingGroup = rotatingService.createRotatingGroup(
                payload.getName(),
                payload.getDescription(),
                payload.getContributionAmount(),
                payload.getMaxMembers(),
                payload.getRotationFrequency(),
                payload.getStartDate());
        return ResponseEntity.ok(rotatingGroupMapper.toDTO(rotatingGroup));
    }

    @PutMapping("/groups/{id}/payload")
    @Operation(summary = "Mettre à jour un groupe avec payload", description = "Met à jour un groupe de rotation à partir d'un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groupe mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RotatingGroupDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<RotatingGroupDTO> updateRotatingGroupWithPayload(@PathVariable Long id, 
                                                                         @RequestBody RotatingGroupPayload payload) {
        RotatingGroup rotatingGroup = rotatingGroupMapper.toEntity(rotatingGroupMapper.toDTO(payload));
        rotatingGroup.setId(id);
        RotatingGroup updatedGroup = rotatingService.updateRotatingGroup(id, rotatingGroup);
        return ResponseEntity.ok(rotatingGroupMapper.toDTO(updatedGroup));
    }
}