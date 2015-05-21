package com.justtrackme.rest.resource;

import java.util.Date;

public class DeviceResource extends Resource {
	
	public static final String DEVICES_CONTEXT = "/devices";
	private String uniqueId;
	private String name;
	private String status;
	private Date lastUpdate;
	private long positionId;
	private long dataId;

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

	@Override
	public String getUri() {
		return DEVICES_CONTEXT + SLASH + getUniqueId();
	}

}
