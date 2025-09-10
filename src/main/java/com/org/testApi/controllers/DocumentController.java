package com.org.testApi.controllers;

import com.org.testApi.models.Document;
import com.org.testApi.payload.DocumentPayload;
import com.org.testApi.services.DocumentService;
import com.org.testApi.mapper.DocumentMapper;
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
@RequestMapping("/api/documents")
@Tag(name = "Document", description = "Gestion des documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentMapper documentMapper;

    @GetMapping
    @Operation(summary = "Récupérer tous les documents", description = "Retourne une liste de tous les documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des documents récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un document par ID", description = "Retourne un document spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document trouvé",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Document> getDocumentById(
            @Parameter(description = "ID du document à récupérer") @PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau document", description = "Crée un nouveau document avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Document> createDocument(
            @Parameter(description = "Données du document à créer") @RequestBody Document document) {
        Document savedDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(savedDocument);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un document à partir d'un payload", description = "Crée un document en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document créé avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Document> createDocumentFromPayload(
            @Parameter(description = "Données du payload pour créer le document") @RequestBody DocumentPayload payload) {
        Document document = documentMapper.toEntityFromPayload(payload);
        Document savedDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(savedDocument);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un document", description = "Met à jour un document existant avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Document> updateDocument(
            @Parameter(description = "ID du document à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour du document") @RequestBody Document document) {
        try {
            Document updatedDocument = documentService.updateDocument(id, document);
            return ResponseEntity.ok(updatedDocument);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(summary = "Mettre à jour un document avec payload", description = "Met à jour un document existant en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document mis à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Document> updateDocumentWithPayload(
            @Parameter(description = "ID du document à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour le document") @RequestBody DocumentPayload payload) {
        return documentService.getDocumentById(id)
                .map(document -> {
                    documentMapper.updateEntityFromPayload(payload, document);
                    Document updatedDocument = documentService.updateDocument(id, document);
                    return ResponseEntity.ok(updatedDocument);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un document", description = "Supprime définitivement un document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteDocument(
            @Parameter(description = "ID du document à supprimer") @PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquement un document", description = "Marque un document comme supprimé sans le retirer de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document supprimé logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteDocument(
            @Parameter(description = "ID du document à supprimer logiquement") @PathVariable Long id) {
        documentService.softDeleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
