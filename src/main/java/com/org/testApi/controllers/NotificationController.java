package com.org.testApi.controllers;

import com.org.testApi.models.User;
import com.org.testApi.payload.NotificationPayload;
import com.org.testApi.services.NotificationService;
import com.org.testApi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "Points de terminaison pour la gestion des notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Envoyer une notification à un utilisateur spécifique", description = "Envoyer un message de notification à un utilisateur spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification envoyée avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping
    public ResponseEntity<String> sendNotificationToUser(
            @Parameter(description = "Charge utile de notification")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationPayload.class),
                            examples = @ExampleObject(
                                    value = "{\n  \"userId\": 1,\n  \"message\": \"Bonjour, ceci est une notification de test\"\n}"
                            )
                    )
            )
            @RequestBody NotificationPayload payload) {

        Optional<User> userOptional = userService.getUserById(payload.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        notificationService.sendNotificationToUser(userOptional.get(), payload.getMessage());
        return ResponseEntity.ok("Notification envoyée avec succès à l'utilisateur : " + userOptional.get().getUsername());
    }

    @Operation(summary = "Envoyer une notification à tous les utilisateurs", description = "Diffuser un message de notification à tous les utilisateurs du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications envoyées avec succès")
    })
    @PostMapping("/broadcast")
    public ResponseEntity<String> sendNotificationToAllUsers(
            @Parameter(description = "Message de notification")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(
                                    value = "{\n  \"message\": \"Bonjour, ceci est une notification de diffusion à tous les utilisateurs\"\n}"
                            )
                    )
            )
            @RequestBody String message) {

        notificationService.sendNotificationToAllUsers(message);
        return ResponseEntity.ok("Notification envoyée avec succès à tous les utilisateurs");
    }

    @Operation(summary = "Envoyer une notification aux utilisateurs avec un rôle spécifique", description = "Envoyer un message de notification à tous les utilisateurs ayant un rôle spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications envoyées avec succès")
    })
    @PostMapping("/role/{roleName}")
    public ResponseEntity<String> sendNotificationToRole(
            @Parameter(description = "Nom du rôle", example = "ADMIN")
            @PathVariable String roleName,
            @Parameter(description = "Message de notification")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(
                                    value = "{\n  \"message\": \"Bonjour, ceci est une notification pour tous les administrateurs\"\n}"
                            )
                    )
            )
            @RequestBody String message) {

        notificationService.sendNotificationToRole(roleName, message);
        return ResponseEntity.ok("Notification envoyée avec succès aux utilisateurs avec le rôle : " + roleName);
    }
}