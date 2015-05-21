package com.justtrackme.dao.device;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.justtrackme.model.Device;
import com.justtrackme.model.Paged;
import com.justtrackme.model.SearchRequest;

@Repository("deviceDao")
public class DefaultDeviceDao implements DeviceDao {

	private static final String DEVICE_PAGED_QUERY = "SELECT * FROM device LIMIT ? OFFSET ?";
	private static final String DEVICE_AMOUNT = "SELECT COUNT(*) FROM device";
	private static final String DEVICE_UPDATE_QUERY = "UPDATE `device` SET `uniqueId` = ?, `name` = ?, `status` = ?, `lastUpdate` = NOW(), `positionId` = ?, `dataId` = ? WHERE `id` = ?";
	private static final String DEVICE_REMOVE_BY_UID = "DELETE FROM `device` WHERE uniqueId = ?";
	private static final String DEVICE_FIND_BY_UID_QUERY = "SELECT * FROM device WHERE uniqueId = ?";
	private static final String DEVICE_CREATE_QUERY = "INSERT INTO `device` (`uniqueId`, `name`, `status`, `lastUpdate`) VALUES ( ?, ?, ?, NOW())";

	@Resource(name = "jdbcTemplate")
	private JdbcOperations jdbcTemplate;

	private RowMapper<Device> rowMapper = (rs, index) -> {
		Device device = new Device();
		device.setId(rs.getLong("id"));
		device.setUniqueId(rs.getString("uniqueId"));
		device.setName(rs.getString("name"));
		device.setStatus(rs.getString("status"));
		device.setLastUpdate(rs.getDate("lastUpdate"));
		device.setPositionId(rs.getLong("positionId"));
		device.setDataId(rs.getLong("dataId"));

		return device;
	};

	@Override
	public Device getById(String deviceUid) {
		return jdbcTemplate.queryForObject(DEVICE_FIND_BY_UID_QUERY, rowMapper, deviceUid);
	}

	@Override
	public void create(Device entity) {
		Objects.requireNonNull(entity, "Device should be provided");

		jdbcTemplate.update(DEVICE_CREATE_QUERY, entity.getUniqueId(), entity.getName(), entity.getStatus(), entity.getLastUpdate());

	}

	@Override
	public void update(Device device) {
		Objects.requireNonNull(device, "Device should be provided");
		jdbcTemplate.update(DEVICE_UPDATE_QUERY, device.getUniqueId(), device.getName(), device.getStatus(), device.getPositionId(), device.getDataId(),
				device.getId());

	}

	@Override
	public void remove(Device entity) {
		Objects.requireNonNull(entity, "Device should be provided");
		jdbcTemplate.update(DEVICE_REMOVE_BY_UID, entity.getUniqueId());
	}

	@Override
	public Paged<Device> search(SearchRequest request) {
		Paged<Device> result = new Paged<>();
		List<Device> devices = jdbcTemplate.query(DEVICE_PAGED_QUERY, rowMapper, request.getLimit(), request.getOffset());
		Integer total = jdbcTemplate.queryForObject(DEVICE_AMOUNT, Integer.class);

		result.setLimit(request.getLimit());
		result.setOffset(request.getOffset());
		result.setTotal(total);
		result.setEntities(devices);

		return result;
	}

}
