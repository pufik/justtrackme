package com.justtrackme.dao.position;

import com.justtrackme.dao.Dao;
import com.justtrackme.model.Device;
import com.justtrackme.model.Paged;
import com.justtrackme.model.Position;
import com.justtrackme.model.SearchRequest;

public interface PositionDao extends Dao<Position, Long> {

	Position getLastPositionForDevice(Device device);

	Paged<Position> getPositionsForDevice(SearchRequest request, Device device);

}
