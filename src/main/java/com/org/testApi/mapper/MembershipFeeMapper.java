package com.org.testApi.mapper;

import com.org.testApi.dto.MembershipFeeDTO;
import com.org.testApi.models.MembershipFee;
import com.org.testApi.payload.MembershipFeePayload;
import com.org.testApi.models.Member;
import com.org.testApi.repository.MemberRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mapper pour l'entité MembershipFee et son DTO.
 */
@Mapper(componentModel = "spring")
public abstract class MembershipFeeMapper implements BaseMapper<MembershipFee, MembershipFeeDTO> {
    
    private static final Logger logger = LoggerFactory.getLogger(MembershipFeeMapper.class);

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
        logger.info("Converting payload to entity with member: {}", payload);
        if (payload == null) {
            logger.warn("Payload is null");
            return null;
        }

        MembershipFee membershipFee = toEntityFromPayload(payload);
        logger.info("Entity after initial mapping: {}", membershipFee);
        
        if (payload.getMemberId() != null) {
            logger.info("Looking up member with id: {}", payload.getMemberId());
            Member member = memberRepository.findById(payload.getMemberId()).orElse(null);
            membershipFee.setMember(member);
            logger.info("Set member: {}", member);
        }

        logger.info("Final entity: {}", membershipFee);
        return membershipFee;
    }

    /**
     * Updates an entity from a payload and sets the member based on memberId from the payload
     * Preserves existing member and required fields if not provided in payload
     */
    public void updateEntityWithMemberFromPayload(MembershipFeePayload payload, @MappingTarget MembershipFee entity) {
        logger.info("Updating entity from payload - entity: {}, payload: {}", entity, payload);
        if (payload == null || entity == null) {
            logger.warn("Payload or entity is null - payload: {}, entity: {}", payload, entity);
            return;
        }

        // Preserve existing values for required fields
        BigDecimal existingAmount = entity.getAmount();
        LocalDate existingPaymentDate = entity.getPaymentDate();
        Member existingMember = entity.getMember();
        
        logger.info("Existing values - amount: {}, paymentDate: {}, member: {}", existingAmount, existingPaymentDate, existingMember);
        
        // Update entity with payload values
        updateEntityFromPayload(payload, entity);
        logger.info("Entity after payload update: {}", entity);
        
        // Restore required fields if not provided in payload
        if (payload.getAmount() == null) {
            entity.setAmount(existingAmount);
            logger.info("Restored amount: {}", existingAmount);
        }
        if (payload.getPaymentDate() == null) {
            entity.setPaymentDate(existingPaymentDate);
            logger.info("Restored paymentDate: {}", existingPaymentDate);
        }
        
        // Handle member update
        if (payload.getMemberId() != null) {
            logger.info("Looking up member with id: {}", payload.getMemberId());
            Member member = memberRepository.findById(payload.getMemberId()).orElse(null);
            entity.setMember(member);
            logger.info("Set member: {}", member);
        } else {
            // Restore existing member if not provided in payload
            entity.setMember(existingMember);
            logger.info("Restored member: {}", existingMember);
        }
        
        logger.info("Final updated entity: {}", entity);
    }
}