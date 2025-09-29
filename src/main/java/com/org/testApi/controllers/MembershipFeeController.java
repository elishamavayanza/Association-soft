package com.org.testApi.controllers;

import com.org.testApi.models.MembershipFee;
import com.org.testApi.payload.MembershipFeePayload;
import com.org.testApi.services.MembershipFeeService;
import com.org.testApi.mapper.MembershipFeeMapper;
import com.org.testApi.repository.MemberRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/membership-fees")
@Tag(name = "Cotisation", description = "Gestion des cotisations des membres")
public class MembershipFeeController {
    
    private static final Logger logger = LoggerFactory.getLogger(MembershipFeeController.class);

    @Autowired
    private MembershipFeeService membershipFeeService;

    @Autowired
    private MembershipFeeMapper membershipFeeMapper;
    
    @Autowired
    private MemberRepository memberRepository;

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
    public ResponseEntity<MembershipFee>getMembershipFeeById(
            @Parameter(description = "ID de la cotisation à récupérer") @PathVariable Long id) {
        logger.info("Fetching membership fee with id: {}", id);
        Optional<MembershipFee> membershipFee = membershipFeeService.getMembershipFeeById(id);
        if (membershipFee.isPresent()) {
            logger.info("Found membership fee: {}", membershipFee.get());
            return ResponseEntity.ok(membershipFee.get());
        } else {
            logger.warn("Membership fee not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle cotisation", description = "Crée une nouvelle cotisation avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema =@Schema(implementation =MembershipFee.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> createMembershipFee(
            @Parameter(description = "Données de la cotisation à créer") 
           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données de la cotisation à créer",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MembershipFee.class),
                    examples = @ExampleObject(
                        name = "Exemple de cotisation",
                        summary = "Exemple de création de cotisation",
                        value = "{\n  \"amount\": 50.00,\n  \"paymentDate\": \"2025-09-27\",\n  \"startDate\": \"2025-10-01\",\n  \"endDate\": \"2026-09-30\",\n  \"paymentMethod\": \"CASH\",\n  \"reference\": \"COT-2025-001\",\n  \"member\": {\n    \"id\": 1\n  }\n}"
)
                )
            ) @RequestBody MembershipFee membershipFee) {
        logger.info("Creating membership fee: {}", membershipFee);
        try {
            MembershipFee savedMembershipFee = membershipFeeService.saveMembershipFee(membershipFee);
             logger.info("Successfully created membership fee with id: {}", savedMembershipFee.getId());
            return ResponseEntity.ok(savedMembershipFee);
        } catch (Exception e) {
            logger.error("Error creating membership fee: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer une cotisation à partir d'un payload", description = "Crée une cotisation en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation créée avec succès à partir du payload",
                    content= {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> createMembershipFeeFromPayload(
            @Parameter(description = "Données du payload pour créer la cotisation") 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données du payload pour créer la cotisation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MembershipFeePayload.class),
                    examples = @ExampleObject(
                        name = "Exemple de cotisation avec payload",
                        summary = "Exemple de création de cotisation avec payload",
                        value = "{\n  \"memberId\": 1,\n  \"amount\": 50.00,\n  \"paymentDate\": \"2025-09-27\",\n  \"startDate\": \"2025-10-01\",\n  \"endDate\": \"2026-09-30\",\n  \"paymentMethod\": \"CASH\",\n  \"reference\": \"COT-2025-001\"\n}"
                    )
                )
            ) @RequestBody MembershipFeePayload payload) {
        logger.info("Creating membership fee from payload: {}", payload);
        try {
            MembershipFee membershipFee = membershipFeeMapper.toEntityWithMemberFromPayload(payload);
            logger.info("Mapped membership fee: {}", membershipFee);
            MembershipFee savedMembershipFee = membershipFeeService.saveMembershipFee(membershipFee);
            logger.info("Successfully created membership fee with id: {}", savedMembershipFee.getId());
            return ResponseEntity.ok(savedMembershipFee);
        } catch (Exception e) {
            logger.error("Error creating membership fee from payload: ", e);
            return ResponseEntity.status(500).build();
        }
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
            @Parameter(description = "Données de mise à jour de la cotisation") 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données de mise à jour de la cotisation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MembershipFee.class),
                    examples = @ExampleObject(
                        name = "Exemple de mise à jour de cotisation",
                        summary = "Exemple de mise à jour de cotisation",
                        value = "{\n  \"amount\": 75.00,\n  \"paymentDate\": \"2025-09-27\",\n  \"startDate\": \"2025-10-01\",\n  \"endDate\": \"2026-09-30\",\n  \"paymentMethod\": \"BANK_TRANSFER\",\n  \"reference\": \"COT-2025-002\",\n  \"member\": {\n    \"id\": 1\n  }\n}"
                    )
                )
            ) @RequestBody MembershipFee membershipFeeDetails) {
        logger.info("Updating membership fee with id: {}, data: {}", id, membershipFeeDetails);
        try {
            Optional<MembershipFee> existingFeeOpt = membershipFeeService.getMembershipFeeById(id);
            if (existingFeeOpt.isPresent()) {
                MembershipFee existingFee = existingFeeOpt.get();
                logger.info("Found existing membership fee: {}", existingFee);
                
// Update only the fields that are provided in the request
                if (membershipFeeDetails.getAmount() != null) {
                    existingFee.setAmount(membershipFeeDetails.getAmount());
                }
                if (membershipFeeDetails.getPaymentDate() != null) {
                    existingFee.setPaymentDate(membershipFeeDetails.getPaymentDate());
                }
                if (membershipFeeDetails.getPaymentMethod() != null) {
                    existingFee.setPaymentMethod(membershipFeeDetails.getPaymentMethod());
                }
                if (membershipFeeDetails.getReference() != null) {
                    existingFee.setReference(membershipFeeDetails.getReference());
                }
                if (membershipFeeDetails.getStartDate() != null) {
                    existingFee.setStartDate(membershipFeeDetails.getStartDate());
                }
                if (membershipFeeDetails.getEndDate() != null) {
                    existingFee.setEndDate(membershipFeeDetails.getEndDate());
                }
                if (membershipFeeDetails.getMember() != null) {
                    existingFee.setMember(membershipFeeDetails.getMember());
                }
                
                logger.info("Updated membership fee to save: {}", existingFee);
                MembershipFee updatedMembershipFee = membershipFeeService.updateMembershipFee(id, existingFee);
                logger.info("Successfully updated membership fee with id: {}", id);
            return ResponseEntity.ok(updatedMembershipFee);
            } else {
                logger.warn("Membership fee not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            logger.error("Membership fee not found with id: " + id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error updating membership fee with id: " + id, e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour unecotisation avec payload", description = "Met à jour une cotisation existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotisation mise à jour avec succès à partir du payload",
                    content = {@Content(mediaType ="application/json",
                            schema = @Schema(implementation = MembershipFee.class))}),
            @ApiResponse(responseCode = "404", description = "Cotisation non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<MembershipFee> updateMembershipFeeWithPayload(
            @Parameter(description = "ID de la cotisation à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pourmettre à jour la cotisation") 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données du payload pour mettreà jour la cotisation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MembershipFeePayload.class),
                    examples = @ExampleObject(
                        name = "Exemple de mise à jour de cotisation avec payload",
                        summary = "Exemple de mise àjourde cotisation avec payload",
                        value = "{\n  \"memberId\": 1,\n  \"amount\": 75.00,\n  \"paymentDate\": \"2025-09-27\",\n  \"startDate\": \"2025-10-01\",\n  \"endDate\": \"2026-09-30\",\n  \"paymentMethod\": \"BANK_TRANSFER\",\n  \"reference\": \"COT-2025-002\"\n}"
                    )
                )
            ) @RequestBody MembershipFeePayload payload) {
        logger.info("Updating membership fee with id: {} using payload: {}", id, payload);
        try {
            Optional<MembershipFee> existingFeeOpt = membershipFeeService.getMembershipFeeById(id);
            if (existingFeeOpt.isPresent()){
MembershipFee existingFee = existingFeeOpt.get();
                logger.info("Found existing membership fee: {}", existingFee);
                
                // Use the mapper method that properly handles field preservation
                membershipFeeMapper.updateEntityWithMemberFromPayload(payload, existingFee);
                
                logger.info("Updated membership fee to save: {}", existingFee);
                MembershipFee savedMembershipFee = membershipFeeService.updateMembershipFee(id, existingFee);
                logger.info("Successfully updated membership fee with id: {}", id);
                return ResponseEntity.ok(savedMembershipFee);
            } else {
                logger.warn("Membership fee not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            logger.error("Membership fee not found with id: " + id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Errorupdating membership fee with id: " + id, e);
            return ResponseEntity.status(500).build();
        }
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
        logger.info("Deleting membership fee with id: {}", id);
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
        logger.info("Soft deleting membership fee with id: {}", id);
        membershipFeeService.softDeleteMembershipFee(id);
        return ResponseEntity.noContent().build();
    }
}