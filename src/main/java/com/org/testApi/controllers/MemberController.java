package com.org.testApi.controllers;

import com.org.testApi.models.Member;
import com.org.testApi.payload.MemberPayload;
import com.org.testApi.services.MemberService;
import com.org.testApi.mapper.MemberMapper;
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
@RequestMapping("/api/members")
@Tag(name = "Membre", description = "Gestion des membres")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @GetMapping
    @Operation(summary = "Récupérer tous les membres", description = "Retourne une liste de tous les membres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des membres récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un membre par ID", description = "Retourne un membre spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Member> getMemberById(
            @Parameter(description = "ID du membre à récupérer") @PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau membre", description = "Crée un nouveau membre avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Member> createMember(
            @Parameter(description = "Données du membre à créer") @RequestBody Member member) {
        Member savedMember = memberService.saveMember(member);
        return ResponseEntity.ok(savedMember);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un membre à partir d'un payload", description = "Crée un membre en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre créé avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Member> createMemberFromPayload(
            @Parameter(description = "Données du payload pour créer le membre") @RequestBody MemberPayload payload) {
        Member member = memberMapper.toEntityFromPayload(payload);
        Member savedMember = memberService.saveMember(member);
        return ResponseEntity.ok(savedMember);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un membre", description = "Met à jour un membre existant avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Member> updateMember(
            @Parameter(description = "ID du membre à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour du membre") @RequestBody Member member) {
        try {
            Member updatedMember = memberService.updateMember(id, member);
            return ResponseEntity.ok(updatedMember);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour un membre avec payload", description = "Met à jour un membre existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre mis à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Member> updateMemberWithPayload(
            @Parameter(description = "ID du membre à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour le membre") @RequestBody MemberPayload payload) {
        return memberService.getMemberById(id)
                .map(member -> {
                    memberMapper.updateEntityFromPayload(payload, member);
                    Member updatedMember = memberService.updateMember(id, member);
                    return ResponseEntity.ok(updatedMember);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un membre", description = "Supprime définitivement un membre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membre supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteMember(
            @Parameter(description = "ID du membre à supprimer") @PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement un membre", description = "Marque un membre comme supprimé sans le retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membre supprimé logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteMember(
            @Parameter(description = "ID du membre à supprimer logiquement") @PathVariable Long id) {
        memberService.softDeleteMember(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recherche des membres avec des filtres complexes.
     */
    @GetMapping("/search")
    @Operation(summary = "Rechercher des membres", description = "Recherche des membres avec des filtres complexes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Member>> searchMembers(
            @Parameter(description = "Nom du membre (optionnel)") @RequestParam(required = false) String name,
            @Parameter(description = "Email du membre (optionnel)") @RequestParam(required = false) String email,
            @Parameter(description = "Type de membre (optionnel)") @RequestParam(required = false) Member.MemberType memberType,
            @Parameter(description = "ID de l'association (optionnel)") @RequestParam(required = false) Long associationId,
            @Parameter(description = "Statut d'activité (optionnel)") @RequestParam(required = false) Boolean isActive) {
        // Note: Pour une implémentation complète, vous devriez ajouter cette méthode au service
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    /**
     * Vérifie si un membre est éligible pour emprunter.
     */
    @GetMapping("/{id}/eligible")
    @Operation(summary = "Vérifier l'éligibilité d'un membre pour un prêt", description = "Indique si un membre est éligible pour emprunter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Éligibilité du membre déterminée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Boolean> isMemberEligibleForLoan(
            @Parameter(description = "ID du membre") @PathVariable Long id) {
        // Note: Vous devriez ajouter cette méthode au MemberService
        Member member = memberService.getMemberById(id).orElse(null);
        if (member != null) {
            boolean eligible = member.isEligibleForLoan();
            return ResponseEntity.ok(eligible);
        }
        return ResponseEntity.notFound().build();
    }
}
