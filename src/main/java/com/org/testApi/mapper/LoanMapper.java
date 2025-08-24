package com.org.testApi.mapper;

import com.org.testApi.dto.LoanDTO;
import com.org.testApi.dto.request.LoanRequestDTO;
import com.org.testApi.dto.response.LoanResponseDTO;
import com.org.testApi.models.Loan;
import com.org.testApi.payload.LoanPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour l'entité Loan et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface LoanMapper extends BaseMapper<Loan, LoanDTO> {

    @Mapping(target = "member", ignore = true)
    Loan toEntity(LoanDTO dto);

    @Mapping(target = "memberId", source = "member.id")
    LoanResponseDTO toResponseDto(Loan entity);

    @Mapping(target = "member", ignore = true)
    Loan toEntityFromRequest(LoanRequestDTO requestDTO);

    // Payload mappings
    @Mapping(target = "member", ignore = true)
    Loan toEntityFromPayload(LoanPayload payload);

    @Mapping(target = "memberId", source = "member.id")
    LoanPayload toPayload(Loan entity);

    @Mapping(target = "member", ignore = true)
    void updateEntityFromPayload(LoanPayload payload, @MappingTarget Loan entity);
}
