package com.org.testApi.repository.custom;

import com.org.testApi.models.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Document> searchDocumentsComplexQuery(String name, String fileType, Long associationId) {
        StringBuilder jpql = new StringBuilder("SELECT d FROM Document d WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        if (fileType != null && !fileType.isEmpty()) {
            jpql.append(" AND d.fileType = :fileType");
        }
        if (associationId != null) {
            jpql.append(" AND d.association.id = :associationId");
        }

        jpql.append(" ORDER BY d.uploadDate DESC");

        TypedQuery<Document> query = entityManager.createQuery(jpql.toString(), Document.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (fileType != null && !fileType.isEmpty()) {
            query.setParameter("fileType", fileType);
        }
        if (associationId != null) {
            query.setParameter("associationId", associationId);
        }

        return query.getResultList();
    }

    @Override
    public List<Document> findRecentDocumentsByAssociation(Long associationId, int limit) {
        String jpql = "SELECT d FROM Document d WHERE d.association.id = :associationId ORDER BY d.uploadDate DESC";
        return entityManager.createQuery(jpql, Document.class)
                .setParameter("associationId", associationId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Long calculateTotalSizeForAssociation(Long associationId) {
        String jpql = "SELECT SUM(d.fileSize) FROM Document d WHERE d.association.id = :associationId";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("associationId", associationId)
                .getSingleResult();
    }
}
