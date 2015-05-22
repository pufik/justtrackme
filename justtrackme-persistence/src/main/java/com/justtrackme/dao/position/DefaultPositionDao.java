package com.justtrackme.dao.position;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.justtrackme.model.Device;
import com.justtrackme.model.Paged;
import com.justtrackme.model.Position;
import com.justtrackme.model.SearchRequest;

@Repository("positionDao")
public class DefaultPositionDao implements PositionDao {

	private static final String POSITION_LAST_FOR_DEVICE = "SELECT * FROM `position` WHERE `position`.id = (SELECT MAX(`position`.id) FROM `position` LEFT JOIN `device` ON `position`.deviceId = `device`.id WHERE `device`.uniqueId = ?)";
	private static final String POSITION_PAGED_QUERY = "SELECT * FROM `position` LIMIT ? OFFSET ?";
	private static final String POSITION_AMOUNT = "SELECT COUNT(*) FROM `position`";
	private static final String POSITION_FOR_DEVICE_QUERY = "SELECT `position`.* FROM `position`  LEFT JOIN `device` ON `position`.deviceId = `device`.id WHERE `device`.uniqueId = ? ORDER BY `position`.serverTime DESC LIMIT ? OFFSET ?";
	private static final String POSITION_FOR_DEVICE_AMOUNT = "SELECT COUNT(*) FROM `position` LEFT JOIN `device` ON `position`.deviceId = `device`.id WHERE `device`.uniqueId = ?";
	private static final String POSITION_CREATE_QUERY = "INSERT INTO `position` (`protocol`, `deviceId`, `serverTime`, `deviceTime`, `fixTime`, `valid`, `latitude`, `longitude`, `altitude`, `speed`, `course`, `address`, `other`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String POSITION_UPDATE_QUERY = "UPDATE `position` SET `protocol` = ?, `deviceId` = ?, `serverTime` = ?, `deviceTime` = ?, `fixTime` = ?, `valid` = ?, `latitude` = ?, `longitude` = ?, `altitude` = ?, `speed` = ?, `course` = ?, `address` = ?, `other` = ? WHERE `id` = ?";
	private static final String POSITION_GET_ID_QUERY = "SELECT * FROM `position` WHERE id = ?";
	private static final String POSITION_REMOVE_BY_UID = "DELETE FROM `position` WHERE id = ?";

	@Resource(name = "jdbcTemplate")
	private JdbcOperations jdbcTemplate;

	private RowMapper<Position> rowMapper = (rs, index) -> {
		Position position = new Position();
		position.setId(rs.getLong("id"));
		position.setDeviceId(rs.getLong("deviceId"));
		position.setProtocol(rs.getString("protocol"));
		position.setServerTime(rs.getDate("serverTime"));
		position.setDeviceTime(rs.getDate("deviceTime"));
		position.setFixTime(rs.getDate("fixTime"));
		position.setValid(rs.getBoolean("valid"));
		position.setLatitude(rs.getDouble("latitude"));
		position.setLongitude(rs.getDouble("longitude"));
		position.setAltitude(rs.getDouble("altitude"));
		position.setSpeed(rs.getDouble("speed"));
		position.setCourse(rs.getDouble("course"));
		position.setAddress(rs.getString("address"));
		position.setOther(rs.getString("other"));

		return position;
	};

	@Override
	public Position getById(Long positionId) {
		return jdbcTemplate.queryForObject(POSITION_GET_ID_QUERY, rowMapper, positionId);
	}

	@Override
	public void create(Position position) {
		Objects.requireNonNull(position, "Position should be provided");

		jdbcTemplate.update(POSITION_CREATE_QUERY, position.getProtocol(), position.getDeviceId(), position.getServerTime(), position.getDeviceTime(),
				position.getFixTime(), position.isValid(), position.getLatitude(), position.getLongitude(), position.getAltitude(), position.getSpeed(),
				position.getCourse(), position.getAddress(), position.getOther());

	}

	@Override
	public void update(Position position) {
		Objects.requireNonNull(position, "Position should be provided");

		jdbcTemplate.update(POSITION_UPDATE_QUERY, position.getProtocol(), position.getDeviceId(), position.getServerTime(), position.getDeviceTime(),
				position.getFixTime(), position.isValid(), position.getLatitude(), position.getLongitude(), position.getAltitude(), position.getSpeed(),
				position.getCourse(), position.getAddress(), position.getOther(), position.getId());
	}

	@Override
	public void remove(Position position) {
		Objects.requireNonNull(position, "Position should be provided");

		jdbcTemplate.update(POSITION_REMOVE_BY_UID, position.getId());

	}

	@Override
	public Paged<Position> search(SearchRequest request) {
		Paged<Position> result = new Paged<>();
		List<Position> devices = jdbcTemplate.query(POSITION_PAGED_QUERY, rowMapper, request.getLimit(), request.getOffset());
		Integer total = jdbcTemplate.queryForObject(POSITION_AMOUNT, Integer.class);

		result.setLimit(request.getLimit());
		result.setOffset(request.getOffset());
		result.setTotal(total);
		result.setEntities(devices);

		return result;
	}

	@Override
	public Position getLastPositionForDevice(Device device) {
		Objects.requireNonNull(device, "Device should be provided");
		return jdbcTemplate.queryForObject(POSITION_LAST_FOR_DEVICE, rowMapper, device.getUniqueId());
	}

	@Override
	public Paged<Position> getPositionsForDevice(SearchRequest request, Device device) {
		Objects.requireNonNull(device, "Device should be provided");

		Paged<Position> result = new Paged<>();
		List<Position> devices = jdbcTemplate.query(POSITION_FOR_DEVICE_QUERY, rowMapper, device.getUniqueId(), request.getLimit(), request.getOffset());
		Integer total = jdbcTemplate.queryForObject(POSITION_FOR_DEVICE_AMOUNT, Integer.class, device.getUniqueId());
		
		result.setLimit(request.getLimit());
		result.setOffset(request.getOffset());
		result.setTotal(total);
		result.setEntities(devices);

		return result;
	}
}
