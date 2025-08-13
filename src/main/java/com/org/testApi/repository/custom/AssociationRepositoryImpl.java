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
        AND (SELECT COUNT(m) 
             FROM Member m 
             WHERE m.association = a AND m.active = true) > 0
        """;
        return entityManager.createQuery(jpql, Association.class).getResultList();
    }

    @Override
    public List<Association> searchAssociationsComplexQuery(String name, String location, String status) {
        // Implémentation de requête dynamique
        StringBuilder jpql = new StringBuilder("SELECT a FROM Association a WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        if (location != null && !location.isEmpty()) {
            jpql.append(" AND LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%'))");
        }
        // Note: Association n'a pas de champ status comme Activity, donc ce paramètre est ignoré
        // Si vous ajoutez un champ status à l'entité Association, vous pouvez l'utiliser ici

        var query = entityManager.createQuery(jpql.toString(), Association.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (location != null && !location.isEmpty()) {
            query.setParameter("location", location);
        }

        return query.getResultList();
    }

    @Override
    public void updateAssociationStats(Long associationId) {
        // Implémentation de mise à jour custom
        // Cette méthode pourrait être utilisée pour mettre à jour des statistiques comme
        // le nombre de membres, le nombre d'activités, etc.
    }
}
