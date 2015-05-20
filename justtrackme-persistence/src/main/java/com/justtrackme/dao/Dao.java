package com.justtrackme.dao;

import com.justtrackme.model.Paged;
import com.justtrackme.model.SearchRequest;

public interface Dao<T, I> {

    T getById(I id);

    void create(T entity);

    void update(T entity);

    void remove(T entity);

    Paged<T> search(SearchRequest request);
}
