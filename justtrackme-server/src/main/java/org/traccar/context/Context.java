package org.traccar.context;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.traccar.database.DataCache;
import org.traccar.database.DataManager;
import org.traccar.database.PermissionsManager;
import org.traccar.geocode.GisgraphyReverseGeocoder;
import org.traccar.geocode.GoogleReverseGeocoder;
import org.traccar.geocode.NominatimReverseGeocoder;
import org.traccar.geocode.ReverseGeocoder;

public class Context {

	private boolean loggerEnabled;
	private Properties properties;
	private DataManager dataManager;
	private DataCache dataCache;
	private ReverseGeocoder reverseGeocoder;
	private PermissionsManager permissionsManager;

	// TODO: Think how to inject all dependencies
	@PostConstruct
	public void init() throws Exception {

		loggerEnabled = Boolean.valueOf(properties.getProperty("logger.enable"));
		if (loggerEnabled) {
			setupLogger();
		}

		if (Boolean.valueOf(properties.getProperty("http.new"))) {
			permissionsManager = new PermissionsManager();
		}

		if (Boolean.parseBoolean(properties.getProperty("geocoder.enable"))) {
			String type = properties.getProperty("geocoder.type");
			if (type != null && type.equals("nominatim")) {
				reverseGeocoder = new NominatimReverseGeocoder(properties.getProperty("geocoder.url"));
			}
			if (type != null && type.equals("gisgraphy")) {
				reverseGeocoder = new GisgraphyReverseGeocoder(properties.getProperty("geocoder.url"));
			} else {
				reverseGeocoder = new GoogleReverseGeocoder();
			}
		}

	}

	private void setupLogger() throws IOException {
		// Workaround for "Bug 745866 - (EDG-45) Possible netty logging config problem"
		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
	}

	public boolean isLoggerEnabled() {
		return loggerEnabled;
	}

	public void setLoggerEnabled(boolean loggerEnabled) {
		this.loggerEnabled = loggerEnabled;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public DataCache getDataCache() {
		return dataCache;
	}

	public void setDataCache(DataCache dataCache) {
		this.dataCache = dataCache;
	}

	public ReverseGeocoder getReverseGeocoder() {
		return reverseGeocoder;
	}

	public void setReverseGeocoder(ReverseGeocoder reverseGeocoder) {
		this.reverseGeocoder = reverseGeocoder;
	}

	public PermissionsManager getPermissionsManager() {
		return permissionsManager;
	}

	public void setPermissionsManager(PermissionsManager permissionsManager) {
		this.permissionsManager = permissionsManager;
	}
}
