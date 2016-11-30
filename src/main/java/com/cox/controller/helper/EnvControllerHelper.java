package com.cox.controller.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.service.impl.Configuration;
import com.cox.view.model.ServerConfigBean;

@Service
public class EnvControllerHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnvControllerHelper.class);
	
	@Autowired
	private Configuration configuration;
	
	public void loadEnvGroups(String homepage){
		Properties inProps = readProperties(homepage+"_groups.properties");
		Set<Entry<Object, Object>> entrySet = inProps.entrySet();
		for(Entry<Object, Object> entry : entrySet){
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			String[] environments = value.split(",");
			List<String> envList = new ArrayList<String>();
			for(String env : environments){
				if(env.contains(key)){
					env = env.substring(env.indexOf("_")+1);
					envList.add(env);
				}
			}
			configuration.getEnvGroupsMap().put(key, envList);
		}
	}
	
	public void addEnvGroup(String homepage,String groupName){
		String filename = homepage+"_groups.properties";
		Properties props = readProperties(filename);
		if(props.containsKey(groupName)){
			throw new IllegalStateException("Environment Group "+groupName+" already exist, Please edit and update.");
		}
		props.setProperty(groupName, "");
		writeProperties(props, filename);
		
		/*Set<Entry<Object, Object>> entrySet = props.entrySet();
		for(Entry<Object, Object> entry : entrySet){
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			String[] environments = value.split(",");
			List<String> envList = new ArrayList<String>();
			if(environments != null && "".equals(environments)){
				for(String env : environments){
					envList.add(env);
				}
			}
			configuration.getEnvGroupsMap().put(key, envList);
		}*/
		configuration.getEnvGroupsMap().put(groupName, new ArrayList<String>());
	}

	public void deleteEnvGroup(String homepage, String envGrpName) {
		Properties props = readProperties(homepage+"_groups.properties");
		Properties configProp = readProperties(homepage+"_serverConfig.properties");
		Properties prop = readProperties(homepage+"_server.properties");
		String envVal = props.getProperty(envGrpName);
		if(StringUtils.isNotBlank(envVal)){
			for(String envName : envVal.trim().split(",")){
				String envServers = prop.getProperty(envName); 
				for(String server : envServers.split(",")){
					if(configProp.containsKey(server+"_scheme") && configProp.containsKey(server+"_port") && configProp.containsKey(server+"_jolokiaUrl")){
						configProp.remove(server+"_scheme");
						configProp.remove(server+"_port");
						configProp.remove(server+"_jolokiaUrl");
					}
				}
				if(prop.containsKey(envName)){
					prop.remove(envName);
				}
				configuration.getServerIpConfigMap().remove(envName);
			}
		}
		if(props.containsKey(envGrpName)){
			props.remove(envGrpName);
			writeProperties(props, homepage+"_groups.properties");
			writeProperties(prop, homepage+"_server.properties");
			writeProperties(configProp, homepage+"_serverConfig.properties");
		}
		
		configuration.getEnvGroupsMap().remove(envGrpName);
		
	}
	
	public void updateEnvGroup(String homepage, String oldName, String newName){
		Properties props = readProperties(homepage+"_groups.properties");
		Properties serverProp = readProperties(homepage+"_server.properties");
		if(props.containsKey(newName)){
			throw new IllegalStateException("Environment Group "+newName+" already exist, Please edit and update.");
		}
		String envStrs = props.getProperty(oldName);
		String newEnvStrs = "";
		if(StringUtils.isNotBlank(envStrs)){
			String envNames[] = envStrs.split(",");
			String envKey = "";
			for(int i=0; i<envNames.length; i++){
				envKey = (newName+"_"+envNames[i].substring(envNames[i].indexOf("_")+1));
				 String servers = (String) serverProp.remove(envNames[i]);
				 serverProp.setProperty(envKey, servers);
				if(i == envNames.length-1)
					newEnvStrs = newEnvStrs + envKey;
				else
					newEnvStrs = newEnvStrs + envKey + ",";
			}
			
		}
		props.remove(oldName);
		props.setProperty(newName, newEnvStrs);
		writeProperties(serverProp, homepage+"_server.properties");
		writeProperties(props, homepage+"_groups.properties");
		/*
		Properties props = readProperties(homepage+"_groups.properties");
		if(props.containsKey(newName)){
			throw new IllegalStateException("Environment Group "+newName+" already exist, Please edit and update.");
		}
		String envStrs = props.getProperty(oldName);
		props.remove(oldName);
		props.setProperty(newName, envStrs);
		writeProperties(props, homepage+"_groups.properties");*/
		List<String> envList = configuration.getEnvGroupsMap().remove(oldName);
		configuration.getEnvGroupsMap().put(newName, envList);
	}
	
	/*public void loadEnv(String homepage, String envName, String envGrpName){
		Properties props = readProperties(homepage+"_server.properties");
		String serverStrs = (String)props.get(envGrpName+"_"+envName);
		String[] servers = serverStrs.split(",");
		List<String> serverList = new ArrayList<String>();
		for(String server : servers){
			serverList.add(server);
		}
		ContextListener.getEnvServersMap().put(envName, serverList);
	}*/
	
	public List<ServerConfigBean> loadEnv(String homepage,String envName, String envGrpName){
		Properties props = readProperties(homepage+"_server.properties");
		String serverStrs = (String)props.get(envGrpName+"_"+envName);
		String[] servers = serverStrs.split(",");
		List<ServerConfigBean> serverList = new ArrayList<ServerConfigBean>();
		for(String server : servers){
			props = readProperties(homepage+"_serverConfig.properties");
			ServerConfigBean amqServer = new ServerConfigBean();
			amqServer.setIp(server);
			amqServer.setJolokiaUrl(props.getProperty(server+"_jolokiaUrl"));
			amqServer.setPort(props.getProperty(server+"_port"));
			amqServer.setScheme(props.getProperty(server+"_scheme"));
			amqServer.setType(homepage.toUpperCase());
			serverList.add(amqServer);
		}
		configuration.getServerIpConfigMap().put(envName, serverList);
		return serverList;
	}
	
	/*public void addEnv(String homepage,String envGrpName, String envName, List<String> serverList){
		Properties p = readProperties(homepage+"_groups.properties");
		String envStrs = p.getProperty(envGrpName);
		if(envStrs.contains(envGrpName+"_"+envName)){
			throw new IllegalStateException("Environment "+envName+" already exist in group "+envGrpName+", Please edit and update.");
		}
		if(!"".equals(envStrs)){
			envStrs = envStrs+","+envGrpName+"_"+envName;
		}else{
			envStrs = envGrpName+"_"+envName;
		}
		p.setProperty(envGrpName, envStrs);
		writeProperties(p, homepage+"_groups.properties");
		
		Properties props = readProperties(homepage+"_server.properties");
		int count = 0;
		String servers = "";
		for(String server : serverList){
			count++;
			if(count == serverList.size()){
				servers = servers+server;
			}else{
				servers = servers+server+",";
			}
		}
		props.put(envGrpName+"_"+envName, servers);
		writeProperties(props, homepage+"_server.properties");
		
		ContextListener.getEnvServersMap().put(envName, serverList);
		ContextListener.getEnvGroupsMap().get(envGrpName).add(envName);
	}*/
	
	public void addEnv(String homepage,String envGrpName, String envName, List<ServerConfigBean> serverList){
		
		Properties p = readProperties(homepage+"_groups.properties");
		String envStrs = p.getProperty(envGrpName);
		if(envStrs.contains(envGrpName+"_"+envName)){
			throw new IllegalStateException("Environment "+envName+" already exist in group "+envGrpName+", Please edit and update.");
		}
		if(!"".equals(envStrs)){
			envStrs = envStrs+","+envGrpName+"_"+envName;
		}else{
			envStrs = envGrpName+"_"+envName;
		}
		p.setProperty(envGrpName, envStrs);
		writeProperties(p, homepage+"_groups.properties");
		
		Properties props = readProperties(homepage+"_serverConfig.properties");
		int count = 0;
		String servers = "";
		for(ServerConfigBean server : serverList){
			count++;
			if(count == serverList.size()){
				servers = servers+server.getIp();
			}else{
				servers = servers+server.getIp()+",";
			}
			props.put(server.getIp()+"_scheme", server.getScheme());
			props.put(server.getIp()+"_port", server.getPort());
			props.put(server.getIp()+"_jolokiaUrl", server.getJolokiaUrl());
			writeProperties(props, homepage+"_serverConfig.properties");
			//set type for serverConfig
			server.setType(homepage.toUpperCase());
		}
		
		props = readProperties(homepage+"_server.properties");
		props.put(envGrpName+"_"+envName, servers);
		writeProperties(props, homepage+"_server.properties");
		
		configuration.getServerIpConfigMap().put(envName, serverList);
		configuration.getEnvGroupsMap().get(envGrpName).add(envName);
	}
	
	/*public void deleteEnv(String homepage,String envGrpName, String envName){
		Properties p = readProperties(homepage+"_groups.properties");
		String envStrs = p.getProperty(envGrpName);
		String newEnvStr = "";
		String[] envArr = envStrs.split(",");
		int count = 0;
		for(String env : envArr){
			count++;
			if(!env.equals(envGrpName+"_"+envName)){
				if(envArr.length == count){
					newEnvStr = newEnvStr + env;
				}else{
					newEnvStr = newEnvStr + env+",";
				}
			}
		}
		p.setProperty(envGrpName, newEnvStr);
		writeProperties(p, homepage+"_groups.properties");
		
		Properties props = readProperties(homepage+"_server.properties");
		props.remove(envGrpName+"_"+envName);
		writeProperties(props, homepage+"_server.properties");
		ContextListener.getEnvServersMap().remove(envName);
		ContextListener.getEnvGroupsMap().get(envGrpName).remove(envName);
	}*/
	
	public void deleteEnv(String homepage,String envGrpName, String envName){
		Properties p = readProperties(homepage+"_groups.properties");
		String envStrs = p.getProperty(envGrpName);
		String newEnvStr = "";
		String[] envArr = envStrs.split(",");
		int count = 0;
		for(String env : envArr){
			count++;
			if(!env.equals(envGrpName+"_"+envName)){
				if(envArr.length == count){
					newEnvStr = newEnvStr + env;
				}else{
					newEnvStr = newEnvStr + env+",";
				}
			}
		}
		p.setProperty(envGrpName, newEnvStr);
		writeProperties(p, homepage+"_groups.properties");
		
		Properties props = readProperties(homepage+"_server.properties");
		Properties configProp = readProperties(homepage+"_serverConfig.properties");
		String envServers = props.getProperty(envGrpName+"_"+envName);
		String[] servers = envServers.split(",");
		for(String server: servers){
			configProp.remove(server+"_scheme");
			configProp.remove(server+"_port");
			configProp.remove(server+"_jolokiaUrl");
		}
		props.remove(envGrpName+"_"+envName);
		writeProperties(props, homepage+"_server.properties");
		configuration.getServerIpConfigMap().remove(envName);
		configuration.getEnvGroupsMap().get(envGrpName).remove(envName);
	}
	
	/*public void updateEnv(String homepage,String envGrpName, String oldEnvName, String newEnvName, List<String> serverList){
		
		Properties p = readProperties(homepage+"_groups.properties");
		String envStrs = p.getProperty(envGrpName);
		String newEnvStr = "";
		String[] envArr = envStrs.split(",");
		String[] newEnvArr = new String[envArr.length];
		int counter = 0;
		for(String env : envArr){
			if(env.equals(envGrpName+"_"+oldEnvName)){
				newEnvArr[counter] = envGrpName+"_"+newEnvName;
				continue;
			}
			newEnvArr[counter] = env;
			counter++;
		}
		counter = 0;
		for(String env : newEnvArr){
			counter++;
			if(envArr.length == counter){
				newEnvStr = newEnvStr + env;
			}else{
				newEnvStr = newEnvStr + env +",";
			}
		}
		p.setProperty(envGrpName, newEnvStr);
		writeProperties(p, homepage+"_groups.properties");
		
		Properties props = readProperties(homepage+"_server.properties");
		counter = 0;
		String servers = "";
		for(String server : serverList){
			counter++;
			if(counter == serverList.size()){
				servers = servers+server;
			}else{
				servers = servers+server+",";
			}
		}
		props.remove(envGrpName+"_"+oldEnvName);
		props.setProperty(envGrpName+"_"+newEnvName, servers);
		writeProperties(props, homepage+"_server.properties");
		ContextListener.getEnvServersMap().remove(oldEnvName);
		ContextListener.getEnvServersMap().put(newEnvName, serverList);
		ContextListener.getEnvGroupsMap().get(envGrpName).remove(oldEnvName);
		ContextListener.getEnvGroupsMap().get(envGrpName).add(newEnvName);
	}*/
	
	public void updateEnv(String homepage, String envGrpName, String oldEnvName, String newEnvName, List<ServerConfigBean> serverList){
		
		Properties p = readProperties(homepage+"_groups.properties");
		String envStrs = p.getProperty(envGrpName);
		String newEnvStr = "";
		String[] envArr = envStrs.split(",");
		String[] newEnvArr = new String[envArr.length];
		int counter = 0;
		for(String env : envArr){
			if(env.equals(envGrpName+"_"+oldEnvName)){
				newEnvArr[counter] = envGrpName+"_"+newEnvName;
				continue;
			}
			newEnvArr[counter] = env;
			counter++;
		}
		counter = 0;
		for(String env : newEnvArr){
			counter++;
			if(envArr.length == counter){
				newEnvStr = newEnvStr + env;
			}else{
				newEnvStr = newEnvStr + env +",";
			}
		}
		p.setProperty(envGrpName, newEnvStr);
		writeProperties(p, homepage+"_groups.properties");
		
		
		Properties props = readProperties(homepage+"_serverConfig.properties");
		counter = 0;
		String servers = "";
		for(ServerConfigBean server : serverList){
			counter++;
			if(counter == serverList.size()){
				servers = servers+server.getIp();
			}else{
				servers = servers+server.getIp()+",";
			}
			props.put(server.getIp()+"_scheme", server.getScheme());
			props.put(server.getIp()+"_port", server.getPort());
			props.put(server.getIp()+"_jolokiaUrl", server.getJolokiaUrl());
			writeProperties(props, homepage+"_serverConfig.properties");
			//set type for serverConfig
			server.setType(homepage.toUpperCase());
		}
		
		props = readProperties(homepage+"_server.properties");
		props.remove(envGrpName+"_"+oldEnvName);
		props.setProperty(envGrpName+"_"+newEnvName, servers);
		writeProperties(props, homepage+"_server.properties");
		
		configuration.getServerIpConfigMap().remove(oldEnvName);
		configuration.getServerIpConfigMap().put(newEnvName, serverList);
		configuration.getEnvGroupsMap().get(envGrpName).remove(oldEnvName);
		configuration.getEnvGroupsMap().get(envGrpName).add(newEnvName);
	}


	private void writeProperties(Properties props, String fileName) {
		OutputStream out = null;
		String location = getPropertiesPath();
		try{
			out = new FileOutputStream(new File(location+fileName));
			props.store(out, null);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+":: loadEnvGroups :::: error while loading "+fileName, e);
		} catch (Exception e) {
			LOGGER.error(getClass().getName()+":: loadEnvGroups :::: error while loading "+fileName, e);
		} finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					LOGGER.error(getClass().getName()+":: getEnvGroups :::: error while closing InputSteam", e);
				}
			}
		}
	}

	private Properties readProperties(String fileName) {
		Properties props = new Properties();
		String location = getPropertiesPath();
		InputStream is = null;
		try{
			File dir = new File(location);
			if(!dir.exists()){
				boolean isCreated = dir.mkdirs();
				if(!isCreated){
					throw new IllegalStateException("Not able to create directory "+location);
				}
			}
			File file = new File(location+fileName);
			if(!file.exists()){
				boolean isCreated = file.createNewFile();
				if(!isCreated){
					throw new IllegalStateException("Not able to create file "+location+fileName);
				}
			}
			is = new FileInputStream(new File(location+fileName));
			props.load(is);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+":: loadEnvGroups :::: error while loading "+fileName, e);
		} catch (Exception e) {
			LOGGER.error(getClass().getName()+":: loadEnvGroups :::: error while loading "+fileName, e);
		} finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error(getClass().getName()+":: getEnvGroups :::: error while closing InputSteam", e);
				}
			}
		}
		return props;
	}

	private String getPropertiesPath(){
		Properties properties = new Properties();
		String location = null;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("application.properties");
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
	
}
