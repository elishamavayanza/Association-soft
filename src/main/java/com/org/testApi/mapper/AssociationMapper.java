package com.org.testApi.mapper;

import com.org.testApi.dto.AssociationDTO;
import com.org.testApi.dto.request.AssociationRequestDTO;
import com.org.testApi.dto.response.AssociationResponseDTO;
import com.org.testApi.models.Association;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Association et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface AssociationMapper extends BaseMapper<Association, AssociationDTO> {

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "financialTransactions", ignore = true)
    Association toEntity(AssociationDTO dto);

    @Mapping(target = "address", source = "location") // Mapping spécifique pour le champ address
    AssociationResponseDTO toResponseDto(Association entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "financialTransactions", ignore = true)
    @Mapping(target = "location", source = "address") // Mapping spécifique pour le champ location
    Association toEntityFromRequest(AssociationRequestDTO requestDTO);
}