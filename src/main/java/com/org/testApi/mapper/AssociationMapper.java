package com.org.testApi.mapper;

import com.org.testApi.dto.AssociationDTO;
import com.org.testApi.dto.request.AssociationRequestDTO;
import com.org.testApi.dto.response.AssociationResponseDTO;
import com.org.testApi.models.Association;
import com.org.testApi.payload.AssociationPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour l'entité Association et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface AssociationMapper extends BaseMapper<Association, AssociationDTO> {

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Association toEntity(AssociationDTO dto);

    @Mapping(target = "address", source = "location") // Mapping spécifique pour le champ address
    AssociationResponseDTO toResponseDto(Association entity);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "location", source = "address") // Mapping spécifique pour le champ location
    Association toEntityFromRequest(AssociationRequestDTO requestDTO);

    // Payload mappings
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Association toEntityFromPayload(AssociationPayload payload);

    @Mapping(target = "memberIds", ignore = true)
    @Mapping(target = "activityIds", ignore = true)
    @Mapping(target = "transactionIds", ignore = true)
    @Mapping(target = "documentIds", ignore = true)
    AssociationPayload toPayload(Association entity);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "documents", ignore = true)
    void updateEntityFromPayload(AssociationPayload payload, @MappingTarget Association entity);
}
