package com.org.testApi.mapper;

import com.org.testApi.dto.MembershipFeeDTO;
import com.org.testApi.models.MembershipFee;
import com.org.testApi.payload.MembershipFeePayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour l'entité MembershipFee et son DTO.
 */
@Mapper(componentModel = "spring")
public interface MembershipFeeMapper extends BaseMapper<MembershipFee, MembershipFeeDTO> {

    @Mapping(target = "member", ignore = true)
    MembershipFee toEntity(MembershipFeeDTO dto);

    @Mapping(target = "memberId", source = "member.id")
    MembershipFeeDTO toDto(MembershipFee entity);

    // Payload mappings
    @Mapping(target = "member", ignore = true)
    MembershipFee toEntityFromPayload(MembershipFeePayload payload);

    @Mapping(target = "memberId", source = "member.id")
    MembershipFeePayload toPayload(MembershipFee entity);

    @Mapping(target = "member", ignore = true)
    void updateEntityFromPayload(MembershipFeePayload payload, @MappingTarget MembershipFee entity);
}
