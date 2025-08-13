package com.org.testApi.repository;

import com.org.testApi.models.MembershipFee;
import com.org.testApi.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipFeeRepository extends BaseRepository<MembershipFee, Long> {

    List<MembershipFee> findByMemberId(Long memberId);

    List<MembershipFee> findByPaymentMethod(MembershipFee.PaymentMethod paymentMethod);

    List<MembershipFee> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    List<MembershipFee> findByAmountGreaterThanEqual(BigDecimal amount);

    List<MembershipFee> findByAmountLessThanEqual(BigDecimal amount);

    List<MembershipFee> findByTransactionId(Long transactionId);

    Page<MembershipFee> findByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT SUM(mf.amount) FROM MembershipFee mf WHERE mf.member.id = :memberId")
    BigDecimal sumAmountsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT mf FROM MembershipFee mf JOIN FETCH mf.member WHERE mf.id = :id")
    Optional<MembershipFee> findByIdWithMember(@Param("id") Long id);

    @Query("SELECT mf FROM MembershipFee mf WHERE mf.paymentDate >= :startDate AND mf.paymentDate <= :endDate")
    List<MembershipFee> findMembershipFeesInPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
