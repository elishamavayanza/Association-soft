package com.org.testApi.mapper;

import com.org.testApi.dto.MemberRoleHistoryDTO;
import com.org.testApi.models.MemberRoleHistory;
import com.org.testApi.payload.MemberRoleHistoryPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour l'entité MemberRoleHistory et son DTO.
 */
@Mapper(componentModel = "spring")
public interface MemberRoleHistoryMapper extends BaseMapper<MemberRoleHistory, MemberRoleHistoryDTO> {

    @Mapping(target = "member", ignore = true)
    @Mapping(target = "role", ignore = true)
    MemberRoleHistory toEntity(MemberRoleHistoryDTO dto);

    @Mapping(target = "memberId", source = "member.id")
        // Correction: role est une String dans le modèle, pas une entité
    MemberRoleHistoryDTO toDto(MemberRoleHistory entity);

    // Payload mappings
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "role", ignore = true)
    MemberRoleHistory toEntityFromPayload(MemberRoleHistoryPayload payload);

    @Mapping(target = "memberId", source = "member.id")
    MemberRoleHistoryPayload toPayload(MemberRoleHistory entity);

    @Mapping(target = "member", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromPayload(MemberRoleHistoryPayload payload, @MappingTarget MemberRoleHistory entity);
}
