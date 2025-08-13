package com.org.testApi.services;

import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.repository.FinancialTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    private List<Observer<FinancialTransaction>> observers = new ArrayList<>();

    @Override
    public List<FinancialTransaction> getAllFinancialTransactions() {
        return financialTransactionRepository.findAll();
    }

    @Override
    public Optional<FinancialTransaction> getFinancialTransactionById(Long id) {
        return financialTransactionRepository.findById(id);
    }

    @Override
    public FinancialTransaction saveFinancialTransaction(FinancialTransaction financialTransaction) {
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
        notifyObservers("SAVE", savedFinancialTransaction);
        return savedFinancialTransaction;
    }

    @Override
    public FinancialTransaction updateFinancialTransaction(Long id, FinancialTransaction financialTransaction) {
        if (financialTransactionRepository.existsById(id)) {
            financialTransaction.setId(id);
            FinancialTransaction updatedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
            notifyObservers("UPDATE", updatedFinancialTransaction);
            return updatedFinancialTransaction;
        }
        throw new RuntimeException("FinancialTransaction not found with id: " + id);
    }

    @Override
    public void deleteFinancialTransaction(Long id) {
        FinancialTransaction financialTransaction = financialTransactionRepository.findById(id).orElse(null);
        financialTransactionRepository.deleteById(id);
        if (financialTransaction != null) {
            notifyObservers("DELETE", financialTransaction);
        }
    }

    @Override
    public void softDeleteFinancialTransaction(Long id) {
        FinancialTransaction financialTransaction = financialTransactionRepository.findById(id).orElse(null);
        financialTransactionRepository.softDelete(id);
        if (financialTransaction != null) {
            notifyObservers("SOFT_DELETE", financialTransaction);
        }
    }

    @Override
    public void addObserver(Observer<FinancialTransaction> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<FinancialTransaction> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, FinancialTransaction entity) {
        for (Observer<FinancialTransaction> observer : observers) {
            observer.update(event, entity);
        }
    }
}
