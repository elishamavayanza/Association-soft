package com.org.testApi.repository;

import com.org.testApi.models.Round;
import com.org.testApi.models.RotatingGroup;
import com.org.testApi.models.RoundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findByRotatingGroup(RotatingGroup rotatingGroup);
    
    List<Round> findByRotatingGroupOrderByRoundNumberDesc(RotatingGroup rotatingGroup);
    
    List<Round> findByRotatingGroupOrderByLastModifiedDateDesc(RotatingGroup rotatingGroup);
    
    @Query("SELECT r FROM Round r LEFT JOIN FETCH r.rotatingGroup rg LEFT JOIN FETCH rg.members WHERE r.id = :id")
    Optional<Round> findWithRotatingGroupAndMembersById(@Param("id") Long id);
    
    // New methods for notification scheduling
    List<Round> findByStartDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, RoundStatus status);
    
    List<Round> findByDistributionDateAfter(LocalDateTime distributionDate);
}