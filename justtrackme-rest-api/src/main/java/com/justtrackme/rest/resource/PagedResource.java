package com.justtrackme.rest.resource;

import java.util.Collection;

public class PagedResource<T extends Resource> {

	private int offset;
	private int limit;
	private int total;

	private Collection<T> entities;

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
}
