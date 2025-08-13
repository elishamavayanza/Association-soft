package com.org.testApi.repository;

import com.org.testApi.models.Association;
import com.org.testApi.repository.base.BaseRepository;
import com.org.testApi.repository.custom.AssociationRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssociationRepository extends BaseRepository<Association, Long>, AssociationRepositoryCustom {

    Optional<Association> findByName(String name);

    List<Association> findByLocationContaining(String location);

    @Query("SELECT a FROM Association a WHERE a.legalStatus = :status")
    List<Association> findByLegalStatus(@Param("status") String status);

    Page<Association> findAll(Pageable pageable);

    boolean existsBySiret(String siret);

    @Query("SELECT a FROM Association a JOIN FETCH a.members WHERE a.id = :id")
    Optional<Association> findByIdWithMembers(@Param("id") Long id);
}
