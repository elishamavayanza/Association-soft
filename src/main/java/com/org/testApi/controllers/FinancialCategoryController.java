package com.org.testApi.controllers;

import com.org.testApi.models.FinancialCategory;
import com.org.testApi.payload.FinancialCategoryPayload;
import com.org.testApi.services.FinancialCategoryService;
import com.org.testApi.mapper.FinancialCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-categories")
public class FinancialCategoryController {

    @Autowired
    private FinancialCategoryService financialCategoryService;

    @Autowired
    private FinancialCategoryMapper financialCategoryMapper;

    @GetMapping
    public ResponseEntity<List<FinancialCategory>> getAllFinancialCategories() {
        List<FinancialCategory> financialCategories = financialCategoryService.getAllFinancialCategories();
        return ResponseEntity.ok(financialCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialCategory> getFinancialCategoryById(@PathVariable Long id) {
        return financialCategoryService.getFinancialCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FinancialCategory> createFinancialCategory(@RequestBody FinancialCategory financialCategory) {
        FinancialCategory savedFinancialCategory = financialCategoryService.saveFinancialCategory(financialCategory);
        return ResponseEntity.ok(savedFinancialCategory);
    }

    @PostMapping("/payload")
    public ResponseEntity<FinancialCategory> createFinancialCategoryFromPayload(@RequestBody FinancialCategoryPayload payload) {
        FinancialCategory financialCategory = financialCategoryMapper.toEntityFromPayload(payload);
        FinancialCategory savedFinancialCategory = financialCategoryService.saveFinancialCategory(financialCategory);
        return ResponseEntity.ok(savedFinancialCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialCategory> updateFinancialCategory(@PathVariable Long id, @RequestBody FinancialCategory financialCategory) {
        try {
            FinancialCategory updatedFinancialCategory = financialCategoryService.updateFinancialCategory(id, financialCategory);
            return ResponseEntity.ok(updatedFinancialCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    public ResponseEntity<FinancialCategory> updateFinancialCategoryWithPayload(@PathVariable Long id, @RequestBody FinancialCategoryPayload payload) {
        return financialCategoryService.getFinancialCategoryById(id)
                .map(financialCategory -> {
                    financialCategoryMapper.updateEntityFromPayload(payload, financialCategory);
                    FinancialCategory updatedFinancialCategory = financialCategoryService.updateFinancialCategory(id, financialCategory);
                    return ResponseEntity.ok(updatedFinancialCategory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialCategory(@PathVariable Long id) {
        financialCategoryService.deleteFinancialCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteFinancialCategory(@PathVariable Long id) {
        financialCategoryService.softDeleteFinancialCategory(id);
        return ResponseEntity.noContent().build();
    }
}
