package com.org.testApi.mapper;

import com.org.testApi.dto.ProjectDTO;
import com.org.testApi.dto.request.ProjectRequestDTO;
import com.org.testApi.dto.response.ProjectResponseDTO;
import com.org.testApi.models.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Project et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends BaseMapper<Project, ProjectDTO> {

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    Project toEntity(ProjectDTO dto);

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "associationId", source = "association.id")
    ProjectResponseDTO toResponseDto(Project entity);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    Project toEntityFromRequest(ProjectRequestDTO requestDTO);
}
