package com.org.testApi.controllers;

import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.services.FinancialTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-transactions")
public class FinancialTransactionController {

    @Autowired
    private FinancialTransactionService financialTransactionService;

    @GetMapping
    public ResponseEntity<List<FinancialTransaction>> getAllFinancialTransactions() {
        List<FinancialTransaction> transactions = financialTransactionService.getAllFinancialTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransaction> getFinancialTransactionById(@PathVariable Long id) {
        return financialTransactionService.getFinancialTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FinancialTransaction> createFinancialTransaction(@RequestBody FinancialTransaction transaction) {
        FinancialTransaction savedTransaction = financialTransactionService.saveFinancialTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialTransaction> updateFinancialTransaction(@PathVariable Long id, @RequestBody FinancialTransaction transaction) {
        try {
            FinancialTransaction updatedTransaction = financialTransactionService.updateFinancialTransaction(id, transaction);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialTransaction(@PathVariable Long id) {
        financialTransactionService.deleteFinancialTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteFinancialTransaction(@PathVariable Long id) {
        financialTransactionService.softDeleteFinancialTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
