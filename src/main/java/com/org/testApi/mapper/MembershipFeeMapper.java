package com.org.testApi.mapper;

import com.org.testApi.dto.MembershipFeeDTO;
import com.org.testApi.models.MembershipFee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité MembershipFee et son DTO.
 */
@Mapper(componentModel = "spring")
public interface MembershipFeeMapper extends BaseMapper<MembershipFee, MembershipFeeDTO> {

    @Mapping(target = "member", ignore = true)
    MembershipFee toEntity(MembershipFeeDTO dto);

    @Mapping(target = "memberId", source = "member.id")
    MembershipFeeDTO toDto(MembershipFee entity);
}
