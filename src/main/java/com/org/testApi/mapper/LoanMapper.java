package com.org.testApi.mapper;

import com.org.testApi.dto.LoanDTO;
import com.org.testApi.dto.request.LoanRequestDTO;
import com.org.testApi.dto.response.LoanResponseDTO;
import com.org.testApi.models.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Loan et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface LoanMapper extends BaseMapper<Loan, LoanDTO> {

    @Mapping(target = "member", ignore = true)
    Loan toEntity(LoanDTO dto);

    @Mapping(target = "memberId", source = "member.id")
    LoanResponseDTO toResponseDto(Loan entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "member", ignore = true)
    Loan toEntityFromRequest(LoanRequestDTO requestDTO);
}