package com.org.testApi.repository.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.Map;

public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

    @PersistenceContext
    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                              EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public T refresh(T entity) {
        entityManager.refresh(entity);
        return entity;
    }

    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }

    @Override
    @Transactional
    public <S extends T> S saveAndRefresh(S entity) {
        S savedEntity = save(entity);
        entityManager.flush();
        entityManager.refresh(savedEntity);
        return savedEntity;
    }
}