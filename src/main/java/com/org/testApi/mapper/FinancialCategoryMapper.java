package com.org.testApi.mapper;

import com.org.testApi.dto.FinancialCategoryDTO;
import com.org.testApi.models.FinancialCategory;
import com.org.testApi.payload.FinancialCategoryPayload;
import com.org.testApi.models.Association;
import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.repository.AssociationRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper pour l'entité FinancialCategory et son DTO.
 */
@Mapper(componentModel = "spring")
public abstract class FinancialCategoryMapper implements BaseMapper<FinancialCategory, FinancialCategoryDTO> {
    
    private static final Logger logger = LoggerFactory.getLogger(FinancialCategoryMapper.class);

    @Autowired
    protected AssociationRepository associationRepository;

    @Mapping(target = "association", ignore = true)
    public abstract FinancialCategory toEntity(FinancialCategoryDTO dto);

    // CORRECTION: Utiliser simplement "type" au lieu de "type.name()"
    @Mapping(target = "type", source = "type")
    public abstract FinancialCategoryDTO toDto(FinancialCategory entity);

    // Payload mappings - CORRIGÉ pour gérer correctement l'association
    @Mapping(target = "type", source = "type", qualifiedByName = "mapTransactionType")
    @Mapping(target = "association", source = "associationId", qualifiedByName = "associationFromId")
    public abstract FinancialCategory toEntityFromPayload(FinancialCategoryPayload payload);

    @Mapping(target = "type", source = "type", qualifiedByName = "mapTransactionTypeToString")
    @Mapping(target = "associationId", source = "association.id")
    public abstract FinancialCategoryPayload toPayload(FinancialCategory entity);

    @Mapping(target = "type", source = "type", qualifiedByName = "mapTransactionType")
    @Mapping(target = "association", source = "associationId", qualifiedByName = "associationFromId")
    public abstract void updateEntityFromPayload(FinancialCategoryPayload payload, @MappingTarget FinancialCategory entity);

    @Named("mapTransactionType")
    protected FinancialTransaction.TransactionType mapTransactionType(String type) {
        logger.info("Mapping transaction type from string: {}", type);
        if (type == null) {
            return null;
        }
        
        switch (type.toUpperCase()) {
            case "REVENUE":
            case "INCOME":
                return FinancialTransaction.TransactionType.INCOME;
            case "EXPENSE":
                return FinancialTransaction.TransactionType.EXPENSE;
            default:
                logger.warn("Unknown transaction type: {}", type);
                throw new IllegalArgumentException("Invalid transaction type: " + type);
        }
    }
    
    @Named("mapTransactionTypeToString")
    protected String mapTransactionTypeToString(FinancialTransaction.TransactionType type) {
        logger.info("Mapping transaction type to string: {}", type);
        if (type == null) {
            return null;
        }
        
        switch (type) {
            case INCOME:
                return "INCOME";
            case EXPENSE:
                return "EXPENSE";
            default:
                return type.name();
        }
    }

    @Named("associationFromId")
    protected Association associationFromId(Long associationId) {
        logger.info("Looking up association with id: {}", associationId);
        if (associationId == null) {
            logger.warn("Association ID is null");
            return null;
        }
        // Check if repository is properly injected
        if (associationRepository == null) {
            logger.error("AssociationRepository not injected");
            throw new IllegalStateException("AssociationRepository not injected");
        }
        
        return associationRepository.findById(associationId).orElse(null);
    }
}