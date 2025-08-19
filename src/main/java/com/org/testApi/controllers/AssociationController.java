package com.org.testApi.controllers;

import com.org.testApi.models.Association;
import com.org.testApi.services.AssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/associations")
public class AssociationController {

    @Autowired
    private AssociationService associationService;

    @GetMapping
    public ResponseEntity<List<Association>> getAllAssociations() {
        List<Association> associations = associationService.getAllAssociations();
        return ResponseEntity.ok(associations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Association> getAssociationById(@PathVariable Long id) {
        return associationService.getAssociationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Association> createAssociation(@RequestBody Association association) {
        Association savedAssociation = associationService.saveAssociation(association);
        return ResponseEntity.ok(savedAssociation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Association> updateAssociation(@PathVariable Long id, @RequestBody Association association) {
        try {
            Association updatedAssociation = associationService.updateAssociation(id, association);
            return ResponseEntity.ok(updatedAssociation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssociation(@PathVariable Long id) {
        associationService.deleteAssociation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteAssociation(@PathVariable Long id) {
        associationService.softDeleteAssociation(id);
        return ResponseEntity.noContent().build();
    }
}
