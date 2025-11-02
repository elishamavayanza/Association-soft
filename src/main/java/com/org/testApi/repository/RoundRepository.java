package com.org.testApi.repository;

import com.org.testApi.models.Round;
import com.org.testApi.models.RotatingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findByRotatingGroup(RotatingGroup rotatingGroup);
    
    @Query("SELECT r FROM Round r LEFT JOIN FETCH r.rotatingGroup rg LEFT JOIN FETCH rg.members WHERE r.id = :id")
    Optional<Round> findWithRotatingGroupAndMembersById(@Param("id") Long id);
}