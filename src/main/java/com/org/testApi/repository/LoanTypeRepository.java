package com.org.testApi.repository;

import com.org.testApi.models.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
    
    /**
     * Trouve un type de prêt par son nom.
     *
     * @param name le nom du type de prêt
     * @return le type de prêt trouvé
     */
    Optional<LoanType> findByName(String name);
    
    /**
     * Trouve tous les types de prêt actifs.
     *
     * @return la liste des types de prêt actifs
     */
    List<LoanType> findByActiveTrue();
    
    /**
     * Trouve tous les types de prêt par catégorie.
     *
     * @param category la catégorie des types de prêt
     * @return la liste des types de prêt de cette catégorie
     */
    List<LoanType> findByCategory(String category);
}