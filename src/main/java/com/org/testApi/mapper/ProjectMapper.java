package com.org.testApi.mapper;

import com.org.testApi.dto.ProjectDTO;
import com.org.testApi.dto.request.ProjectRequestDTO;
import com.org.testApi.dto.response.ProjectResponseDTO;
import com.org.testApi.models.Project;
import com.org.testApi.models.Activity;
import com.org.testApi.models.ProjectMember;
import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.models.Association;
import com.org.testApi.payload.ProjectPayload;
import com.org.testApi.repository.AssociationRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour l'entité Project et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public abstract class ProjectMapper implements BaseMapper<Project, ProjectDTO> {

    @Autowired
    protected AssociationRepository associationRepository;

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    public abstract Project toEntity(ProjectDTO dto);

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "associationId", source = "association.id")
    public abstract ProjectResponseDTO toResponseDto(Project entity);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    public abstract Project toEntityFromRequest(ProjectRequestDTO requestDTO);

    // Payload mappings
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    public Project toEntityFromPayload(ProjectPayload payload) {
        if (payload == null) {
            return null;
        }
        
        Project project = new Project();
        project.setId(payload.getId());
        project.setName(payload.getName());
        project.setDescription(payload.getDescription());
        project.setStartDate(payload.getStartDate());
        project.setEndDate(payload.getEndDate());
        project.setStatus(payload.getStatus() != null ? 
            Project.ProjectStatus.valueOf(payload.getStatus()) : 
            Project.ProjectStatus.PLANNING);
        
        // Handle association relationship
        if (payload.getAssociationId() != null) {
            Association association = associationRepository.findById(payload.getAssociationId()).orElse(null);
            project.setAssociation(association);
        }
        
        return project;
    }

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "associationId", source = "association.id")
    @Mapping(target = "activityIds", source = "activities")
    @Mapping(target = "memberIds", source = "members")
    @Mapping(target = "transactionIds", source = "transactions")
    public abstract ProjectPayload toPayload(Project entity);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    public abstract void updateEntityFromPayload(ProjectPayload payload, @MappingTarget Project entity);

    // Méthodes de mapping personnalisées pour convertir les listes d'entités en listes d'IDs
    public List<Long> mapActivityListToIdList(List<Activity> activities) {
        if (activities == null) {
            return null;
        }
        return activities.stream().map(Activity::getId).collect(Collectors.toList());
    }

    public List<Long> mapProjectMemberListToIdList(List<ProjectMember> members) {
        if (members == null) {
            return null;
        }
        return members.stream().map(ProjectMember::getId).collect(Collectors.toList());
    }

    public List<Long> mapFinancialTransactionListToIdList(List<FinancialTransaction> transactions) {
        if (transactions == null) {
            return null;
        }
        return transactions.stream().map(FinancialTransaction::getId).collect(Collectors.toList());
    }
}