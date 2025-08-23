package com.org.testApi.mapper;

import com.org.testApi.dto.FinancialTransactionDTO;
import com.org.testApi.models.FinancialTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité FinancialTransaction et son DTO.
 */
@Mapper(componentModel = "spring")
public interface FinancialTransactionMapper extends BaseMapper<FinancialTransaction, FinancialTransactionDTO> {

    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "category", ignore = true)
    FinancialTransaction toEntity(FinancialTransactionDTO dto);

    @Mapping(target = "associationId", source = "association.id")
    @Mapping(target = "activityId", source = "activity.id")
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "type", source = "type")
    FinancialTransactionDTO toDto(FinancialTransaction entity);

}
