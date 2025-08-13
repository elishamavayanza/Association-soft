package com.org.testApi.repository.custom;

import com.org.testApi.models.Document;
import java.util.List;

public interface DocumentRepositoryCustom {

    /**
     * Recherche des documents avec des filtres complexes
     * @param name nom du document (recherche partielle)
     * @param fileType type de fichier
     * @param associationId ID de l'association
     * @return Liste des documents correspondant aux critères
     */
    List<Document> searchDocumentsComplexQuery(String name, String fileType, Long associationId);

    /**
     * Trouve les documents d'une association triés par date de téléchargement
     * @param associationId ID de l'association
     * @param limit nombre maximum de documents à retourner
     * @return Liste des documents récents
     */
    List<Document> findRecentDocumentsByAssociation(Long associationId, int limit);

    /**
     * Calcule la taille totale des documents pour une association
     * @param associationId ID de l'association
     * @return Taille totale en octets
     */
    Long calculateTotalSizeForAssociation(Long associationId);
}
