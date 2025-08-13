package com.org.testApi.services;

import com.org.testApi.models.Document;
import java.util.List;
import java.util.Optional;

public interface DocumentService extends ObservableService<Document> {
    List<Document> getAllDocuments();
    Optional<Document> getDocumentById(Long id);
    Document saveDocument(Document document);
    Document updateDocument(Long id, Document document);
    void deleteDocument(Long id);
    void softDeleteDocument(Long id);
}
