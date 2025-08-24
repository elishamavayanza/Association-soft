package com.org.testApi.mapper;

import com.org.testApi.dto.FinancialCategoryDTO;
import com.org.testApi.models.FinancialCategory;
import com.org.testApi.payload.FinancialCategoryPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour l'entité FinancialCategory et son DTO.
 */
@Mapper(componentModel = "spring")
public interface FinancialCategoryMapper extends BaseMapper<FinancialCategory, FinancialCategoryDTO> {

    @Mapping(target = "association", ignore = true)
    FinancialCategory toEntity(FinancialCategoryDTO dto);

    // CORRECTION: Utiliser simplement "type" au lieu de "type.name()"
    @Mapping(target = "type", source = "type")
    FinancialCategoryDTO toDto(FinancialCategory entity);

    // Payload mappings
    @Mapping(target = "association", ignore = true)
    FinancialCategory toEntityFromPayload(FinancialCategoryPayload payload);

    @Mapping(target = "associationId", source = "association.id")
    FinancialCategoryPayload toPayload(FinancialCategory entity);

    @Mapping(target = "association", ignore = true)
    void updateEntityFromPayload(FinancialCategoryPayload payload, @MappingTarget FinancialCategory entity);
}
