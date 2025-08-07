package com.org.testApi.repository.base.observer;

import java.util.List;

public interface ObservableRepository<T> {
    void addObserver(RepositoryObserver<T> observer);
    void removeObserver(RepositoryObserver<T> observer);
    List<RepositoryObserver<T>> getObservers();
    void notifyObservers(RepositoryEvent<T> event);
}