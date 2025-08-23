package com.org.testApi.mapper;

import com.org.testApi.dto.MemberRoleHistoryDTO;
import com.org.testApi.models.MemberRoleHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité MemberRoleHistory et son DTO.
 */
@Mapper(componentModel = "spring")
public interface MemberRoleHistoryMapper extends BaseMapper<MemberRoleHistory, MemberRoleHistoryDTO> {

    @Mapping(target = "member", ignore = true)
    @Mapping(target = "role", ignore = true)
    MemberRoleHistory toEntity(MemberRoleHistoryDTO dto);

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "roleId", source = "role.id")
    MemberRoleHistoryDTO toDto(MemberRoleHistory entity);
}
