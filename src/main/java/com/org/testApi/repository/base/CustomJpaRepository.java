package com.org.testApi.repository.base;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID extends Serializable> {

    List<T> findByAttributes(Map<String, Object> attributes);
    List<T> findByAttributeContains(String attribute, String value);
    void softDelete(ID id);
    void softDelete(T entity);
    T refresh(T entity);
    void detach(T entity);
    <S extends T> S saveAndRefresh(S entity);
}