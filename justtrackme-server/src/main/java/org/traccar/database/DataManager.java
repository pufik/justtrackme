/*
 * Copyright 2012 - 2015 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.model.Device;
import org.traccar.model.Permission;
import org.traccar.model.Position;

import com.justtrackme.dao.device.DeviceDao;
import com.justtrackme.dao.position.PositionDao;

//TODO: Should be removed, temporary glue
public class DataManager {

	private static final Logger LOG = LoggerFactory.getLogger(DataManager.class);

	private DataSource dataSource;

	private DeviceDao deviceDao;

	private PositionDao positionDao;

	public DataSource getDataSource() {
		return dataSource;
	}

	public Device getDeviceByUniqueId(String uniqueId) throws SQLException {
		Device trackDevice = null;

		com.justtrackme.model.Device device = deviceDao.getById(uniqueId);

		if (Objects.nonNull(device)) {
			trackDevice = new Device();
			trackDevice.setId(device.getId());
			trackDevice.setUniqueId(device.getUniqueId());
			trackDevice.setName(device.getName());
		}

		return trackDevice;
	}

	public void addPosition(Position trackedPosition) throws SQLException {
		LOG.info("Process position for device [{}] -> [lat:{};lon:{};alt:{}]", trackedPosition.getDeviceId(), trackedPosition.getLatitude(),
				trackedPosition.getLongitude(), trackedPosition.getAltitude());

		com.justtrackme.model.Position position = new com.justtrackme.model.Position();

		position.setDeviceId(trackedPosition.getDeviceId());
		position.setProtocol(trackedPosition.getProtocol());
		position.setServerTime(trackedPosition.getServerTime());
		position.setDeviceTime(trackedPosition.getDeviceTime());
		position.setFixTime(trackedPosition.getFixTime());
		position.setValid(trackedPosition.getValid());
		position.setLatitude(trackedPosition.getLatitude());
		position.setLongitude(trackedPosition.getLongitude());
		position.setAltitude(trackedPosition.getAltitude());
		position.setSpeed(trackedPosition.getSpeed());
		position.setCourse(trackedPosition.getCourse());
		position.setAddress(trackedPosition.getAddress());
		position.setOther(trackedPosition.getOther());

		getPositionDao().create(position);

	}

	public Collection<Permission> getPermissions() throws SQLException {
		return QueryBuilder.create(dataSource, "SELECT userId, deviceId FROM user_device;").executeQuery(new Permission());
	}

	public DeviceDao getDeviceDao() {
		return deviceDao;
	}

	public void setDeviceDao(DeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public PositionDao getPositionDao() {
		return positionDao;
	}

	public void setPositionDao(PositionDao positionDao) {
		this.positionDao = positionDao;
	}
}
