package com.org.testApi.services;

import com.org.testApi.models.FinancialCategory;
import com.org.testApi.repository.FinancialCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class FinancialCategoryServiceImpl implements FinancialCategoryService {

    @Autowired
    private FinancialCategoryRepository financialCategoryRepository;

    private List<Observer<FinancialCategory>> observers = new ArrayList<>();

    @Override
    public List<FinancialCategory> getAllFinancialCategories() {
        return financialCategoryRepository.findAll();
    }

    @Override
    public Optional<FinancialCategory> getFinancialCategoryById(Long id) {
        return financialCategoryRepository.findById(id);
    }

    @Override
    public FinancialCategory saveFinancialCategory(FinancialCategory financialCategory) {
        FinancialCategory savedFinancialCategory = financialCategoryRepository.save(financialCategory);
        notifyObservers("SAVE", savedFinancialCategory);
        return savedFinancialCategory;
    }

    @Override
    public FinancialCategory updateFinancialCategory(Long id, FinancialCategory financialCategory) {
        if (financialCategoryRepository.existsById(id)) {
            financialCategory.setId(id);
            FinancialCategory updatedFinancialCategory = financialCategoryRepository.save(financialCategory);
            notifyObservers("UPDATE", updatedFinancialCategory);
            return updatedFinancialCategory;
        }
        throw new RuntimeException("FinancialCategory not found with id: " + id);
    }

    @Override
    public void deleteFinancialCategory(Long id) {
        FinancialCategory financialCategory = financialCategoryRepository.findById(id).orElse(null);
        financialCategoryRepository.deleteById(id);
        if (financialCategory != null) {
            notifyObservers("DELETE", financialCategory);
        }
    }

    @Override
    public void softDeleteFinancialCategory(Long id) {
        FinancialCategory financialCategory = financialCategoryRepository.findById(id).orElse(null);
        financialCategoryRepository.softDelete(id);
        if (financialCategory != null) {
            notifyObservers("SOFT_DELETE", financialCategory);
        }
    }

    @Override
    public void addObserver(Observer<FinancialCategory> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<FinancialCategory> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, FinancialCategory entity) {
        for (Observer<FinancialCategory> observer : observers) {
            observer.update(event, entity);
        }
    }
}
