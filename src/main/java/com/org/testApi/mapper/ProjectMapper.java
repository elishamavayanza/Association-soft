package com.org.testApi.mapper;

import com.org.testApi.dto.ProjectDTO;
import com.org.testApi.dto.request.ProjectRequestDTO;
import com.org.testApi.dto.response.ProjectResponseDTO;
import com.org.testApi.models.Project;
import com.org.testApi.models.Activity;
import com.org.testApi.models.ProjectMember;
import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.payload.ProjectPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;
import java.util.stream.Collectors;

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

    // Payload mappings
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Project toEntityFromPayload(ProjectPayload payload);

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "associationId", source = "association.id")
    @Mapping(target = "activityIds", source = "activities")
    @Mapping(target = "memberIds", source = "members")
    @Mapping(target = "transactionIds", source = "transactions")
    ProjectPayload toPayload(Project entity);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    void updateEntityFromPayload(ProjectPayload payload, @MappingTarget Project entity);

    // Méthodes de mapping personnalisées pour convertir les listes d'entités en listes d'IDs
    default List<Long> mapActivityListToIdList(List<Activity> activities) {
        if (activities == null) {
            return null;
        }
        return activities.stream().map(Activity::getId).collect(Collectors.toList());
    }

    default List<Long> mapProjectMemberListToIdList(List<ProjectMember> members) {
        if (members == null) {
            return null;
        }
        return members.stream().map(ProjectMember::getId).collect(Collectors.toList());
    }

    default List<Long> mapFinancialTransactionListToIdList(List<FinancialTransaction> transactions) {
        if (transactions == null) {
            return null;
        }
        return transactions.stream().map(FinancialTransaction::getId).collect(Collectors.toList());
    }
}
