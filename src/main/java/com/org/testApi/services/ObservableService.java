package com.org.testApi.services;

import java.util.List;

/**
 * Interface pour les services qui peuvent être observés.
 * @param <T> le type d'entité gérée par le service
 */
public interface ObservableService<T> {
    /**
     * Ajoute un observateur à la liste.
     * @param observer l'observateur à ajouter
     */
    void addObserver(Observer<T> observer);

    /**
     * Supprime un observateur de la liste.
     * @param observer l'observateur à supprimer
     */
    void removeObserver(Observer<T> observer);

    /**
     * Notifie tous les observateurs d'un événement.
     * @param event le type d'événement
     * @param entity l'entité concernée par l'événement
     */
    void notifyObservers(String event, T entity);
}
