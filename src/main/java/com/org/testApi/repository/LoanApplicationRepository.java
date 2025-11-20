package com.org.testApi.repository;

import com.org.testApi.models.LoanApplication;
import com.org.testApi.models.Member;
import com.org.testApi.models.LoanApplication.LoanApplicationStatus;
import com.org.testApi.models.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    
    /**
     * Trouve toutes les demandes d'un membre.
     *
     * @param member le membre
     * @return la liste des demandes du membre
     */
    List<LoanApplication> findByMember(Member member);
    
    /**
     * Trouve toutes les demandes d'un type spécifique.
     *
     * @param loanType le type de prêt
     * @return la liste des demandes de ce type
     */
    List<LoanApplication> findByLoanType(LoanType loanType);
    
    /**
     * Trouve toutes les demandes par statut.
     *
     * @param status le statut des demandes
     * @return la liste des demandes avec ce statut
     */
    List<LoanApplication> findByStatus(LoanApplicationStatus status);
    
    /**
     * Trouve toutes les demandes d'un membre avec un statut spécifique.
     *
     * @param member le membre
     * @param status le statut des demandes
     * @return la liste des demandes du membre avec ce statut
     */
    List<LoanApplication> findByMemberAndStatus(Member member, LoanApplicationStatus status);
    
    /**
     * Compte le nombre de demandes en attente d'un membre.
     *
     * @param member le membre
     * @return le nombre de demandes en attente
     */
    @Query("SELECT COUNT(la) FROM LoanApplication la WHERE la.member = :member AND la.status = 'PENDING'")
    Long countPendingByMember(@Param("member") Member member);
}