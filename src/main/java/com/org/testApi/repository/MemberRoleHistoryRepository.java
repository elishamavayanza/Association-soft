package com.org.testApi.repository;

import com.org.testApi.models.MemberRoleHistory;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRoleHistoryRepository extends BaseRepository<MemberRoleHistory, Long> {

    List<MemberRoleHistory> findByMemberId(Long memberId);

    List<MemberRoleHistory> findByRoleContainingIgnoreCase(String role);

    List<MemberRoleHistory> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<MemberRoleHistory> findByEndDateIsNull();

    List<MemberRoleHistory> findByEndDateIsNotNull();

    @Query("SELECT mrh FROM MemberRoleHistory mrh WHERE mrh.member.id = :memberId AND mrh.startDate <= :date AND (mrh.endDate IS NULL OR mrh.endDate >= :date)")
    List<MemberRoleHistory> findMemberRolesAtDate(@Param("memberId") Long memberId, @Param("date") LocalDate date);

    @Query("SELECT mrh FROM MemberRoleHistory mrh JOIN FETCH mrh.member WHERE mrh.id = :id")
    Optional<MemberRoleHistory> findByIdWithMember(@Param("id") Long id);
}
