package com.org.testApi.controllers;

import com.org.testApi.models.*;
import com.org.testApi.payload.*;
import com.org.testApi.services.*;
import com.org.testApi.mapper.*;
import com.org.testApi.dto.response.ActivityResponseDTO;
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
@Tag(name = "Activités", description = "Gestion des activités de l'association")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private AssociationService associationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ActivityMapper activityMapper;

    @GetMapping
    @Operation(summary = "Lister toutes les activités", description = "Récupère la liste de toutes les activités")
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
    @Operation(summary = "Récupérer une activité par ID", description = "Récupère une activité spécifique par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité trouvée",
                 content= {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> getActivityById(
            @Parameter(description = "ID de l'activité à récupérer") @PathVariable Long id) {
        Optional<Activity> activity = activityService.getActivityById(id);
        if (activity.isPresent()) {
            // Convert to DTO to avoid serialization issues with Hibernate lazy loading
            ActivityResponseDTO responseDTO = activityMapper.toResponseDto(activity.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle activité", description = "Crée une nouvelle activité avec les données fournies")
@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> createActivity(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données de la nouvelle activité",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActivityPayload.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemple d'activité",
                                           description = "Exemple de création d'une activité avec une association",
                                            value = """
                                                  {
                                                      "title": "Conférence sur l'environnement",
                                                      "description": "Une conférence annuelle sur la protection de l'environnement",
                                                      "type": "CONFERENCE",
                                                      "startDateTime": "2025-11-20T09:00:00",
                                                      "endDateTime": "2025-11-20T18:00:00",
                                                      "location": "Centre de conférences de Marseille",
                                                     "associationId": 1,
                                                      "status": "PLANNED"
                                                    }
                                                    """
                                    )
                            }
                    )
            ) @Valid @RequestBody ActivityPayload payload) {
        try {
            Activity activity = new Activity();
            
            // Set activity fields from payload
            activity.setTitle(payload.getTitle());
            activity.setDescription(payload.getDescription());
           activity.setStartDateTime(payload.getStartDateTime());
            activity.setEndDateTime(payload.getEndDateTime());
            activity.setLocation(payload.getLocation());
            activity.setDeleted(payload.getDeleted() != null ? payload.getDeleted() : false);
            
            // Handle enum conversions with proper error handling
            if (payload.getType() != null) {
                try {
                    activity.setType(Activity.ActivityType.valueOf(payload.getType().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid activity type: " + payload.getType());
                }
            }
            
            if (payload.getStatus() != null) {
                try {
                    activity.setStatus(Activity.ActivityStatus.valueOf(payload.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid activity status: " + payload.getStatus());
                }
            } else {
                activity.setStatus(Activity.ActivityStatus.PLANNED);
            }
            
            // Check if association exists when associationId is provided in the payload
            if(payload.getAssociationId() != null) {
                Association association = associationService.getAssociationById(payload.getAssociationId())
                        .orElseThrow(() -> new RuntimeException("Association not found with id: " + payload.getAssociationId()));
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
    @Operation(summary = "Mettre à jour uneactivité", description = "Met à jour une activité existante avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité mise à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation =ActivityResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données derequête invalides"),
            @ApiResponse(responseCode = "404", description = "Association nontrouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> updateActivity(
@Parameter(description = "ID de l'activité à mettre à jour") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
description = "Données de mise à jour de l'activité",
                    content = @Content(
                            mediaType = "application/json",
                           schema = @Schema(implementation = Activity.class),
                            examples = {
                                   @ExampleObject(
                                            name = "Exemple demise à jour d'activité",
                                            description = "Exemple de mise à jour d'une activité avec une association",
                                            value = """
                                                    {
                                                      "title": "Conférencesurl'environnement - Mise à jour",
                                                      "description": "Une conférence annuelle sur la protection de l'environnement (mise à jour)",
                                                     "type": "CONFERENCE",
                                                      "startDateTime": "2025-11-20T09:00:00",
                                                      "endDateTime": "2025-11-20T18:00:00",
                                                      "location": "Centrede conférences de Marseille",
                                                     "associationId": 1,
                                                      "status": "PLANNED"
                                                    }
                                                    """
                                  )
                          }
                    )
            ) @RequestBody Activity activity) {
        try {
            // Get the existing activity to preserve the association if not provided
            Optional<Activity> existingActivityOpt = activityService.getActivityById(id);
            if (existingActivityOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
Activity existingActivity = existingActivityOpt.get();
// Preserve the existing association if not provided in the update
            if (activity.getAssociation() == null) {
                activity.setAssociation(existingActivity.getAssociation());
            } else if (activity.getAssociation().getId() != null) {
                // Check if association existswhen associationIDis providedin the activity entity
Association association = associationService.getAssociationById(activity.getAssociation().getId())
                        .orElseThrow(() -> new RuntimeException("Association not found with id: " + activity.getAssociation().getId()));
                activity.setAssociation(association);
            } else {
                // If association object isprovided but without ID, preserveexisting
                activity.setAssociation(existingActivity.getAssociation());
            }
            
            // Preserve other fields that might not be provided in the update
            if (activity.getParticipants() == null) {
                activity.setParticipants(existingActivity.getParticipants());
            }
            
            if (activity.getTransactions() ==null) {
               activity.setTransactions(existingActivity.getTransactions());
            }
            
           if (activity.getCreator() == null) {
                activity.setCreator(existingActivity.getCreator());
            }
            
            if (activity.getProject() == null) {
                activity.setProject(existingActivity.getProject());
            }
            
            activity.setId(id); // Ensure the ID isset forthe update
                       Activity updatedActivity = activityService.updateActivity(id, activity);
            // Convert to DTO to avoid serialization issues with Hibernate lazy loading
            ActivityResponseDTO responseDTO = activityMapper.toResponseDto(updatedActivity);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating activity: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unexpected error updating activity: " + e.getMessage());
        }
    }

   @PutMapping("/{id}/payload")
@Operation(summary = "Mettre à jour une activité avec payload", description = "Met à jour une activité existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité mise à jour avec succès à partir du payload",
                    content = {@Content(mediaType ="application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode ="404", description = "Association non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> updateActivityWithPayload(
            @Parameter(description = "ID de l'activité à mettre à jour") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données dupayload pour mettre à jour l'activité",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActivityPayload.class),
examples= {
                                  @ExampleObject(
                                            name = "Exemple demise à jour de payload d'activité",
                                            description = "Exemple de mise à jour d'un payload d'activité avec associationId",
                                            value = """
                                                  {
                                                      "title": "Nettoyage de la plage - Mise à jour",
"description": "Activité de nettoyage de la plage organisée par l'association pour protéger l'environnement côtier (mise à jour)",
                                                      "type": "SOCIAL_EVENT",
                                                      "startDateTime": "2025-11-15T09:00:00",
                                                      "endDateTime": "2025-11-15T13:00:00",
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
            
            // Preserve the existing association if not provided in the payload
            if (payload.getAssociationId() == null) {
// Do nothing, keep the existing association
            } else {
                // Update association if associationId is provided in payload
                try{
                                        Association association = associationService.getAssociationById(payload.getAssociationId())
                            .orElseThrow(() -> new RuntimeException("Association not found with id: " + payload.getAssociationId()));
                    activity.setAssociation(association);
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
            
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
           // If no projectId is provided in payload, leave the existing project unchanged (it can be null)
            
            // Manually update activity fields from payload instead of using mapper
            if(payload.getTitle() != null) {
                activity.setTitle(payload.getTitle());
            }
            if (payload.getDescription() != null) {
               activity.setDescription(payload.getDescription());
            }
            if (payload.getType() != null) {
                try {
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
            
            // Handle enum conversions with proper error handling
            if (payload.getStatus() != null) {
                try {
                    activity.setStatus(Activity.ActivityStatus.valueOf(payload.getStatus().toUpperCase()));
                } catch(IllegalArgumentException e) {
                                        return ResponseEntity.badRequest().body("Invalid activity status: " + payload.getStatus());
                }
            }
            
            Activity updatedActivity = activityService.updateActivity(id, activity);
            return ResponseEntity.ok(updatedActivity);
        } catch (jakarta.validation.ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating activity with payload: " + e.getMessage());
        }
    }

      @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une activité", description = "Supprime une activité par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activité supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> deleteActivity(
            @Parameter(description = "ID de l'activité à supprimer") @PathVariable Long id) {
        try {
            activityService.deleteActivity(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting activity: " + e.getMessage());
        }
    }

 @GetMapping("/association/{associationId}")
    @Operation(summary = "Lister les activités d'une association", description = "Récupère la liste des activités d'une association spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des activités de l'association récupérée avec succès",
                    content= {@Content(mediaType = "application/json",
                            schema = @Schema(implementation= Activity.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Activity>> getActivitiesByAssociation(
            @Parameter(description = "ID de l'association") @PathVariable Long associationId) {
                    List<Activity> activities = activityService.getActivitiesByAssociationId(associationId);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Lister les activités d'un projet", description = "Récupère la liste des activités d'un projet spécifique")
    @ApiResponses(value = {
@ApiResponse(responseCode = "200", description = "Liste des activités du projet récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
schema= @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Activity>> getActivitiesByProject(
            @Parameter(description = "ID du projet") @PathVariable Long projectId) {
        List<Activity> activities = activityService.getActivitiesByProjectId(projectId);
return ResponseEntity.ok(activities);
    }

    @GetMapping("/user/{userId}")
        @Operation(summary = "Lister les activités d'un utilisateur", description = "Récupère la liste des activités auxquelles un utilisateur participe")
    @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "Liste des activités de l'utilisateur récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
schema= @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
 })
        public ResponseEntity<List<Activity>> getActivitiesByUser(
           @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        List<Activity> activities = activityService.getActivitiesByUserId(userId);
        return ResponseEntity.ok(activities);
    }

    @PostMapping("/{id}/participants")
    @Operation(summary = "Ajouter des participants à une activité", description = "Ajoute un ou plusieurs participants à une activité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participants ajoutés avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> addParticipants(
            @Parameter(description = "ID de l'activité") @PathVariable Long id,
            @Parameter(description = "Liste des IDs des utilisateurs à ajouter comme participants") @RequestBody List<Long> userIds) {
       try {
                        Activity updatedActivity = activityService.addParticipants(id, userIds);
            // Convert to DTO to avoid serialization issues with Hibernate lazy loading
            ActivityResponseDTO responseDTO = activityMapper.toResponseDto(updatedActivity);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error adding participants: " + e.getMessage());
        }
    }

       @DeleteMapping("/{id}/participants")
    @Operation(summary = "Supprimer des participants d'une activité", description = "Supprime un ou plusieurs participants d'une activité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participants supprimés avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> removeParticipants(
            @Parameter(description = "ID de l'activité") @PathVariable Long id,
            @Parameter(description = "Liste des IDs des utilisateurs à supprimer des participants") @RequestBody List<Long> userIds) {
        try {
            Activity updatedActivity = activityService.removeParticipants(id, userIds);
            // Convert to DTO to avoid serialization issues with Hibernate lazy loading
            ActivityResponseDTO responseDTO = activityMapper.toResponseDto(updatedActivity);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error removing participants: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/participants")
    @Operation(summary = "Lister les participants d'une activité", description = "Récupère la liste des participants d'une activité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des participants récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Activité non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> getParticipants(
            @Parameter(description = "ID de l'activité") @PathVariable Long id) {
        try {
            Optional<Activity> activityOpt = activityService.getActivityById(id);
            if (activityOpt.isPresent()) {
                List<User> participants = activityOpt.get().getParticipants();
                return ResponseEntity.ok(participants);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving participants: " + e.getMessage());
        }
    }
}