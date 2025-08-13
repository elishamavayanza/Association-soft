package com.org.testApi.services;

import com.org.testApi.models.Document;
import com.org.testApi.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    private List<Observer<Document>> observers = new ArrayList<>();

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public Document saveDocument(Document document) {
        Document savedDocument = documentRepository.save(document);
        notifyObservers("SAVE", savedDocument);
        return savedDocument;
    }

    @Override
    public Document updateDocument(Long id, Document document) {
        if (documentRepository.existsById(id)) {
            document.setId(id);
            Document updatedDocument = documentRepository.save(document);
            notifyObservers("UPDATE", updatedDocument);
            return updatedDocument;
        }
        throw new RuntimeException("Document not found with id: " + id);
    }

    @Override
    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id).orElse(null);
        documentRepository.deleteById(id);
        if (document != null) {
            notifyObservers("DELETE", document);
        }
    }

    @Override
    public void softDeleteDocument(Long id) {
        Document document = documentRepository.findById(id).orElse(null);
        documentRepository.softDelete(id);
        if (document != null) {
            notifyObservers("SOFT_DELETE", document);
        }
    }

    @Override
    public void addObserver(Observer<Document> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Document> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, Document entity) {
        for (Observer<Document> observer : observers) {
            observer.update(event, entity);
        }
    }
}
