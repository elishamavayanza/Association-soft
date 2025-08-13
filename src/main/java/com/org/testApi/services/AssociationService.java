package com.org.testApi.services;

import com.org.testApi.models.Association;
import java.util.List;
import java.util.Optional;

public interface AssociationService extends ObservableService<Association> {
    List<Association> getAllAssociations();
    Optional<Association> getAssociationById(Long id);
    Association saveAssociation(Association association);
    Association updateAssociation(Long id, Association association);
    void deleteAssociation(Long id);
    void softDeleteAssociation(Long id);
}
