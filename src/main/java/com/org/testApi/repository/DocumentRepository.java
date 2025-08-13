package com.org.testApi.repository;

import com.org.testApi.models.Document;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends BaseRepository<Document, Long> {

    List<Document> findByNameContainingIgnoreCase(String name);

    List<Document> findByFileType(String fileType);

    List<Document> findByAssociationId(Long associationId);

    List<Document> findByUploadedById(Long userId);

    Page<Document> findByAssociationId(Long associationId, Pageable pageable);

    boolean existsByNameAndAssociationId(String name, Long associationId);

    @Query("SELECT d FROM Document d WHERE d.fileSize > :minSize")
    List<Document> findDocumentsLargerThan(Long minSize);

    @Query("SELECT d FROM Document d JOIN FETCH d.association WHERE d.id = :id")
    Optional<Document> findByIdWithAssociation(Long id);
}
