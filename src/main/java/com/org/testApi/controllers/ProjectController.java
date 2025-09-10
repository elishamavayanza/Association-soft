package com.org.testApi.controllers;

import com.org.testApi.models.Project;
import com.org.testApi.payload.ProjectPayload;
import com.org.testApi.services.ProjectService;
import com.org.testApi.mapper.ProjectMapper;
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
@RequestMapping("/api/projects")
@Tag(name = "Projet", description = "Gestion des projets")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMapper projectMapper;

    @GetMapping
    @Operation(summary = "Récupérer tous les projets", description = "Retourne une liste de tous les projets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des projets récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un projet par ID", description = "Retourne un projet spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projet trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID du projet à récupérer") @PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau projet", description = "Crée un nouveau projet avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projet créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Project> createProject(
            @Parameter(description = "Données du projet à créer") @RequestBody Project project) {
        Project savedProject = projectService.saveProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un projet à partir d'un payload", description = "Crée un projet en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projet créé avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Project> createProjectFromPayload(
            @Parameter(description = "Données du payload pour créer le projet") @RequestBody ProjectPayload payload) {
        Project project = projectMapper.toEntityFromPayload(payload);
        Project savedProject = projectService.saveProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un projet", description = "Met à jour un projet existant avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projet mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID du projet à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour du projet") @RequestBody Project project) {
        try {
            Project updatedProject = projectService.updateProject(id, project);
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour un projet avec payload", description = "Met à jour un projet existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projet mis à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Project> updateProjectWithPayload(
            @Parameter(description = "ID du projet à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour le projet") @RequestBody ProjectPayload payload) {
        return projectService.getProjectById(id)
                .map(project -> {
                    projectMapper.updateEntityFromPayload(payload, project);
                    Project updatedProject = projectService.updateProject(id, project);
                    return ResponseEntity.ok(updatedProject);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un projet", description = "Supprime définitivement un projet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projet supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID du projet à supprimer") @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement un projet", description = "Marque un projet comme supprimé sans le retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projet supprimé logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteProject(
            @Parameter(description = "ID du projet à supprimer logiquement") @PathVariable Long id) {
        projectService.softDeleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
