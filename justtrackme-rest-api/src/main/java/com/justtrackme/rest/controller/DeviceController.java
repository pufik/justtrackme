package com.justtrackme.rest.controller;

import java.util.List;
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
import com.justtrackme.model.Device;
import com.justtrackme.model.Paged;
import com.justtrackme.model.SearchRequest;
import com.justtrackme.rest.resource.DeviceResource;
import com.justtrackme.rest.resource.PagedResource;

@RestController
@RequestMapping(DeviceResource.DEVICES_CONTEXT)
public class DeviceController extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceController.class);

	// TODO: We need introduce service layer. DAO as temp solution
	@Resource(name = "deviceDao")
	private DeviceDao deviceDao;

	@RequestMapping(method = RequestMethod.GET)
	public PagedResource<DeviceResource> getDevices(@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset) {
		PagedResource<DeviceResource> searchResult = new PagedResource<>();

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setLimit(limit);
		searchRequest.setOffset(offset);

		Paged<Device> result = deviceDao.search(searchRequest);
		List<DeviceResource> deviceResources = result.getEntities().stream().map(this::convert).collect(Collectors.toList());

		searchResult.setOffset(result.getOffset());
		searchResult.setLimit(result.getLimit());
		searchResult.setTotal(result.getTotal());
		searchResult.setEntities(deviceResources);

		return searchResult;
	}

	@RequestMapping(value = "/{deviceUid}", method = RequestMethod.GET)
	public DeviceResource getDeviceByUid(@PathVariable("deviceUid") String deviceUid) {
		LOG.debug("Request device info [{}]", deviceUid);
		Device device = deviceDao.getById(deviceUid);

		// TODO: Move to converter
		DeviceResource deviceResource = convert(device);

		return deviceResource;
	}

	private DeviceResource convert(Device device) {
		DeviceResource deviceResource = new DeviceResource();
		deviceResource.setUniqueId(device.getUniqueId());
		deviceResource.setName(device.getName());
		deviceResource.setLastUpdate(device.getLastUpdate());
		
		return deviceResource;
	}
}
