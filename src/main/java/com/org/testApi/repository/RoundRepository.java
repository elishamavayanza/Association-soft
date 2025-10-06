package com.org.testApi.repository;

import com.org.testApi.models.Round;
import com.org.testApi.models.RotatingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findByRotatingGroup(RotatingGroup rotatingGroup);
    List<Round> findByStatus(String status);
}