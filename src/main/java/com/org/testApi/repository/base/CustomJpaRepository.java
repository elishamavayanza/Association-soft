package com.org.testApi.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID extends Serializable> {
    // Méthodes custom qui seront implémentées dans BaseRepositoryImpl
    T refresh(T entity);

    void detach(T entity);

    <S extends T> S saveAndRefresh(S entity);
}
