package com.org.testApi.mapper;

import com.org.testApi.dto.MembershipFeeDTO;
import com.org.testApi.models.MembershipFee;
import com.org.testApi.payload.MembershipFeePayload;
import com.org.testApi.models.Member;
import com.org.testApi.repository.MemberRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper pour l'entité MembershipFee et son DTO.
 */
@Mapper(componentModel = "spring")
public abstract class MembershipFeeMapper implements BaseMapper<MembershipFee, MembershipFeeDTO> {

    @Autowired
    protected MemberRepository memberRepository;

    @Mapping(target = "member", ignore = true)
    public abstract MembershipFee toEntity(MembershipFeeDTO dto);

    @Mapping(target = "memberId", source = "member.id")
    public abstract MembershipFeeDTO toDto(MembershipFee entity);

    // Payload mappings
    @Mapping(target = "member", ignore = true)
    public abstract MembershipFee toEntityFromPayload(MembershipFeePayload payload);

    @Mapping(target = "memberId", source = "member.id")
    public abstract MembershipFeePayload toPayload(MembershipFee entity);

    @Mapping(target = "member", ignore = true)
    public abstract void updateEntityFromPayload(MembershipFeePayload payload, @MappingTarget MembershipFee entity);

    /**
     * Converts a payload to an entity and sets the member based on memberId from the payload
     */
    public MembershipFee toEntityWithMemberFromPayload(MembershipFeePayload payload) {
        if (payload == null) {
            return null;
        }

        MembershipFee membershipFee = toEntityFromPayload(payload);
        if (payload.getMemberId() != null) {
            Member member = memberRepository.findById(payload.getMemberId()).orElse(null);
            membershipFee.setMember(member);
        }

        return membershipFee;
    }

    /**
     * Updates an entity from a payload and sets the member based on memberId from the payload
     */
    public void updateEntityWithMemberFromPayload(MembershipFeePayload payload, @MappingTarget MembershipFee entity) {
        if (payload == null || entity == null) {
            return;
        }

        updateEntityFromPayload(payload, entity);
        if (payload.getMemberId() != null) {
            Member member = memberRepository.findById(payload.getMemberId()).orElse(null);
            entity.setMember(member);
        }
    }
}