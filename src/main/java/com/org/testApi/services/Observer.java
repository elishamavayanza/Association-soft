package com.org.testApi.services;

/**
 * Interface pour les observateurs qui souhaitent être notifiés des changements dans les services.
 * @param <T> le type d'entité observée
 */
public interface Observer<T> {
    /**
     * Méthode appelée lorsqu'un événement se produit dans le service observé.
     * @param event le type d'événement
     * @param entity l'entité concernée par l'événement
     */
    void update(String event, T entity);
}
