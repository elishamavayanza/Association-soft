package com.org.testApi.services;

import com.org.testApi.models.FinancialCategory;
import java.util.List;
import java.util.Optional;

public interface FinancialCategoryService extends ObservableService<FinancialCategory> {
    List<FinancialCategory> getAllFinancialCategories();
    Optional<FinancialCategory> getFinancialCategoryById(Long id);
    FinancialCategory saveFinancialCategory(FinancialCategory financialCategory);
    FinancialCategory updateFinancialCategory(Long id, FinancialCategory financialCategory);
    void deleteFinancialCategory(Long id);
    void softDeleteFinancialCategory(Long id);
}
