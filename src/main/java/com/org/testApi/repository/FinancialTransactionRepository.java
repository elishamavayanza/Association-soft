package com.org.testApi.repository;

import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialTransactionRepository extends BaseRepository<FinancialTransaction, Long> {

    List<FinancialTransaction> findByType(FinancialTransaction.TransactionType type);

    List<FinancialTransaction> findByAssociationId(Long associationId);

    List<FinancialTransaction> findByActivityId(Long activityId);

    List<FinancialTransaction> findByProjectId(Long projectId);

    List<FinancialTransaction> findByCategoryId(Long categoryId);

    List<FinancialTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    List<FinancialTransaction> findByAmountGreaterThanEqual(BigDecimal amount);

    List<FinancialTransaction> findByAmountLessThanEqual(BigDecimal amount);

    Page<FinancialTransaction> findByAssociationId(Long associationId, Pageable pageable);

    @Query("SELECT SUM(ft.amount) FROM FinancialTransaction ft WHERE ft.association.id = :associationId AND ft.type = 'INCOME'")
    BigDecimal sumIncomeByAssociationId(@Param("associationId") Long associationId);

    @Query("SELECT SUM(ft.amount) FROM FinancialTransaction ft WHERE ft.association.id = :associationId AND ft.type = 'EXPENSE'")
    BigDecimal sumExpenseByAssociationId(@Param("associationId") Long associationId);

    @Query("SELECT ft FROM FinancialTransaction ft JOIN FETCH ft.association WHERE ft.id = :id")
    Optional<FinancialTransaction> findByIdWithAssociation(@Param("id") Long id);

    // Méthodes ajoutées pour le ReportService
    List<FinancialTransaction> findByTransactionDateBetweenAndAmountGreaterThan(
            LocalDateTime startDate, LocalDateTime endDate, double amount);

    List<FinancialTransaction> findByTransactionDateBetweenAndAmountLessThan(
            LocalDateTime startDate, LocalDateTime endDate, double amount);

    List<FinancialTransaction> findByTransactionDateBetween(
            LocalDateTime startDate, LocalDateTime endDate);
}
