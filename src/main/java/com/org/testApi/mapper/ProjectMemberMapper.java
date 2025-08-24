package com.org.testApi.mapper;

import com.org.testApi.dto.ProjectMemberDTO;
import com.org.testApi.models.ProjectMember;
import com.org.testApi.payload.ProjectMemberPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour l'entité ProjectMember et son DTO.
 */
@Mapper(componentModel = "spring")
public interface ProjectMemberMapper extends BaseMapper<ProjectMember, ProjectMemberDTO> {

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "member", ignore = true)
    ProjectMember toEntity(ProjectMemberDTO dto);

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "memberId", source = "member.id")
    ProjectMemberDTO toDto(ProjectMember entity);

    // Payload mappings
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "member", ignore = true)
    ProjectMember toEntityFromPayload(ProjectMemberPayload payload);

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "memberId", source = "member.id")
    ProjectMemberPayload toPayload(ProjectMember entity);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "member", ignore = true)
    void updateEntityFromPayload(ProjectMemberPayload payload, @MappingTarget ProjectMember entity);
}
