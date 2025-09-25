package com.org.testApi.services;

import com.org.testApi.models.Association;
import com.org.testApi.repository.AssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class AssociationServiceImpl implements AssociationService {

    @Autowired
    private AssociationRepository associationRepository;

    private List<Observer<Association>> observers = new ArrayList<>();

    @Override
    public List<Association> getAllAssociations() {
        return associationRepository.findAll();
    }

    @Override
    public Optional<Association> getAssociationById(Long id) {
        try {
            // Make sure to properly fetch all related entities if needed
            return associationRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching association with id: " + id, e);
        }
    }
    @Override
    public Association saveAssociation(Association association) {
        Association savedAssociation = associationRepository.save(association);
        notifyObservers("SAVE", savedAssociation);
        return savedAssociation;
    }

    @Override
    public Association updateAssociation(Long id, Association association) {
        if (associationRepository.existsById(id)) {
            association.setId(id);
            Association updatedAssociation = associationRepository.save(association);
            notifyObservers("UPDATE", updatedAssociation);
            return updatedAssociation;
        }
        throw new RuntimeException("Association not found with id: " + id);
    }

    @Override
    public void deleteAssociation(Long id) {
        Association association = associationRepository.findById(id).orElse(null);
        associationRepository.deleteById(id);
        if (association != null) {
            notifyObservers("DELETE", association);
        }
    }

    @Override
    public void softDeleteAssociation(Long id) {
        Association association = associationRepository.findById(id).orElse(null);
        associationRepository.softDelete(id);
        if (association != null) {
            notifyObservers("SOFT_DELETE", association);
        }
    }

    @Override
    public void addObserver(Observer<Association> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Association> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, Association entity) {
        for (Observer<Association> observer : observers) {
            observer.update(event, entity);
        }
    }
}
