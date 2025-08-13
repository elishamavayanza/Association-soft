package com.org.testApi.repository;

import com.org.testApi.models.FinancialCategory;
import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialCategoryRepository extends BaseRepository<FinancialCategory, Long> {

    List<FinancialCategory> findByNameContainingIgnoreCase(String name);

    List<FinancialCategory> findByType(FinancialTransaction.TransactionType type);

    List<FinancialCategory> findByAssociationId(Long associationId);

    boolean existsByNameAndAssociationId(String name, Long associationId);

    @Query("SELECT fc FROM FinancialCategory fc JOIN FETCH fc.association WHERE fc.id = :id")
    Optional<FinancialCategory> findByIdWithAssociation(@Param("id") Long id);
}
