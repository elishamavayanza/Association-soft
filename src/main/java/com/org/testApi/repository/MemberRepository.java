package com.org.testApi.repository;

import com.org.testApi.models.Member;
import com.org.testApi.repository.base.BaseRepository;
import com.org.testApi.repository.custom.MemberRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends BaseRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUserId(Long userId);

    List<Member> findByAssociationId(Long associationId);

    List<Member> findByType(Member.MemberType type);

    List<Member> findByIsActiveTrue();

    List<Member> findByJoinDateBetween(LocalDate startDate, LocalDate endDate);

    List<Member> findByLeaveDateIsNull();

    List<Member> findByLeaveDateIsNotNull();

    Page<Member> findByAssociationId(Long associationId, Pageable pageable);

    boolean existsByUserIdAndAssociationId(Long userId, Long associationId);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.association.id = :associationId AND m.leaveDate IS NULL")
    long countActiveMembersByAssociationId(@Param("associationId") Long associationId);

    @Query("SELECT m FROM Member m JOIN FETCH m.user JOIN FETCH m.association WHERE m.id = :id")
    Optional<Member> findByIdWithUserAndAssociation(@Param("id") Long id);

    @Query("SELECT m FROM Member m WHERE m.association.id = :associationId AND m.leaveDate IS NULL")
    List<Member> findActiveMembersByAssociationId(@Param("associationId") Long associationId);

    /**
     * Trouve un membre par ID avec ses prêts.
     *
     * @param id l'identifiant du membre
     * @return le membre avec ses prêts
     */
    @EntityGraph(attributePaths = {"loans"})
    Optional<Member> findWithLoansById(Long id);

    // Méthode ajoutée pour le ReportService
    long countByIsActiveTrue();
}
