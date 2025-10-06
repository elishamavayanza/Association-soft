package com.org.testApi.mapper;

import com.org.testApi.dto.RotatingGroupDTO;
import com.org.testApi.models.GroupStatus;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.RotationFrequency;
import com.org.testApi.payload.RotatingGroupPayload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RotatingGroupMapper {
    
    public RotatingGroupDTO toDTO(RotatingGroup rotatingGroup) {
        if (rotatingGroup == null) {
            return null;
        }
        
        return RotatingGroupDTO.builder()
                .id(rotatingGroup.getId())
                .createdDate(rotatingGroup.getCreatedDate())
                .lastModifiedDate(rotatingGroup.getLastModifiedDate())
                .createdBy(rotatingGroup.getCreatedBy())
                .lastModifiedBy(rotatingGroup.getLastModifiedBy())
                .active(rotatingGroup.isActive())
                .name(rotatingGroup.getName())
                .description(rotatingGroup.getDescription())
                .contributionAmount(rotatingGroup.getContributionAmount())
                .maxMembers(rotatingGroup.getMaxMembers())
                .rotationFrequency(rotatingGroup.getRotationFrequency() != null ? 
                                 rotatingGroup.getRotationFrequency().name() : null)
                .startDate(rotatingGroup.getStartDate())
                .endDate(rotatingGroup.getEndDate())
                .status(rotatingGroup.getStatus() != null ? 
                      rotatingGroup.getStatus().name() : null)
                .memberIds(rotatingGroup.getMembers() != null ? 
                         rotatingGroup.getMembers().stream().map(member -> member.getId()).collect(Collectors.toList()) : List.of())
                .roundIds(rotatingGroup.getRounds() != null ? 
                        rotatingGroup.getRounds().stream().map(round -> round.getId()).collect(Collectors.toList()) : List.of())
                .build();
    }
    
    public RotatingGroup toEntity(RotatingGroupDTO rotatingGroupDTO) {
        if (rotatingGroupDTO == null) {
            return null;
        }
        
        RotatingGroup rotatingGroup = new RotatingGroup();
        rotatingGroup.setId(rotatingGroupDTO.getId());
        rotatingGroup.setCreatedDate(rotatingGroupDTO.getCreatedDate());
        rotatingGroup.setLastModifiedDate(rotatingGroupDTO.getLastModifiedDate());
        rotatingGroup.setCreatedBy(rotatingGroupDTO.getCreatedBy());
        rotatingGroup.setLastModifiedBy(rotatingGroupDTO.getLastModifiedBy());
        rotatingGroup.setActive(rotatingGroupDTO.isActive());
        rotatingGroup.setName(rotatingGroupDTO.getName());
        rotatingGroup.setDescription(rotatingGroupDTO.getDescription());
        rotatingGroup.setContributionAmount(rotatingGroupDTO.getContributionAmount());
        rotatingGroup.setMaxMembers(rotatingGroupDTO.getMaxMembers());
        rotatingGroup.setRotationFrequency(rotatingGroupDTO.getRotationFrequency() != null ? 
                                         RotationFrequency.valueOf(rotatingGroupDTO.getRotationFrequency()) : null);
        rotatingGroup.setStartDate(rotatingGroupDTO.getStartDate());
        rotatingGroup.setEndDate(rotatingGroupDTO.getEndDate());
        rotatingGroup.setStatus(rotatingGroupDTO.getStatus() != null ? 
                               GroupStatus.valueOf(rotatingGroupDTO.getStatus()) : null);
        
        return rotatingGroup;
    }
    
    public List<RotatingGroupDTO> toDTOList(List<RotatingGroup> rotatingGroups) {
        return rotatingGroups.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public RotatingGroupDTO toDTO(RotatingGroupPayload payload) {
        if (payload == null) {
            return null;
        }
        
        return RotatingGroupDTO.builder()
                .createdDate(payload.getCreatedAt())
                .lastModifiedDate(payload.getUpdatedAt())
                .createdBy(payload.getCreatedBy() != null ? payload.getCreatedBy().toString() : null)
                .lastModifiedBy(payload.getUpdatedBy() != null ? payload.getUpdatedBy().toString() : null)
                .name(payload.getName())
                .description(payload.getDescription())
                .contributionAmount(payload.getContributionAmount())
                .maxMembers(payload.getMaxMembers())
                .rotationFrequency(payload.getRotationFrequency())
                .startDate(payload.getStartDate())
                .endDate(payload.getEndDate())
                .status(payload.getStatus())
                .memberIds(payload.getMemberIds())
                .build();
    }
}