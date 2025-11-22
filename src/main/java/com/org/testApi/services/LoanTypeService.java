package com.org.testApi.services;

import com.org.testApi.models.LoanType;
import java.util.List;
import java.util.Optional;

public interface LoanTypeService {
    
    /**
     * Crée un nouveau type de prêt.
     *
     * @param loanType le type de prêt à créer
     * @return le type de prêt créé
     */
    LoanType createLoanType(LoanType loanType);
    
    /**
     * Met à jour un type de prêt existant.
     *
     * @param id l'identifiant du type de prêt
     * @param loanType le type de prêt mis à jour
     * @return le type de prêt mis à jour
     */
    LoanType updateLoanType(Long id, LoanType loanType);
    
    /**
     * Trouve un type de prêt par son identifiant.
     *
     * @param id l'identifiant du type de prêt
     * @return le type de prêt s'il existe
     */
    Optional<LoanType> findLoanTypeById(Long id);
    
    /**
     * Trouve un type de prêt par son nom.
     *
     * @param name le nom du type de prêt
     * @return le type de prêt s'il existe
     */
    Optional<LoanType> findLoanTypeByName(String name);
    
    /**
     * Trouve tous les types de prêt.
     *
     * @return la liste de tous les types de prêt
     */
    List<LoanType> findAllLoanTypes();
    
    /**
     * Trouve tous les types de prêt actifs.
     *
     * @return la liste des types de prêt actifs
     */
    List<LoanType> findActiveLoanTypes();
    
    /**
     * Supprime un type de prêt.
     *
     * @param id l'identifiant du type de prêt à supprimer
     */
    void deleteLoanType(Long id);
}