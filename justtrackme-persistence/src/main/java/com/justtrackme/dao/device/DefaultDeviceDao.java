package com.justtrackme.dao.device;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.justtrackme.model.Device;
import com.justtrackme.model.Paged;
import com.justtrackme.model.SearchRequest;

@Repository("deviceDao")
public class DefaultDeviceDao implements DeviceDao {

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
		return jdbcTemplate.queryForObject("SELECT * FROM device WHERE uniqueId = ?", rowMapper, deviceUid);
	}

	@Override
	public void create(Device entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Device entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Device entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Paged<Device> search(SearchRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
