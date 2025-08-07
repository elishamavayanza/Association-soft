package com.org.testApi.repository.custom;

import com.org.testApi.models.Association;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssociationRepositoryImpl implements AssociationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Association> findActiveAssociationsWithMembersCount() {
        String jpql = """
            SELECT a FROM Association a 
            WHERE a.active = true 
            AND (SELECT COUNT(m) FROM Member m WHERE m.association = a AND m.isActive()) > 0
            """;
        return entityManager.createQuery(jpql, Association.class).getResultList();
    }

    @Override
    public List<Association> searchAssociationsComplexQuery(String name, String location, String status) {
        // Implémentation de requête dynamique
        return null;
    }

    @Override
    public void updateAssociationStats(Long associationId) {
        // Implémentation de mise à jour custom
    }
}