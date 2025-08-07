package com.org.testApi.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, CustomJpaRepository<T, ID> {

    List<T> findByAttributes(Map<String, Object> attributes);

    List<T> findByAttributeContains(String attribute, String value);

    void softDelete(ID id);

    void softDelete(T entity);
}