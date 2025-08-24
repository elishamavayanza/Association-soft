package com.org.testApi.mapper;

import com.org.testApi.dto.UserDTO;
import com.org.testApi.dto.request.UserRequestDTO;
import com.org.testApi.dto.response.UserResponseDTO;
import com.org.testApi.models.User;
import com.org.testApi.payload.UserPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    UserPayload toPayload(User entity);

    @Mapping(target = "password", ignore = true)
    void updateEntityFromPayload(UserPayload payload, @MappingTarget User entity);
}
