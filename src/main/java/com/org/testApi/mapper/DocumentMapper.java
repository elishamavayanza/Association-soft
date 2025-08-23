package com.org.testApi.mapper;

import com.org.testApi.dto.DocumentDTO;
import com.org.testApi.models.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'entité Document et son DTO.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends BaseMapper<Document, DocumentDTO> {

    @Mapping(target = "association", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    Document toEntity(DocumentDTO dto);

    @Mapping(target = "associationId", source = "association.id")
    @Mapping(target = "memberId", ignore = true) // À ajuster selon le modèle réel
    DocumentDTO toDto(Document entity);
}
