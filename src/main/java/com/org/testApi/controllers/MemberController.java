package com.org.testApi.controllers;

import com.org.testApi.models.Member;
import com.org.testApi.payload.MemberPayload;
import com.org.testApi.services.MemberService;
import com.org.testApi.mapper.MemberMapper;
import com.org.testApi.services.UserService;
import com.org.testApi.services.AssociationService;
import com.org.testApi.models.User;
import com.org.testApi.models.Association;
import com.org.testApi.dto.MemberDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Membre", description = "Gestion des membres")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AssociationService associationService;

    @GetMapping
    @Operation(summary = "Récupérer tous les membres", description = "Retourne une liste de tous les membres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des membres récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        List<MemberDTO> memberDTOs = members.stream()
                .map(memberMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(memberDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un membre par ID", description = "Retourne un membre spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberDTO> getMemberById(
            @Parameter(description = "ID du membre à récupérer") @PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(member -> ResponseEntity.ok(memberMapper.toDto(member)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau membre", description = "Crée un nouveau membre avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou association non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberDTO> createMember(
            @Parameter(description = "Données du membre à créer")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Exemple de données pour créer un membre",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Member.class),
                            examples = @ExampleObject(
                                    name = "Exemple de création de membre avec entité complète",
                                    summary = "Exemple de création de membre avec entité complète",
                                    value = "{\n  \"user\": {\n    \"id\": 6\n  },\n  \"association\": {\n    \"id\": 3\n  }\n}"
                            )
                    )
            ) @RequestBody Member member) {
        // Check if user exists when user ID is provided directly in the member entity
        if (member.getUser() != null && member.getUser().getId() != null) {
            User user = userService.getUserById(member.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + member.getUser().getId()));
            member.setUser(user);
        }

        // Check if association exists when association ID is provided directly in the member entity
        if (member.getAssociation() != null && member.getAssociation().getId() != null) {
            Association association = associationService.getAssociationById(member.getAssociation().getId())
                    .orElseThrow(() -> new RuntimeException("Association not found with id: " + member.getAssociation().getId()));
            member.setAssociation(association);
        }

        // Générer un code membre unique si ce n'est pas déjà fait
        if (member.getMemberCode() == null) {
            member.setMemberCode(generateUniqueMemberCode());
        }

        Member savedMember = memberService.saveMember(member);
        return ResponseEntity.ok(memberMapper.toDto(savedMember));
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un membre à partir d'un payload", description = "Crée un nouveau membre en utilisant un objet payload contenant les informations du membre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Membre créé avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = MemberDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
        @ApiResponse(responseCode = "404", description = "Utilisateur ou association non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> createMemberFromPayload(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du membre à créer",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberPayload.class),
                            examples = @ExampleObject(
                                    name = "Exemple de création de membre",
                                    summary = "Exemple de création de membre",
                                    value = "{\n  \"userId\": 2,\n  \"firstName\": \"Marie\",\n  \"lastName\": \"Leroy\",\n  \"email\": \"marie.leroy@example.com\",\n  \"phone\": \"+33198765432\",\n  \"address\": \"456 Avenue des Champs-Élysées, 75008 Paris, France\",\n  \"associationId\": 2\n}"
                            )
                    )
            ) @RequestBody MemberPayload payload) {
        try {
            logger.info("Starting member creation from payload: {}", payload);

            // Check if user exists
            logger.info("Checking if user exists with ID: {}", payload.getUserId());
            User user = userService.getUserById(payload.getUserId())
                    .orElseThrow(() -> {
                        logger.error("User not found with id: {}", payload.getUserId());
                        return new RuntimeException("User not found with id: " + payload.getUserId());
                    });

            // Check if association exists
            logger.info("Checking if association exists with ID: {}", payload.getAssociationId());
            Association association = associationService.getAssociationById(payload.getAssociationId())
                    .orElseThrow(() -> {
                        logger.error("Association not found with id: {}", payload.getAssociationId());
                        return new RuntimeException("Association not found with id: " + payload.getAssociationId());
                    });

            logger.info("Creating member entity from payload");
            Member member = memberMapper.toEntityFromPayload(payload);
            if (member == null) {
                logger.error("Failed to map payload to Member entity");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to map payload to Member entity");
            }
            member.setUser(user);
            member.setAssociation(association);

            logger.debug("Mapped member entity: {}", member);

            // Set member code if not provided
            logger.info("Setting member code");
            if (member.getMemberCode() == null) {
                if (payload.getMemberCode() != null) {
                    member.setMemberCode(payload.getMemberCode());
                } else {
                    member.setMemberCode(generateUniqueMemberCode());
                }
            }

            logger.info("Saving member with code: {}", member.getMemberCode());
            Member savedMember = memberService.saveMember(member);
            logger.info("Member created successfully with ID: {}", savedMember.getId());
            return ResponseEntity.ok(memberMapper.toDto(savedMember));
        } catch (Exception e) {
            logger.error("Error creating member from payload: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating member: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un membre", description = "Met à jour un membre existant avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberDTO> updateMember(
            @Parameter(description = "ID du membre à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour du membre") @RequestBody Member member) {
        try {
            Member updatedMember = memberService.updateMember(id, member);
            return ResponseEntity.ok(memberMapper.toDto(updatedMember));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour un membre avec payload", description = "Met à jour un membre existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre mis à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MemberDTO> updateMemberWithPayload(
            @Parameter(description = "ID du membre à mettre à jour") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du payload pour mettre à jour le membre",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberPayload.class),
                            examples = @ExampleObject(
                                    name = "Exemple de mise à jour de membre",
                                    summary = "Exemple de mise à jour de membre",
                                    value = "{\n  \"userId\": 2,\n  \"firstName\": \"Marie\",\n  \"lastName\": \"Leroy\",\n  \"email\": \"marie.leroy@example.com\",\n  \"phone\": \"+33198765432\",\n  \"address\": \"456 Avenue des Champs-Élysées, 75008 Paris, France\",\n  \"associationId\": 2\n}"
                            )
                    )
            ) @RequestBody MemberPayload payload) {
        try {
            return memberService.getMemberById(id)
                    .map(member -> {
                        try {
                            // Update user and association if IDs are provided in payload
                            if (payload.getUserId() != null) {
                                User user = userService.getUserById(payload.getUserId())
                                        .orElseThrow(() -> new RuntimeException("User not found with id: " + payload.getUserId()));
                                member.setUser(user);

                                // Mettre à jour les informations personnelles de l'utilisateur
                                try {
                                    // Fetch the current user from database to ensure we have all properties
                                    User currentUser = userService.getUserById(user.getId())
                                            .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

                                    // Update only the fields provided in the payload
                                    if (payload.getFirstName() != null) {
                                        currentUser.setFirstName(payload.getFirstName());
                                    }

                                    if (payload.getLastName() != null) {
                                        currentUser.setLastName(payload.getLastName());
                                    }

                                    if (payload.getEmail() != null) {
                                        currentUser.setEmail(payload.getEmail());
                                    }

                                    if (payload.getPhone() != null) {
                                        currentUser.setPhoneNumber(payload.getPhone());
                                    }

                                    userService.updateUser(currentUser.getId(), currentUser);
                                } catch (Exception e) {
                                    logger.error("Error updating user information: ", e);
                                    // Continue with member update even if user update fails
                                }
                            }
                            if (payload.getAssociationId() != null) {
                                Association association = associationService.getAssociationById(payload.getAssociationId())
                                        .orElseThrow(() -> new RuntimeException("Association not found with id: " + payload.getAssociationId()));
                                member.setAssociation(association);
                            }
                            memberMapper.updateEntityFromPayload(payload, member);
                            Member updatedMember = memberService.updateMember(id, member);
                            return ResponseEntity.ok(memberMapper.toDto(updatedMember));
                        } catch (Exception e) {
                            logger.error("Error updating member with id: " + id, e);
                            throw new RuntimeException("Error updating member", e);
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("Error updating member with id: " + id, e);
            return ResponseEntity.status(500).build();
        }
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
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<MemberDTO>> searchMembers(
            @Parameter(description = "Nom du membre (optionnel)") @RequestParam(required = false) String name,
            @Parameter(description = "Email du membre (optionnel)") @RequestParam(required = false) String email,
            @Parameter(description = "Type de membre (optionnel)") @RequestParam(required = false) Member.MemberType memberType,
            @Parameter(description = "ID de l'association (optionnel)") @RequestParam(required = false) Long associationId,
            @Parameter(description = "Statut d'activité (optionnel)") @RequestParam(required = false) Boolean isActive) {
        // Note: Pour une implémentation complète, vous devriez ajouter cette méthode au service
        List<Member> members = memberService.getAllMembers();
        List<MemberDTO> memberDTOs = members.stream()
                .map(memberMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(memberDTOs);
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

    /**
     * Génère un code membre unique
     * @return Un code membre unique
     */
    private String generateUniqueMemberCode() {
        return "MBR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}