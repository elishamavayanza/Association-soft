package com.org.testApi.mapper;

import com.org.testApi.dto.FinancialCategoryDTO;
import com.org.testApi.models.FinancialCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité FinancialCategory et son DTO.
 */
@Mapper(componentModel = "spring")
public interface FinancialCategoryMapper extends BaseMapper<FinancialCategory, FinancialCategoryDTO> {

    @Mapping(target = "association", ignore = true)
    FinancialCategory toEntity(FinancialCategoryDTO dto);

    @Mapping(target = "type", source = "type.name()")
    FinancialCategoryDTO toDto(FinancialCategory entity);
}
