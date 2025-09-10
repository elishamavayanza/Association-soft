package com.org.testApi.controllers;

import com.org.testApi.models.Role;
import com.org.testApi.payload.RolePayload;
import com.org.testApi.services.RoleService;
import com.org.testApi.mapper.RoleMapper;
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
@RequestMapping("/api/roles")
@Tag(name = "Rôle", description = "Gestion des rôles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping
    @Operation(summary = "Récupérer tous les rôles", description = "Retourne une liste de tous les rôles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rôles récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un rôle par ID", description = "Retourne un rôle spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Role> getRoleById(
            @Parameter(description = "ID du rôle à récupérer") @PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau rôle", description = "Crée un nouveau rôle avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Role> createRole(
            @Parameter(description = "Données du rôle à créer") @RequestBody Role role) {
        Role savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un rôle à partir d'un payload", description = "Crée un rôle en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle créé avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Role> createRoleFromPayload(
            @Parameter(description = "Données du payload pour créer le rôle") @RequestBody RolePayload payload) {
        Role role = roleMapper.toEntityFromPayload(payload);
        Role savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un rôle", description = "Met à jour un rôle existant avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Role> updateRole(
            @Parameter(description = "ID du rôle à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour du rôle") @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour un rôle avec payload", description = "Met à jour un rôle existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle mis à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Role> updateRoleWithPayload(
            @Parameter(description = "ID du rôle à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour le rôle") @RequestBody RolePayload payload) {
        return roleService.getRoleById(id)
                .map(role -> {
                    roleMapper.updateEntityFromPayload(payload, role);
                    Role updatedRole = roleService.updateRole(id, role);
                    return ResponseEntity.ok(updatedRole);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un rôle", description = "Supprime définitivement un rôle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rôle supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID du rôle à supprimer") @PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement un rôle", description = "Marque un rôle comme supprimé sans le retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rôle supprimé logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteRole(
            @Parameter(description = "ID du rôle à supprimer logiquement") @PathVariable Long id) {
        roleService.softDeleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
