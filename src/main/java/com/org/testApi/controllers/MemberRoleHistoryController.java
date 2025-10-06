package com.org.testApi.controllers;

import com.org.testApi.models.MemberRoleHistory;
import com.org.testApi.payload.MemberRoleHistoryPayload;
import com.org.testApi.services.MemberRoleHistoryService;
import com.org.testApi.services.MemberService;
import com.org.testApi.mapper.MemberRoleHistoryMapper;
import com.org.testApi.models.Member;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/member-role-histories")
@Tag(name = "Historique des rôles de membre", description = "Gestion de l'historique des rôles des membres")
public class MemberRoleHistoryController {

    @Autowired
    private MemberRoleHistoryService memberRoleHistoryService;
    
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRoleHistoryMapper memberRoleHistoryMapper;

    @GetMapping
    @Operation(summary = "Récupérer tous les historiques de rôles de membres", description = "Retourne une liste de tous les historiques de rôles de membres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des historiques de rôles de membres récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRoleHistory.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<MemberRoleHistory>> getAllMemberRoleHistories() {
        List<MemberRoleHistory> memberRoleHistories = memberRoleHistoryService.getAllMemberRoleHistories();
        return ResponseEntity.ok(memberRoleHistories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un historique de rôle de membre par ID", description = "Retourne un historique de rôle de membre spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique de rôle de membre trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRoleHistory.class))}),
            @ApiResponse(responseCode = "404", description = "Historique de rôle de membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberRoleHistory> getMemberRoleHistoryById(
            @Parameter(description = "ID de l'historique de rôle de membre à récupérer") @PathVariable Long id) {
        return memberRoleHistoryService.getMemberRoleHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel historique de rôle de membre", description = "Crée un nouvel historique de rôle de membre avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique de rôle de membre créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRoleHistory.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberRoleHistory> createMemberRoleHistory(
            @Parameter(description = "Données de l'historique de rôle de membre à créer") @RequestBody MemberRoleHistory memberRoleHistory) {
        MemberRoleHistory savedMemberRoleHistory = memberRoleHistoryService.saveMemberRoleHistory(memberRoleHistory);
        return ResponseEntity.ok(savedMemberRoleHistory);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un historique de rôle de membre à partir d'un payload", description = "Crée un historique de rôle de membre en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique de rôle de membre créé avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRoleHistory.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberRoleHistory> createMemberRoleHistoryFromPayload(
            @Parameter(description = "Données du payload pour créer l'historique de rôle de membre") @RequestBody MemberRoleHistoryPayload payload) {
        // Check if member exists
        Optional<Member> member = memberService.getMemberById(payload.getMemberId());
        if (member.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        MemberRoleHistory memberRoleHistory = memberRoleHistoryMapper.toEntityFromPayload(payload);
        memberRoleHistory.setMember(member.get());
        MemberRoleHistory savedMemberRoleHistory = memberRoleHistoryService.saveMemberRoleHistory(memberRoleHistory);
        return ResponseEntity.ok(savedMemberRoleHistory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un historique de rôle de membre", description = "Met à jour un historique de rôle de membre existant avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique de rôle de membre mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRoleHistory.class))}),
            @ApiResponse(responseCode = "404", description = "Historique de rôle de membre non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberRoleHistory> updateMemberRoleHistory(
            @Parameter(description = "ID de l'historique de rôle de membre à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour de l'historique de rôle de membre") @RequestBody MemberRoleHistory memberRoleHistory) {
        try {
            MemberRoleHistory updatedMemberRoleHistory = memberRoleHistoryService.updateMemberRoleHistory(id, memberRoleHistory);
            return ResponseEntity.ok(updatedMemberRoleHistory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour un historique de rôle de membre avec payload", description = "Met à jour un historique de rôle de membre existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique de rôle de membre mis à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberRoleHistory.class))}),
            @ApiResponse(responseCode = "404", description = "Historique de rôle de membre non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberRoleHistory> updateMemberRoleHistoryWithPayload(
            @Parameter(description = "ID de l'historique de rôle de membre à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour l'historique de rôle de membre") @RequestBody MemberRoleHistoryPayload payload) {
        return memberRoleHistoryService.getMemberRoleHistoryById(id)
                .map(memberRoleHistory -> {
                    // Check if member exists when memberId is provided in payload
                    if (payload.getMemberId() != null) {
                        Optional<Member> member = memberService.getMemberById(payload.getMemberId());
                        if (member.isPresent()) {
                            memberRoleHistory.setMember(member.get());
                        }
                    }
                    memberRoleHistoryMapper.updateEntityFromPayload(payload, memberRoleHistory);
                    MemberRoleHistory updatedMemberRoleHistory = memberRoleHistoryService.updateMemberRoleHistory(id, memberRoleHistory);
                    return ResponseEntity.ok(updatedMemberRoleHistory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un historique de rôle de membre", description = "Supprime définitivement un historique de rôle de membre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Historique de rôle de membre supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Historique de rôle de membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteMemberRoleHistory(
            @Parameter(description = "ID de l'historique de rôle de membre à supprimer") @PathVariable Long id) {
        memberRoleHistoryService.deleteMemberRoleHistory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement un historique de rôle de membre", description = "Marque un historique de rôle de membre comme supprimé sans le retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Historique de rôle de membre supprimé logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Historique de rôle de membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteMemberRoleHistory(
            @Parameter(description = "ID de l'historique de rôle de membre à supprimer logiquement") @PathVariable Long id) {
        memberRoleHistoryService.softDeleteMemberRoleHistory(id);
        return ResponseEntity.noContent().build();
    }
}