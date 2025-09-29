package com.org.testApi.repository.custom;

import com.org.testApi.models.Activity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Activity> findActiveActivitiesWithParticipantsCount() {
        String jpql = """
            SELECT a FROM Activity a 
            WHERE a.status = 'ONGOING' OR a.status = 'PLANNED'
            ORDER BY a.startDateTime ASC
            """;
        return entityManager.createQuery(jpql, Activity.class).getResultList();
    }

    @Override
    public List<Activity> searchActivitiesComplexQuery(String title, String location, Activity.ActivityStatus status) {
        // Implémentation de requête dynamique
        StringBuilder jpql = new StringBuilder("SELECT a FROM Activity a WHERE 1=1");
        if (title != null && !title.isEmpty()) {
            jpql.append(" AND LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))");
        }
        if (location != null && !location.isEmpty()) {
            jpql.append(" AND LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%'))");
        }
        if (status != null) {
            jpql.append(" AND a.status = :status");
        }

        var query = entityManager.createQuery(jpql.toString(), Activity.class);
        if (title != null && !title.isEmpty()) {
            query.setParameter("title", title);
        }
        if (location != null && !location.isEmpty()) {
            query.setParameter("location", location);
        }
        if (status != null) {
            query.setParameter("status", status);
        }

        return query.getResultList();
    }

    @Override
    public void updateActivityStats(Long activityId) {
        // Implémentation de mise à jour custom
    }
    
    @Override
    public void softDeleteActivity(Activity activity) {
        activity.setDeleted(true);
        entityManager.merge(activity);
        entityManager.flush();
    }
}