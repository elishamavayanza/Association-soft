package com.org.testApi.mapper;

import com.org.testApi.models.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for handling photo-related mappings.
 */
@Mapper
public interface MemberPhotoMapper {

    @Mapping(source = "user.photo", target = ".")
    String mapPhoto(Member member);
}