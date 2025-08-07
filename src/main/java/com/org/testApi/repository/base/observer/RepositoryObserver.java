package com.org.testApi.repository.base.observer;

public interface RepositoryObserver<T> {
    void onRepositoryEvent(RepositoryEvent<T> event);
}
