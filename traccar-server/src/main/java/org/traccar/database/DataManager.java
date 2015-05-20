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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.traccar.model.Device;
import org.traccar.model.Permission;
import org.traccar.model.Position;
import org.traccar.model.User;

import com.justtrackme.dao.device.DeviceDao;

public class DataManager {

	private final DataSource dataSource;
	
	@Resource(name = "deviceDao")
	private DeviceDao deviceDao;

	private NamedParameterStatement queryAddPosition;
	private NamedParameterStatement queryUpdateLatestPosition;

	public DataManager(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@PostConstruct
	public void init() throws Exception {
		// Initialize queries
		queryAddPosition = new NamedParameterStatement(
				"INSERT INTO position (deviceId, serverTime, deviceTime, fixTime, valid, latitude, longitude, altitude, speed, course, address, other)"
						+ " VALUES (:deviceId, NOW(), :time, :time, :valid, :latitude, :longitude, :altitude, :speed, :course, :address, :other);", dataSource,
				Statement.RETURN_GENERATED_KEYS);

		queryUpdateLatestPosition = new NamedParameterStatement("UPDATE device SET positionId = :id WHERE id = :deviceId;", dataSource);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public Device getDeviceByUniqueId(String uniqueId) throws SQLException {
		Device trackDevice = null;
		
		com.justtrackme.model.Device device = deviceDao.getById(uniqueId);
		
		if(Objects.nonNull(device)){
			trackDevice = new Device();
			trackDevice.setId(device.getId());
			trackDevice.setUniqueId(device.getUniqueId());
			trackDevice.setName(device.getName());
		}

		return trackDevice; 
	}

	private NamedParameterStatement.ResultSetProcessor<Long> generatedKeysResultSetProcessor = new NamedParameterStatement.ResultSetProcessor<Long>() {
		@Override
		public Long processNextRow(ResultSet rs) throws SQLException {
			return rs.getLong(1);
		}
	};

	public synchronized Long addPosition(Position position) throws SQLException {
		if (queryAddPosition != null) {
			List<Long> result = assignVariables(queryAddPosition.prepare(), position).executeUpdate(generatedKeysResultSetProcessor);
			if (result != null && !result.isEmpty()) {
				return result.iterator().next();
			}
		}
		return null;
	}

	public void updateLatestPosition(Position position, Long positionId) throws SQLException {
		if (queryUpdateLatestPosition != null) {
			assignVariables(queryUpdateLatestPosition.prepare(), position).setLong("id", positionId).executeUpdate();
		}
	}

	private NamedParameterStatement.Params assignVariables(NamedParameterStatement.Params params, Position position) throws SQLException {

		params.setString("protocol", position.getProtocol());
		params.setLong("deviceId", position.getDeviceId());
		params.setTimestamp("deviceTime", position.getDeviceTime());
		params.setTimestamp("fixTime", position.getFixTime());
		params.setBoolean("valid", position.getValid());
		params.setDouble("altitude", position.getAltitude());
		params.setDouble("latitude", position.getLatitude());
		params.setDouble("longitude", position.getLongitude());
		params.setDouble("speed", position.getSpeed());
		params.setDouble("course", position.getCourse());
		params.setString("address", position.getAddress());
		params.setString("other", position.getOther());

		// temporary
		params.setTimestamp("time", position.getFixTime());
		params.setLong("device_id", position.getDeviceId());
		params.setLong("power", null);
		params.setString("extended_info", position.getOther());

		return params;
	}

	public User login(String email, String password) throws SQLException {
		Collection<User> result = QueryBuilder
				.create(dataSource,
						"SELECT * FROM user WHERE email = :email AND " + "password = CAST(HASH('SHA256', STRINGTOUTF8(:password), 1000) AS VARCHAR);")
				.setString("email", email).setString("password", password).executeQuery(new User());
		if (!result.isEmpty()) {
			return result.iterator().next();
		} else {
			return null;
		}
	}

	public void addUser(User user) throws SQLException {
		user.setId(QueryBuilder
				.create(dataSource,
						"INSERT INTO user (name, email, password, salt, admin) "
								+ "VALUES (:name, :email, CAST(HASH('SHA256', STRINGTOUTF8(:password), 1000) AS VARCHAR), '', :admin);").setObject(user)
				.executeUpdate());
	}

	public Collection<Permission> getPermissions() throws SQLException {
		return QueryBuilder.create(dataSource, "SELECT userId, deviceId FROM user_device;").executeQuery(new Permission());
	}

	public Collection<Device> getDevices(long userId) throws SQLException {
		return QueryBuilder.create(dataSource, "SELECT * FROM device WHERE id IN (" + "SELECT deviceId FROM user_device WHERE userId = :userId);")
				.setLong("userId", userId).executeQuery(new Device());
	}

	public void addDevice(Device device) throws SQLException {
		device.setId(QueryBuilder.create(dataSource, "INSERT INTO device (name, uniqueId) VALUES (:name, :uniqueId);").setObject(device).executeUpdate());
	}

	public void updateDevice(Device device) throws SQLException {
		QueryBuilder.create(dataSource, "UPDATE device SET name = :name, uniqueId = :uniqueId WHERE id = :id;").setObject(device).executeUpdate();
	}

	public void removeDevice(Device device) throws SQLException {
		QueryBuilder.create(dataSource, "DELETE FROM device WHERE id = :id;").setObject(device).executeUpdate();
	}

	public void linkDevice(long userId, long deviceId) throws SQLException {
		QueryBuilder.create(dataSource, "INSERT INTO user_device (userId, deviceId) VALUES (:userId, :deviceId);").setLong("userId", userId)
				.setLong("deviceId", deviceId).executeUpdate();
	}

}
