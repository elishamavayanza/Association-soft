package com.org.testApi.repository;

import com.org.testApi.models.Penalty;
import com.org.testApi.models.Member;
import com.org.testApi.models.Round;
import com.org.testApi.models.PenaltyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    List<Penalty> findByMember(Member member);
    List<Penalty> findByRound(Round round);
    List<Penalty> findByStatus(String status);
    List<Penalty> findByPenaltyType(String penaltyType);
    
    // New methods for notification scheduling
    List<Penalty> findByPenaltyDateAfter(LocalDate penaltyDate);
    
    List<Penalty> findByStatus(PenaltyStatus status);
}