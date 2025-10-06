package com.org.testApi.mapper;

import com.org.testApi.dto.RoundDTO;
import com.org.testApi.models.Round;
import com.org.testApi.models.RoundStatus;
import com.org.testApi.payload.RoundPayload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoundMapper {
    
    public RoundDTO toDTO(Round round) {
        if (round == null) {
            return null;
        }
        
        return RoundDTO.builder()
                .id(round.getId())
                .createdDate(round.getCreatedDate())
                .lastModifiedDate(round.getLastModifiedDate())
                .createdBy(round.getCreatedBy())
                .lastModifiedBy(round.getLastModifiedBy())
                .active(round.isActive())
                .roundNumber(round.getRoundNumber())
                .startDate(round.getStartDate())
                .endDate(round.getEndDate())
                .status(round.getStatus() != null ? round.getStatus().name() : null)
                .rotatingGroupId(round.getRotatingGroup() != null ? round.getRotatingGroup().getId() : null)
                .contributionIds(round.getContributions() != null ? 
                               round.getContributions().stream().map(contribution -> contribution.getId()).collect(Collectors.toList()) : List.of())
                .penaltyIds(round.getPenalties() != null ? 
                          round.getPenalties().stream().map(penalty -> penalty.getId()).collect(Collectors.toList()) : List.of())
                .build();
    }
    
    public Round toEntity(RoundDTO roundDTO) {
        if (roundDTO == null) {
            return null;
        }
        
        Round round = new Round();
        round.setId(roundDTO.getId());
        round.setCreatedDate(roundDTO.getCreatedDate());
        round.setLastModifiedDate(roundDTO.getLastModifiedDate());
        round.setCreatedBy(roundDTO.getCreatedBy());
        round.setLastModifiedBy(roundDTO.getLastModifiedBy());
        round.setActive(roundDTO.isActive());
        round.setRoundNumber(roundDTO.getRoundNumber());
        round.setStartDate(roundDTO.getStartDate());
        round.setEndDate(roundDTO.getEndDate());
        round.setStatus(roundDTO.getStatus() != null ? RoundStatus.valueOf(roundDTO.getStatus()) : null);
        
        return round;
    }
    
    public List<RoundDTO> toDTOList(List<Round> rounds) {
        return rounds.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public RoundDTO toDTO(RoundPayload payload) {
        if (payload == null) {
            return null;
        }
        
        return RoundDTO.builder()
                .createdDate(payload.getCreatedAt())
                .lastModifiedDate(payload.getUpdatedAt())
                .createdBy(payload.getCreatedBy() != null ? payload.getCreatedBy().toString() : null)
                .lastModifiedBy(payload.getUpdatedBy() != null ? payload.getUpdatedBy().toString() : null)
                .roundNumber(payload.getRoundNumber())
                .startDate(payload.getStartDate())
                .endDate(payload.getEndDate())
                .status(payload.getStatus())
                .rotatingGroupId(payload.getRotatingGroupId())
                .contributionIds(payload.getContributionIds())
                .penaltyIds(payload.getPenaltyIds())
                .build();
    }
}