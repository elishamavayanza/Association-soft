package com.org.testApi.repository;

import com.org.testApi.models.ProjectMember;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends BaseRepository<ProjectMember, Long> {

    List<ProjectMember> findByProjectId(Long projectId);

    List<ProjectMember> findByMemberId(Long memberId);

    List<ProjectMember> findByRoleInProjectContainingIgnoreCase(String roleInProject);

    List<ProjectMember> findByJoinDateBetween(LocalDate startDate, LocalDate endDate);

    List<ProjectMember> findByLeaveDateIsNull();

    List<ProjectMember> findByLeaveDateIsNotNull();

    boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project.id = :projectId AND pm.leaveDate IS NULL")
    List<ProjectMember> findActiveMembersByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT pm FROM ProjectMember pm JOIN FETCH pm.project JOIN FETCH pm.member WHERE pm.id = :id")
    Optional<ProjectMember> findByIdWithProjectAndMember(@Param("id") Long id);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.member.id = :memberId AND pm.joinDate <= :date AND (pm.leaveDate IS NULL OR pm.leaveDate >= :date)")
    List<ProjectMember> findMemberProjectsAtDate(@Param("memberId") Long memberId, @Param("date") LocalDate date);
}
