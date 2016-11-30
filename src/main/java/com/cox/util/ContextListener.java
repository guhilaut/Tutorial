package com.cox.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ContextListener implements ApplicationListener<ContextRefreshedEvent>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContextListener.class);
	private static Map<String, String> dbPropMap = new HashMap<String, String>();
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//load properties
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+"::onApplicationEvent :::: Spring context has been refreshed,"
					+ " loading all the properties!!!");
		}
		//get the properties file location
		String location = getPropsLocation();
		loadDBProperties(location);
	}

	private String getPropsLocation() {
		Properties properties = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("application.properties");
		String location = null;
		try {
			properties.load(is);
			location = properties.getProperty("app.props.location");
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+":: getPropsLocation :::: error while loading application.properties", e);
			throw new IllegalStateException("Not found location of properties file...", e);
		} catch (Exception e) {
			LOGGER.error(getClass().getName()+":: getPropsLocation :::: error while loading application.properties", e);
			throw new IllegalStateException("Bad things happend...", e);
		}
		return location;
	}

	private void loadDBProperties(String location) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+"::loadDBProperties :::: loading DBConfig.properties!!!");
		}
		Properties properties = new Properties();
		try {
			InputStream is = new FileInputStream(new File(location+"DBConfig.properties"));
			properties.load(is);
			Set<Entry<Object, Object>> entrySet = properties.entrySet();
			for(Entry<Object, Object> entry : entrySet){
				dbPropMap.put((String)entry.getKey(), (String)entry.getValue());
			}
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+":: loadDBProperties :::: error while loading DBConfig.properties", e);
			throw new IllegalStateException("DBConfig.properties not found at "+location, e);
		} catch (Exception e) {
			LOGGER.error(getClass().getName()+":: loadDBProperties :::: error while loading DBConfig.properties", e);
			throw new IllegalStateException("Bad things happend...", e);
		}
	}

	public static Map<String, String> getDbPropMap() {
		return dbPropMap;
	}
	
}
