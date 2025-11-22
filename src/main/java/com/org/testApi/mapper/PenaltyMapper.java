package com.org.testApi.mapper;

import com.org.testApi.dto.PenaltyDTO;
import com.org.testApi.models.Penalty;
import com.org.testApi.models.PenaltyStatus;
import com.org.testApi.models.PenaltyType;
import com.org.testApi.payload.PenaltyPayload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PenaltyMapper {
    
    public PenaltyDTO toDTO(Penalty penalty) {
        if (penalty == null) {
            return null;
        }
        
        return PenaltyDTO.builder()
                .id(penalty.getId())
                .createdDate(penalty.getCreatedDate())
                .lastModifiedDate(penalty.getLastModifiedDate())
                .createdBy(penalty.getCreatedBy())
                .lastModifiedBy(penalty.getLastModifiedBy())
                .active(penalty.isActive())
                .amount(penalty.getAmount())
                .reason(penalty.getReason())
                .penaltyType(penalty.getPenaltyType() != null ? penalty.getPenaltyType().name() : null)
                .penaltyDate(penalty.getPenaltyDate())
                .status(penalty.getStatus() != null ? penalty.getStatus().name() : null)
                .memberId(penalty.getMember() != null ? penalty.getMember().getId() : null)
                .roundId(penalty.getRound() != null ? penalty.getRound().getId() : null)
                .build();
    }
    
    public Penalty toEntity(PenaltyDTO penaltyDTO) {
        if (penaltyDTO == null) {
            return null;
        }
        
        Penalty penalty = new Penalty();
        penalty.setId(penaltyDTO.getId());
        penalty.setCreatedDate(penaltyDTO.getCreatedDate());
        penalty.setLastModifiedDate(penaltyDTO.getLastModifiedDate());
        penalty.setCreatedBy(penaltyDTO.getCreatedBy());
        penalty.setLastModifiedBy(penaltyDTO.getLastModifiedBy());
        penalty.setActive(penaltyDTO.isActive());
        penalty.setAmount(penaltyDTO.getAmount());
        penalty.setReason(penaltyDTO.getReason());
        penalty.setPenaltyType(penaltyDTO.getPenaltyType() != null ? 
                             PenaltyType.valueOf(penaltyDTO.getPenaltyType()) : null);
        penalty.setPenaltyDate(penaltyDTO.getPenaltyDate());
        penalty.setStatus(penaltyDTO.getStatus() != null ? 
                        PenaltyStatus.valueOf(penaltyDTO.getStatus()) : null);
        
        return penalty;
    }
    
    public List<PenaltyDTO> toDTOList(List<Penalty> penalties) {
        return penalties.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public PenaltyDTO toDTO(PenaltyPayload payload) {
        if (payload == null) {
            return null;
        }
        
        return PenaltyDTO.builder()
                .createdDate(payload.getCreatedAt())
                .lastModifiedDate(payload.getUpdatedAt())
                .createdBy(payload.getCreatedBy() != null ? payload.getCreatedBy().toString() : null)
                .lastModifiedBy(payload.getUpdatedBy() != null ? payload.getUpdatedBy().toString() : null)
                .amount(payload.getAmount())
                .reason(payload.getReason())
                .penaltyType(payload.getPenaltyType())
                .penaltyDate(payload.getPenaltyDate())
                .status(payload.getStatus())
                .memberId(payload.getMemberId())
                .roundId(payload.getRoundId())
                .build();
    }
}