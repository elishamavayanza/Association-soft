package com.org.testApi.repository;

import com.org.testApi.models.Penalty;
import com.org.testApi.models.Member;
import com.org.testApi.models.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    List<Penalty> findByMember(Member member);
    List<Penalty> findByRound(Round round);
    List<Penalty> findByStatus(String status);
    List<Penalty> findByPenaltyType(String penaltyType);
}