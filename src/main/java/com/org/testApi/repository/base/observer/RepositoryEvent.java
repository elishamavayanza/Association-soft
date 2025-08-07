package com.org.testApi.repository.base.observer;

public class RepositoryEvent<T> {
    private final T entity;
    private final RepositoryEventType eventType;

    public RepositoryEvent(T entity, RepositoryEventType eventType) {
        this.entity = entity;
        this.eventType = eventType;
    }

    // Getters
    public T getEntity() {
        return entity;
    }

    public RepositoryEventType getEventType() {
        return eventType;
    }
}