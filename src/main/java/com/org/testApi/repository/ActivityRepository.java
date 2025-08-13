package com.org.testApi.repository;

import com.org.testApi.models.Activity;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Long> {

    List<Activity> findByTitleContainingIgnoreCase(String title);

    List<Activity> findByType(Activity.ActivityType type);

    List<Activity> findByStatus(Activity.ActivityStatus status);

    List<Activity> findByAssociationId(Long associationId);

    List<Activity> findByProjectId(Long projectId);

    @Query("SELECT a FROM Activity a WHERE a.startDateTime >= :start AND a.startDateTime <= :end")
    List<Activity> findActivitiesInDateRange(LocalDateTime start, LocalDateTime end);

    Page<Activity> findByAssociationId(Long associationId, Pageable pageable);

    boolean existsByTitleAndAssociationId(String title, Long associationId);

    @Query("SELECT a FROM Activity a JOIN FETCH a.association WHERE a.id = :id")
    Optional<Activity> findByIdWithAssociation(Long id);
}
