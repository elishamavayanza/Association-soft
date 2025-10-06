package com.org.testApi.controllers;

import com.org.testApi.models.ProjectMember;
import com.org.testApi.payload.ProjectMemberPayload;
import com.org.testApi.services.ProjectMemberService;
import com.org.testApi.mapper.ProjectMemberMapper;
import com.org.testApi.services.ProjectService;
import com.org.testApi.services.MemberService;
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
@RequestMapping("/api/project-members")
@Tag(name = "Membre de projet", description = "Gestion des membres de projets")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MemberService memberService;

    @GetMapping
    @Operation(summary = "Récupérer tous les membres de projets", description = "Retourne une liste de tous les membres de projets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des membres de projets récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectMember.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<ProjectMember>> getAllProjectMembers() {
        List<ProjectMember> projectMembers = projectMemberService.getAllProjectMembers();
        return ResponseEntity.ok(projectMembers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un membre de projet par ID", description = "Retourne un membre de projet spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre de projet trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectMember.class))}),
            @ApiResponse(responseCode = "404", description = "Membre de projet non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ProjectMember> getProjectMemberById(
            @Parameter(description = "ID du membre de projet à récupérer") @PathVariable Long id) {
        return projectMemberService.getProjectMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau membre de projet", description = "Crée un nouveau membre de projet avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre de projet créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectMember.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de membre de projet à créer",
        content = @Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\n" +
                    "  \"project\": {\n" +
                    "    \"id\": 1\n" +
                    "  },\n" +
                    "  \"member\": {\n" +
                    "    \"id\": 3\n" +
                    "  },\n" +
                    "  \"roleInProject\": \"Développeur\",\n" +
                    "  \"joinDate\": \"2025-10-01\"\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<ProjectMember> createProjectMember(
            @Parameter(description = "Données du membre de projet à créer") @RequestBody ProjectMember projectMember) {
        ProjectMember savedProjectMember = projectMemberService.saveProjectMember(projectMember);
        return ResponseEntity.ok(savedProjectMember);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un membre de projet à partir d'un payload", description = "Crée un membre de projet en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre de projet créé avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectMember.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de payload pour créer un membre de projet",
        content = @Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\n" +
                    "  \"projectId\": 1,\n" +
                    "  \"memberId\": 3,\n" +
                    "  \"roleInProject\": \"Développeur\",\n" +
                    "  \"joinDate\": \"2025-10-01\"\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<ProjectMember> createProjectMemberFromPayload(
            @Parameter(description = "Données du payload pour créer le membre de projet") @RequestBody ProjectMemberPayload payload) {
        ProjectMember projectMember = projectMemberMapper.toEntityFromPayload(payload);
        
        // Set the project and member based on the IDs in the payload
        projectService.getProjectById(payload.getProjectId()).ifPresent(projectMember::setProject);
        memberService.getMemberById(payload.getMemberId()).ifPresent(projectMember::setMember);
        
        ProjectMember savedProjectMember = projectMemberService.saveProjectMember(projectMember);
        return ResponseEntity.ok(savedProjectMember);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un membre de projet", description = "Met à jour un membre de projet existant avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre de projet mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectMember.class))}),
            @ApiResponse(responseCode = "404", description = "Membre de projet non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de membre de projet à mettre à jour",
        content = @Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\n" +
                    "  \"project\": {\n" +
                    "    \"id\": 1\n" +
                    "  },\n" +
                    "  \"member\": {\n" +
                    "    \"id\": 3\n" +
                    "  },\n" +
                    "  \"roleInProject\": \"Développeur Sénior\",\n" +
                    "  \"joinDate\": \"2025-10-01\",\n" +
                    "  \"leaveDate\": \"2025-12-31\"\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<ProjectMember> updateProjectMember(
            @Parameter(description = "ID du membre de projet à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour du membre de projet") @RequestBody ProjectMember projectMember) {
        try {
            ProjectMember updatedProjectMember = projectMemberService.updateProjectMember(id, projectMember);
            return ResponseEntity.ok(updatedProjectMember);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour un membre de projet avec payload", description = "Met à jour un membre de projet existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre de projet mis à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectMember.class))}),
            @ApiResponse(responseCode = "404", description = "Membre de projet non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de payload pour mettre à jour un membre de projet",
        content = @Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\n" +
                    "  \"projectId\": 1,\n" +
                    "  \"memberId\": 3,\n" +
                    "  \"roleInProject\": \"Développeur Sénior\",\n" +
                    "  \"joinDate\": \"2025-10-01\",\n" +
                    "  \"leaveDate\": \"2025-12-31\"\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<ProjectMember> updateProjectMemberWithPayload(
            @Parameter(description = "ID du membre de projet à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour le membre de projet") @RequestBody ProjectMemberPayload payload) {
        return projectMemberService.getProjectMemberById(id)
                .map(projectMember -> {
                    projectMemberMapper.updateEntityFromPayload(payload, projectMember);
                    
                    // Set the project and member based on the IDs in the payload
                    projectService.getProjectById(payload.getProjectId()).ifPresent(projectMember::setProject);
                    memberService.getMemberById(payload.getMemberId()).ifPresent(projectMember::setMember);
                    
                    ProjectMember updatedProjectMember = projectMemberService.updateProjectMember(id, projectMember);
                    return ResponseEntity.ok(updatedProjectMember);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un membre de projet", description = "Supprime définitivement un membre de projet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membre de projet supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Membre de projet non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteProjectMember(
            @Parameter(description = "ID du membre de projet à supprimer") @PathVariable Long id) {
        projectMemberService.deleteProjectMember(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement un membre de projet", description = "Marque un membre de projet comme supprimé sans le retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membre de projet supprimé logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Membre de projet non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteProjectMember(
            @Parameter(description = "ID du membre de projet à supprimer logiquement") @PathVariable Long id) {
        projectMemberService.softDeleteProjectMember(id);
        return ResponseEntity.noContent().build();
    }
}