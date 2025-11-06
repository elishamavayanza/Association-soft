package com.org.testApi.repository;

import com.org.testApi.models.Contribution;
import com.org.testApi.models.ContributionStatus;
import com.org.testApi.models.Member;
import com.org.testApi.models.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
    List<Contribution> findByMember(Member member);
    List<Contribution> findByRound(Round round);
    List<Contribution> findByStatus(String status);
    List<Contribution> findByMemberAndRound(Member member, Round round);
    
    // New methods for notification scheduling
    List<Contribution> findByContributionDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, ContributionStatus status);
}