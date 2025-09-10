package com.org.testApi.controllers;

import com.org.testApi.models.Role;
import com.org.testApi.models.User;
import com.org.testApi.services.UserService;
import com.org.testApi.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateur - Rôles", description = "Gestion des rôles des utilisateurs")
public class UserRoleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Attribuer un rôle à un utilisateur", description = "Attribue un rôle spécifique à un utilisateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle attribué avec succès à l'utilisateur",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou rôle non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> assignRoleToUser(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId,
            @Parameter(description = "ID du rôle à attribuer") @PathVariable Long roleId) {

        // Récupérer l'utilisateur
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Récupérer le rôle
        Role role = roleService.getRoleById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        // Attribuer le rôle à l'utilisateur
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
            user.setRoles(roles);
        }
        roles.add(role);

        // Sauvegarder l'utilisateur
        User updatedUser = userService.updateUser(userId, user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Retirer un rôle d'un utilisateur", description = "Retire un rôle spécifique d'un utilisateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle retiré avec succès de l'utilisateur",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou rôle non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> removeRoleFromUser(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId,
            @Parameter(description = "ID du rôle à retirer") @PathVariable Long roleId) {

        // Récupérer l'utilisateur
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Récupérer le rôle
        Role role = roleService.getRoleById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        // Retirer le rôle de l'utilisateur
        Set<Role> roles = user.getRoles();
        if (roles != null) {
            roles.remove(role);
        }

        // Sauvegarder l'utilisateur
        User updatedUser = userService.updateUser(userId, user);

        return ResponseEntity.ok(updatedUser);
    }
}
