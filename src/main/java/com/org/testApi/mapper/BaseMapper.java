package com.org.testApi.mapper;

import java.util.List;

/**
 * Interface de base pour tous les mappers.
 * @param <E> Type de l'entité
 * @param <D> Type du DTO
 */
public interface BaseMapper<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntityList(List<D> dtoList);

    List<D> toDtoList(List<E> entityList);
}
