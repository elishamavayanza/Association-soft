package com.org.testApi.mapper;

import com.org.testApi.dto.ContributionDTO;
import com.org.testApi.models.Contribution;
import com.org.testApi.models.ContributionStatus;
import com.org.testApi.payload.ContributionPayload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContributionMapper {
    
    public ContributionDTO toDTO(Contribution contribution) {
        if (contribution == null) {
            return null;
        }
        
        return ContributionDTO.builder()
                .id(contribution.getId())
                .createdDate(contribution.getCreatedDate())
                .lastModifiedDate(contribution.getLastModifiedDate())
                .createdBy(contribution.getCreatedBy())
                .lastModifiedBy(contribution.getLastModifiedBy())
                .active(contribution.isActive())
                .amount(contribution.getAmount())
                .contributionDate(contribution.getContributionDate())
                .status(contribution.getStatus() != null ? contribution.getStatus().name() : null)
                .memberId(contribution.getMember() != null ? contribution.getMember().getId() : null)
                .roundId(contribution.getRound() != null ? contribution.getRound().getId() : null)
                .build();
    }
    
    public Contribution toEntity(ContributionDTO contributionDTO) {
        if (contributionDTO == null) {
            return null;
        }
        
        Contribution contribution = new Contribution();
        contribution.setId(contributionDTO.getId());
        contribution.setCreatedDate(contributionDTO.getCreatedDate());
        contribution.setLastModifiedDate(contributionDTO.getLastModifiedDate());
        contribution.setCreatedBy(contributionDTO.getCreatedBy());
        contribution.setLastModifiedBy(contributionDTO.getLastModifiedBy());
        contribution.setActive(contributionDTO.isActive());
        contribution.setAmount(contributionDTO.getAmount());
        contribution.setContributionDate(contributionDTO.getContributionDate());
        contribution.setStatus(contributionDTO.getStatus() != null ? 
                             ContributionStatus.valueOf(contributionDTO.getStatus()) : null);
        
        return contribution;
    }
    
    public List<ContributionDTO> toDTOList(List<Contribution> contributions) {
        return contributions.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public ContributionDTO toDTO(ContributionPayload payload) {
        if (payload == null) {
            return null;
        }
        
        return ContributionDTO.builder()
                .createdDate(payload.getCreatedAt())
                .lastModifiedDate(payload.getUpdatedAt())
                .createdBy(payload.getCreatedBy() != null ? payload.getCreatedBy().toString() : null)
                .lastModifiedBy(payload.getUpdatedBy() != null ? payload.getUpdatedBy().toString() : null)
                .amount(payload.getAmount())
                .contributionDate(payload.getContributionDate())
                .status(payload.getStatus())
                .memberId(payload.getMemberId())
                .roundId(payload.getRoundId())
                .build();
    }
}