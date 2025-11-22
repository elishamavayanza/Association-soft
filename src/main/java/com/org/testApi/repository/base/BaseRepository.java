package com.org.testApi.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, CustomJpaRepository<T, ID> {
    // Base repository extending both JpaRepository and our custom repository with soft delete functionality
}