package com.org.testApi.repository;
import com.org.testApi.models.User;
import com.org.testApi.repository.base.BaseRepository;
import com.org.testApi.repository.custom.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findWithRolesByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.lastLogin < :date")
    List<User> findInactiveUsersSince(LocalDateTime date);

    Page<User> findAllByOrderByLastNameAsc(Pageable pageable);

    boolean existsByUsernameOrEmail(String username, String email);

    @Query("SELECT u FROM User u JOIN u.attendedEvents a WHERE a.id = :activityId")
    List<User> findParticipantsByActivityId(Long activityId);
}
