package com.justtrackme.model;

import java.util.Collection;

/**
 * Created by iurii on 07.05.15.
 */
public class Paged<T> {

    private int total;
    private int offset;
    private int limit;

    private Collection<T> entities;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Collection<T> getEntities() {
        return entities;
    }

    public void setEntities(Collection<T> entities) {
        this.entities = entities;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
