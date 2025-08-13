package com.org.testApi.repository;

import com.org.testApi.models.Role;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role, Integer> {

    Optional<Role> findByName(Role.ERole name);

    boolean existsByName(Role.ERole name);
}
