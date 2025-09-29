package com.org.testApi.controllers;

import com.org.testApi.models.Activity;
import com.org.testApi.payload.ActivityPayload;
import com.org.testApi.services.ActivityService;
import com.org.testApi.mapper.ActivityMapper;
import com.org.testApi.services.AssociationService;
import com.org.testApi.models.Association;
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
@RequestMapping("/api/activities")
@Tag(name = "Activity", description = "Gestion des activités")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;
    
    @Autowired
    private AssociationService associationService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les activités", description = "Retourne une liste de toutes les activités")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des activités récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une activité par ID", description = "Retourne une activité spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Activity> getActivityById(
            @Parameter(description = "ID de l'activité à récupérer") @PathVariable Long id) {
        return activityService.getActivityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle activité", description = "Crée une nouvelle activité avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Activity> createActivity(
            @Parameter(description = "Données de l'activité à créer") @RequestBody Activity activity) {
        Activity savedActivity = activityService.saveActivity(activity);
        return ResponseEntity.ok(savedActivity);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer une activité à partir d'un payload", description = "Crée une activité en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité créée avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Activity> createActivityFromPayload(
            @Parameter(description = "Données du payload pour créer l'activité") @RequestBody ActivityPayload payload) {
        // Check if association exists when associationId is provided in the payload
        if (payload.getAssociationId() != null) {
            Association association = associationService.getAssociationById(payload.getAssociationId())
                    .orElseThrow(() -> new RuntimeException("Association not found with id: " + payload.getAssociationId()));
            
            Activity activity = activityMapper.toEntityFromPayload(payload);
            activity.setAssociation(association);
            Activity savedActivity = activityService.saveActivity(activity);
            return ResponseEntity.ok(savedActivity);
        } else {
            Activity activity = activityMapper.toEntityFromPayload(payload);
            Activity savedActivity = activityService.saveActivity(activity);
            return ResponseEntity.ok(savedActivity);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une activité", description = "Met à jour une activité existante avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité mise à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Activity> updateActivity(
            @Parameter(description = "ID de l'activité à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour de l'activité") @RequestBody Activity activity) {
        try {
            Activity updatedActivity = activityService.updateActivity(id, activity);
            return ResponseEntity.ok(updatedActivity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour une activité avec payload", description = "Met à jour une activité existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité mise à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Activity> updateActivityWithPayload(
            @Parameter(description = "ID de l'activité à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour l'activité") @RequestBody ActivityPayload payload) {
        return activityService.getActivityById(id)
                .map(activity -> {
                    // Update association if associationId is provided in payload
                    if (payload.getAssociationId() != null) {
                        Association association = associationService.getAssociationById(payload.getAssociationId())
                                .orElseThrow(() -> new RuntimeException("Association not found with id: " + payload.getAssociationId()));
                        activity.setAssociation(association);
                    }
                    activityMapper.updateEntityFromPayload(payload, activity);
                    Activity updatedActivity = activityService.updateActivity(id, activity);
                    return ResponseEntity.ok(updatedActivity);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une activité", description = "Supprime définitivement une activité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activité supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteActivity(
            @Parameter(description = "ID de l'activité à supprimer") @PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement une activité", description = "Marque une activité comme supprimée sans la retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activité supprimée logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteActivity(
            @Parameter(description = "ID de l'activité à supprimer logiquement") @PathVariable Long id) {
        activityService.softDeleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}