package com.org.testApi.repository;

import com.org.testApi.models.Project;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Long> {

    List<Project> findByNameContainingIgnoreCase(String name);

    List<Project> findByStatus(Project.ProjectStatus status);

    List<Project> findByAssociationId(Long associationId);

    List<Project> findByManagerId(Long managerId);

    List<Project> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Project> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    Page<Project> findByAssociationId(Long associationId, Pageable pageable);

    boolean existsByNameAndAssociationId(String name, Long associationId);

    @Query("SELECT p FROM Project p WHERE p.startDate <= :date AND (p.endDate IS NULL OR p.endDate >= :date)")
    List<Project> findActiveProjectsAtDate(@Param("date") LocalDate date);

    @Query("SELECT p FROM Project p JOIN FETCH p.association WHERE p.id = :id")
    Optional<Project> findByIdWithAssociation(@Param("id") Long id);

    @Query("SELECT p FROM Project p WHERE p.status = 'IN_PROGRESS' AND p.association.id = :associationId")
    List<Project> findInProgressProjectsByAssociationId(@Param("associationId") Long associationId);
}
