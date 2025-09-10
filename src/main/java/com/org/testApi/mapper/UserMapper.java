package com.org.testApi.mapper;

import com.org.testApi.dto.UserDTO;
import com.org.testApi.dto.request.UserRequestDTO;
import com.org.testApi.dto.response.UserResponseDTO;
import com.org.testApi.models.User;
import com.org.testApi.payload.UserPayload;
import com.org.testApi.models.Role;
import com.org.testApi.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

/**
 * Mapper pour l'entité User et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO> {

    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO dto);

    UserResponseDTO toResponseDto(User entity);

    @Mapping(target = "password", ignore = true)
    User toEntityFromRequest(UserRequestDTO requestDTO);

    // Payload mappings
    @Mapping(target = "password", ignore = true)
    User toEntityFromPayload(UserPayload payload);

    // Nouvelle méthode pour la création d'utilisateur qui inclut le mot de passe
    User toNewEntityFromPayload(UserPayload payload);

    UserPayload toPayload(User entity);

    @Mapping(target = "password", ignore = true)
    void updateEntityFromPayload(UserPayload payload, @MappingTarget User entity);

    Set<Role> toRoleEntitySet(Set<RoleDTO> roleDTOs);
}
