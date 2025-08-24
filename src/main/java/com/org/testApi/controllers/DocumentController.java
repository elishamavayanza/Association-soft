package com.org.testApi.controllers;

import com.org.testApi.models.Document;
import com.org.testApi.payload.DocumentPayload;
import com.org.testApi.services.DocumentService;
import com.org.testApi.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentMapper documentMapper;

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document savedDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(savedDocument);
    }

    @PostMapping("/payload")
    public ResponseEntity<Document> createDocumentFromPayload(@RequestBody DocumentPayload payload) {
        Document document = documentMapper.toEntityFromPayload(payload);
        Document savedDocument = documentService.saveDocument(document);
        return ResponseEntity.ok(savedDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        try {
            Document updatedDocument = documentService.updateDocument(id, document);
            return ResponseEntity.ok(updatedDocument);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    public ResponseEntity<Document> updateDocumentWithPayload(@PathVariable Long id, @RequestBody DocumentPayload payload) {
        return documentService.getDocumentById(id)
                .map(document -> {
                    documentMapper.updateEntityFromPayload(payload, document);
                    Document updatedDocument = documentService.updateDocument(id, document);
                    return ResponseEntity.ok(updatedDocument);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteDocument(@PathVariable Long id) {
        documentService.softDeleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
