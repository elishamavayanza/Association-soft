package com.org.testApi.mapper;

import com.org.testApi.dto.MemberDTO;
import com.org.testApi.dto.request.MemberRequestDTO;
import com.org.testApi.dto.response.MemberResponseDTO;
import com.org.testApi.models.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Member et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper extends BaseMapper<Member, MemberDTO> {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "fees", ignore = true)
    @Mapping(target = "roleHistory", ignore = true)
    @Mapping(target = "loans", ignore = true)
    Member toEntity(MemberDTO dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "associationId", source = "association.id")
    MemberResponseDTO toResponseDto(Member entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "fees", ignore = true)
    @Mapping(target = "roleHistory", ignore = true)
    @Mapping(target = "loans", ignore = true)
    Member toEntityFromRequest(MemberRequestDTO requestDTO);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "association", ignore = true)
    @Mapping(target = "fees", ignore = true)
    @Mapping(target = "roleHistory", ignore = true)
    @Mapping(target = "loans", ignore = true)
    Member toEntityFromResponse(MemberResponseDTO responseDTO);
}
