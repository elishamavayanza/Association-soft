package com.org.testApi.repository.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> domainClass;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.domainClass = entityInformation.getJavaType();
    }

    // Implémentation des méthodes CustomJpaRepository...
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

    @Override
    public List<T> findByAttributes(Map<String, Object> attributes) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(domainClass);
        Root<T> root = query.from(domainClass);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<T> findByAttributeContains(String attribute, String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(domainClass);
        Root<T> root = query.from(domainClass);

        Predicate condition = cb.like(cb.lower(root.get(attribute)), "%" + value.toLowerCase() + "%");
        query.where(condition);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public void softDelete(ID id) {
        T entity = findById(id).orElse(null);
        if (entity != null) {
            softDelete(entity);
        }
    }

    @Override
    @Transactional
    public void softDelete(T entity) {
        // Implémentation générique du soft delete
        try {
            // Essaye d'appeler setActive(false) si la méthode existe
            entity.getClass().getMethod("setActive", boolean.class).invoke(entity, false);
            save(entity);
        } catch (Exception e) {
            try {
                // Essaye d'appeler setDeleted(true) si la méthode existe
                entity.getClass().getMethod("setDeleted", boolean.class).invoke(entity, true);
                save(entity);
            } catch (Exception ex) {
                // Fallback: suppression physique
                entityManager.remove(entity);
            }
        }
        entityManager.flush();
    }
}