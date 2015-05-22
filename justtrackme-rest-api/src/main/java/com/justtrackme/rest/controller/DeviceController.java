package com.justtrackme.rest.controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.justtrackme.dao.device.DeviceDao;
import com.justtrackme.dao.position.PositionDao;
import com.justtrackme.model.Device;
import com.justtrackme.model.Paged;
import com.justtrackme.model.Position;
import com.justtrackme.model.SearchRequest;
import com.justtrackme.rest.resource.DeviceResource;
import com.justtrackme.rest.resource.PagedResource;
import com.justtrackme.rest.resource.PositionResource;

@RestController
@RequestMapping(DeviceResource.DEVICES_CONTEXT)
public class DeviceController extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceController.class);

	// TODO: We need introduce service layer. DAO as temp solution
	@Resource(name = "deviceDao")
	private DeviceDao deviceDao;

	@Resource(name = "positionDao")
	private PositionDao positionDao;

	@RequestMapping(method = RequestMethod.GET)
	public PagedResource<DeviceResource> getDevices(@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
		SearchRequest searchRequest = createSearchRequest(limit, offset);

		Paged<Device> result = deviceDao.search(searchRequest);

		return createSearchResult(result, this::convertDevice);
	}

	@RequestMapping(value = "/{deviceUid}", method = RequestMethod.GET)
	public DeviceResource getDeviceByUid(@PathVariable("deviceUid") String deviceUid) {
		LOG.debug("Request device info [{}]", deviceUid);
		Device device = deviceDao.getById(deviceUid);

		return convertDevice(device);
	}

	@RequestMapping(value = "/{deviceUid}/positions", method = RequestMethod.GET)
	public PagedResource<PositionResource> getPositionsForDevice(@PathVariable("deviceUid") String deviceUid,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit, @RequestParam(value = "offset", defaultValue = "0") Integer offset) {

		LOG.debug("Request positions for device [{}]", deviceUid);
		SearchRequest searchRequest = createSearchRequest(limit, offset);

		Device device = deviceDao.getById(deviceUid);
		Paged<Position> result = positionDao.getPositionsForDevice(searchRequest, device);
		
		 PagedResource<PositionResource> positions = createSearchResult(result, this::convertPosition);
		 positions.getEntities().stream().forEach((position) -> position.setDevice(convertDevice(device)));

		return positions;

	}

	private <T extends com.justtrackme.rest.resource.Resource, E> PagedResource<T> createSearchResult(Paged<E> result, Function<E, T> convertor) {
		PagedResource<T> searchResult = new PagedResource<>();
		searchResult.setOffset(result.getOffset());
		searchResult.setLimit(result.getLimit());
		searchResult.setTotal(result.getTotal());

		List<T> resources = result.getEntities().stream().map(convertor).collect(Collectors.toList());
		searchResult.setEntities(resources);

		return searchResult;
	}

	private DeviceResource convertDevice(Device device) {
		DeviceResource deviceResource = new DeviceResource();
		deviceResource.setUniqueId(device.getUniqueId());
		deviceResource.setName(device.getName());
		deviceResource.setLastUpdate(device.getLastUpdate());

		return deviceResource;
	}

	private PositionResource convertPosition(Position position) {
		PositionResource positionResourse = new PositionResource();
		positionResourse.setId(position.getId());
		positionResourse.setProtocol(position.getProtocol());
		positionResourse.setServerTime(position.getServerTime());
		positionResourse.setDeviceTime(position.getDeviceTime());
		positionResourse.setFixTime(position.getFixTime());
		positionResourse.setValid(position.isValid());
		positionResourse.setLatitude(position.getLatitude());
		positionResourse.setLongitude(position.getLongitude());
		positionResourse.setAltitude(position.getAltitude());
		positionResourse.setSpeed(position.getSpeed());
		positionResourse.setCourse(position.getCourse());
		positionResourse.setAddress(position.getAddress());
		positionResourse.setOther(position.getOther());

		return positionResourse;
	}

	private SearchRequest createSearchRequest(Integer limit, Integer offset) {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setLimit(limit);
		searchRequest.setOffset(offset);

		return searchRequest;
	}
}
