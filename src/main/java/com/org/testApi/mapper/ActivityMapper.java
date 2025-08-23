package com.org.testApi.mapper;

import com.org.testApi.dto.ActivityDTO;
import com.org.testApi.dto.request.ActivityRequestDTO;
import com.org.testApi.dto.response.ActivityResponseDTO;
import com.org.testApi.models.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Activity et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface ActivityMapper extends BaseMapper<Activity, ActivityDTO> {

    @Mapping(target = "association", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "financialTransactions", ignore = true)
    Activity toEntity(ActivityDTO dto);

    @Mapping(target = "associationId", source = "association.id")
    @Mapping(target = "projectId", source = "project.id")
    ActivityResponseDTO toResponseDto(Activity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "financialTransactions", ignore = true)
    Activity toEntityFromRequest(ActivityRequestDTO requestDTO);

}