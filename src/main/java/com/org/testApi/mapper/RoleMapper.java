package com.org.testApi.mapper;

import com.org.testApi.dto.RoleDTO;
import com.org.testApi.models.Role;
import com.org.testApi.payload.RolePayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

/**
 * Mapper pour l'entité Role et son DTO.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends BaseMapper<Role, RoleDTO> {

    // Correction: Utiliser directement "name" au lieu de "name.name()"
    @Mapping(target = "name", source = "name")
    RoleDTO toDto(Role entity);

    @Mapping(target = "name", source = "name")
    Role toEntity(RoleDTO dto);

    // Payload mappings
    @Mapping(target = "name", source = "name", qualifiedByName = "stringToERole")
    Role toEntityFromPayload(RolePayload payload);

    @Mapping(target = "name", source = "name", qualifiedByName = "eRoleToString")
    RolePayload toPayload(Role entity);

    void updateEntityFromPayload(RolePayload payload, @MappingTarget Role entity);

    @Named("stringToERole")
    default Role.ERole stringToERole(String name) {
        if (name == null) {
            return null;
        }
        try {
            return Role.ERole.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Named("eRoleToString")
    default String eRoleToString(Role.ERole name) {
        return name != null ? name.name() : null;
    }
}