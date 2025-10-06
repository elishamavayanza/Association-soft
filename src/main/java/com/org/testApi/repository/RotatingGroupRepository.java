package com.org.testApi.repository;

import com.org.testApi.models.RotatingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RotatingGroupRepository extends JpaRepository<RotatingGroup, Long> {
    List<RotatingGroup> findByStatus(String status);
    List<RotatingGroup> findByNameContainingIgnoreCase(String name);
}