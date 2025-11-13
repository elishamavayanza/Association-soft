package com.org.testApi.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;

import jakarta.persistence.EntityManager;
import java.io.Serializable;

public class CustomRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
        extends JpaRepositoryFactoryBean<T, S, ID> {

    public CustomRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    // Use default implementation - custom repository base class is configured via @EnableJpaRepositories
}