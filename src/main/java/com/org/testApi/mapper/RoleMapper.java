package com.org.testApi.mapper;

import com.org.testApi.dto.RoleDTO;
import com.org.testApi.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Role et son DTO.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends BaseMapper<Role, RoleDTO> {

    @Mapping(target = "name", source = "name.name()")
    RoleDTO toDto(Role entity);

    @Mapping(target = "name", source = "name")
    Role toEntity(RoleDTO dto);
}
