package com.org.testApi.services;

import com.org.testApi.models.FinancialTransaction;
import java.util.List;
import java.util.Optional;

public interface FinancialTransactionService extends ObservableService<FinancialTransaction> {
    List<FinancialTransaction> getAllFinancialTransactions();
    Optional<FinancialTransaction> getFinancialTransactionById(Long id);
    FinancialTransaction saveFinancialTransaction(FinancialTransaction financialTransaction);
    FinancialTransaction updateFinancialTransaction(Long id, FinancialTransaction financialTransaction);
    void deleteFinancialTransaction(Long id);
    void softDeleteFinancialTransaction(Long id);
}
