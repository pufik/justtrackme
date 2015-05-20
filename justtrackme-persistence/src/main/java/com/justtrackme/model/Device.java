package com.justtrackme.model;

import java.util.Date;

public class Device {

	private long id;
	private String uniqueId;
	private String name;
	private String status;
	private Date lastUpdate;
	private long positionId;
	private long dataId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public long getPositionId() {
		return positionId;
	}

	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}

	public long getDataId() {
		return dataId;
	}

	public void setDataId(long dataId) {
		this.dataId = dataId;
	}
}
