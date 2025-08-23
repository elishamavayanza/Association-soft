package com.org.testApi.mapper;

import com.org.testApi.dto.UserDTO;
import com.org.testApi.dto.request.UserRequestDTO;
import com.org.testApi.dto.response.UserResponseDTO;
import com.org.testApi.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
}
