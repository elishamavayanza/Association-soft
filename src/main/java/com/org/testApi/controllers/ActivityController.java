package com.org.testApi.controllers;

import com.org.testApi.dto.response.ActivityResponseDTO;
import com.org.testApi.models.Activity;
import com.org.testApi.models.Association;
import com.org.testApi.models.Project;
import com.org.testApi.payload.ActivityPayload;
import com.org.testApi.services.ActivityService;
import com.org.testApi.services.AssociationService;
import com.org.testApi.services.ProjectService;
import com.org.testApi.mapper.ActivityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    
    @Autowired
    private ProjectService projectService;

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
    @Operation(summary = "Récupérer une activité par ID", description = "Retourne une activité spécifique en fonctionde sonID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "404", description="Activité non trouvée"),
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
    @ApiResponses(value= {
@ApiResponse(responseCode = "200", description = "Activité créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "400", description = "Données derequêteinvalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Activity> createActivity(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données de l'activité à créer",
                    content=@Content(
                           mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemple d'activité complète",
                                            description = "Exemple d'une activité avec une association",
                                            value = """
                                                    {
                                                      "title":"Conférencesur l'environnement",
                                                      "description": "Une conférence annuelle sur la protection de l'environnement",
                                                      "type": "CONFERENCE",
                                                      "startDateTime": "2025-11-20T09:00:00",
                                                    "endDateTime":"2025-11-20T17:00:00",
                                                      "location": "Centre de conférences de Marseille",
                                                      "association": {
                                                        "id": 1
                                                      },
                                                      "status": "PLANNED"
                                                   }
                                                  """
)
                            }
)
            ) @RequestBody Activity activity) {
        Activity savedActivity = activityService.saveActivity(activity);
        return ResponseEntity.ok(savedActivity);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer une activité à partir d'un payload", description = "Crée uneactivitéen utilisant unobjet payload")
    @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "Activité créée avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
@ApiResponse(responseCode = "400", description = "Données depayload invalides"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?>createActivityFromPayload(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du payload pour créer l'activité",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActivityPayload.class),
                            examples = {
                                   @ExampleObject(
                                           name = "Exemple de payload d'activité",
                                            description = "Exemple d'un payload d'activité avec associationId",
                                            value = """
                                                    {
                                                      "title": "Nettoyage de la plage",
                                                      "description": "Activité de nettoyage de la plage organisée parl'association pourprotéger l'environnement côtier",
                                                      "type": "SOCIAL_EVENT",
                                                      "startDateTime": "2025-10-15T09:00:00",
                                                      "endDateTime": "2025-10-15T12:00:00",
                                                      "location": "Plage du Port de Plaisance",
                                                      "associationId": 1,
                                                      "status": "PLANNED"
                                                    }
                                                    """
                                    )
                            }
                    )
            ) @RequestBody ActivityPayload payload) {
        try{
            Activity activity= new Activity();
            
            // Set activity fields from payload
            activity.setTitle(payload.getTitle());
            activity.setDescription(payload.getDescription());
            activity.setStartDateTime(payload.getStartDateTime());
            activity.setEndDateTime(payload.getEndDateTime());
            activity.setLocation(payload.getLocation());
            activity.setDeleted(payload.getDeleted() != null ? payload.getDeleted(): false);
            
           // Handle enum conversions with proper error handling
            if (payload.getType() != null) {
                try {
                    activity.setType(Activity.ActivityType.valueOf(payload.getType().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid activity type: " + payload.getType());
               }
           }
            
if(payload.getStatus() != null) {
                try {
                    activity.setStatus(Activity.ActivityStatus.valueOf(payload.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid activity status: " + payload.getStatus());
                }
            } else {
                activity.setStatus(Activity.ActivityStatus.PLANNED);
           }
            
            // Check if association exists when associationId is provided in the payload
            if (payload.getAssociationId() != null) {
                Association association = associationService.getAssociationById(payload.getAssociationId())
                        .orElseThrow(() -> new RuntimeException("Association not found with id: " +payload.getAssociationId()));
activity.setAssociation(association);
            }
            
Activity savedActivity = activityService.saveActivity(activity);
            return ResponseEntity.ok(savedActivity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating activity: " + e.getMessage());
       }
   }

@PutMapping("/{id}")
@Operation(summary = "Mettre à jour une activité", description = "Met à jour une activité existante avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité mise àjour avecsuccès",
content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données derequêteinvalides"),
           @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> updateActivity(
            @Parameter(description = "ID del'activitéà mettre àjour")@PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données de mise à jour de l'activité",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class),
                            examples= {
                                   @ExampleObject(
                                            name = "Exemple demiseà jour d'activité",
                                            description = "Exemple de mise à jour d'une activité avec une association",
                                            value = """
                                                    {
                                                      "title": "Conférence sur l'environnement - Mise à jour",
                                                      "description": "Une conférence annuelle sur la protection de l'environnement (mise à jour)",
                                                      "type": "CONFERENCE",
                                                      "startDateTime": "2025-11-20T09:00:00",
                                                      "endDateTime": "2025-11-20T18:00:00",
                                                  "location": "Centre de conférences de Marseille",
                                                      "association": {
                                                        "id": 1
                                                      },
                                                      "status": "PLANNED"
                                                    }
                                                    """
                                    )
                            }
                    )
            ) @RequestBody Activity activity) {
        try {
            // Check if association exists when associationID is provided in the activity entity
            if (activity.getAssociation() != null && activity.getAssociation().getId() != null) {
                Association association = associationService.getAssociationById(activity.getAssociation().getId())
                        .orElseThrow(() -> new RuntimeException("Association not found with id: " + activity.getAssociation().getId()));
                activity.setAssociation(association);
            }
            Activity updatedActivity = activityService.updateActivity(id, activity);
            // Convertto DTO to avoid serialization issues with Hibernate lazy loading
            ActivityResponseDTO responseDTO = activityMapper.toResponseDto(updatedActivity);
            return ResponseEntity.ok(responseDTO);
      } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating activity: " + e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unexpected error updating activity: " + e.getMessage());
        }
}

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour une activité avec payload", description = "Met à jourune activité existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="Activité miseà jouravec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation =Activity.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description ="Données de payloadinvalides"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?>updateActivityWithPayload(
            @Parameter(description ="ID de l'activité à mettre à jour") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du payload pour mettre à jour l'activité",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActivityPayload.class),
                            examples = {
                                   @ExampleObject(
                                            name = "Exemple de miseà jour depayload d'activité",
                                            description = "Exemple de mise à jour d'un payload d'activité avec associationId",
                                            value = """
                                                  {
                                                      "title": "Nettoyage de la plage - Mise à jour",
                                                      "description": "Activité de nettoyage de la plageorganisée parl'association pour protéger l'environnement côtier (mise à jour)",
                                                      "type": "SOCIAL_EVENT",
                                                      "startDateTime": "2025-10-15T09:00:00",
                                                      "endDateTime": "2025-10-15T13:00:00",
"location": "Plage du Port de Plaisance",
                                                      "associationId": 1,
                                                      "status": "PLANNED"
                                                    }
                                                    """
                                    )
                            }
                    )
            ) @Valid @RequestBody ActivityPayload payload) {
        try {
            Optional<Activity> activityOpt = activityService.getActivityById(id);
            if (activityOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Activity activity = activityOpt.get();
            
            // Update association if associationId is provided in payload
            if (payload.getAssociationId() != null) {
                try{
Association association = associationService.getAssociationById(payload.getAssociationId())
                            .orElseThrow(() -> new RuntimeException("Association not found with id: " + payload.getAssociationId()));
                    activity.setAssociation(association);
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
           }
//If no associationId is provided in payload, preserve the existing association
            // This is important because the association field is required (nullable= false)
            
            // Update project if projectId is provided in payload
            if (payload.getProjectId() != null) {
                try {
                    Project project = projectService.getProjectById(payload.getProjectId())
                            .orElseThrow(() -> new RuntimeException("Project not found with id: " + payload.getProjectId()));
                    activity.setProject(project);
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
            // If no projectId is provided in payload,leavetheexisting project unchanged (itcan be null)
            
            // Manually update activity fields from payload instead of using mapper
            if (payload.getTitle() != null) {
                activity.setTitle(payload.getTitle());
            }
            if (payload.getDescription() != null){
                activity.setDescription(payload.getDescription());
            }
            if (payload.getType()!= null) {
               try{
                    activity.setType(Activity.ActivityType.valueOf(payload.getType().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid activity type: " + payload.getType());
                }
            }
            if (payload.getStartDateTime() != null) {
                activity.setStartDateTime(payload.getStartDateTime());
            }
if (payload.getEndDateTime() != null) {
                activity.setEndDateTime(payload.getEndDateTime());
            }
            if (payload.getLocation() != null) {
                activity.setLocation(payload.getLocation());
            }
            if (payload.getDeleted() != null) {
                activity.setDeleted(payload.getDeleted());
}
if(payload.getStatus() != null) {
                try{
                    activity.setStatus(Activity.ActivityStatus.valueOf(payload.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid activity status: " + payload.getStatus());
                }
            }
            
            Activity updatedActivity = activityService.updateActivity(id, activity);
           return ResponseEntity.ok(updatedActivity);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating activity: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une activité",description ="Supprime définitivement une activité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activité supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
           @ApiResponse(responseCode= "500", description = "Erreur interne duserveur")
    })
    public ResponseEntity<Void> deleteActivity(
            @Parameter(description = "ID de l'activité à supprimer") @PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
}

   @DeleteMapping("/{id}/soft")
    @Operation(summary= "Supprimer logiquement une activité",description = "Marque une activité comme supprimée sans la retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Activitésupprimée logiquement avec succès"),
            @ApiResponse(responseCode = "404", description= "Activité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteActivity(
            @Parameter(description= "ID del'activité à supprimerlogiquement") @PathVariable Long id) {
       activityService.softDeleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}