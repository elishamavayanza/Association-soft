package com.org.testApi.repository;

import com.org.testApi.models.Round;
import com.org.testApi.models.RotatingGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findByRotatingGroup(RotatingGroup rotatingGroup);
    List<Round> findByStatus(String status);
    
    @EntityGraph(attributePaths = {"rotatingGroup", "rotatingGroup.members"})
    Optional<Round> findWithRotatingGroupAndMembersById(Long id);
}