package com.org.testApi.repository.custom;

import com.org.testApi.models.Association;
import java.util.List;

public interface AssociationRepositoryCustom {
    List<Association> findActiveAssociationsWithMembersCount();

    List<Association> searchAssociationsComplexQuery(String name, String location, String status);

    void updateAssociationStats(Long associationId);
}
