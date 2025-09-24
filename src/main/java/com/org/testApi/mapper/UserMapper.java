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
import org.springframework.beans.factory.annotation.Autowired;
import com.org.testApi.repository.RoleRepository;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper pour l'entité User et ses DTOs associés.
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper implements BaseMapper<User, UserDTO> {

    @Autowired
    protected RoleRepository roleRepository;

    @Mapping(target = "password", ignore = true)
    public abstract User toEntity(UserDTO dto);

    public abstract UserResponseDTO toResponseDto(User entity);

    @Mapping(target = "password", ignore = true)
    public abstract User toEntityFromRequest(UserRequestDTO requestDTO);

    // Payload mappings
    @Mapping(target = "password", ignore = true)
    public abstract User toEntityFromPayload(UserPayload payload);

    // Nouvelle méthode pour la création d'utilisateur qui inclut le mot de passe
    @Mapping(target = "password", source = "password")
    @Mapping(target = "roles", ignore = true)
    public abstract User toNewEntityFromPayload(UserPayload payload);

    public abstract UserPayload toPayload(User entity);

    @Mapping(target = "password", ignore = true)
    public abstract void updateEntityFromPayload(UserPayload payload, @MappingTarget User entity);

    public Set<Role> toRoleEntitySet(Set<RoleDTO> roleDTOs) {
        if (roleDTOs == null) {
            return null;
        }

        return roleDTOs.stream()
                .map(roleDTO -> {
                    if (roleDTO.getId() != null) {
                        return roleRepository.findById(roleDTO.getId()).orElse(null);
                    }
                    return null;
                })
                .filter(role -> role != null)
                .collect(Collectors.toSet());
    }

    // Méthode pour convertir un roleId en Role
    public Role toRoleEntity(Long roleId) {
        if (roleId == null) {
            return null;
        }
        // Convertir Long en Integer car le repository attend un Integer
        return roleRepository.findById(roleId.intValue()).orElse(null);
    }
}