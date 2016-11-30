package com.cox.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.cox.service.model.User;
import com.cox.view.model.ServerConfigBean;
/**
 * This class holds configuration information used throughout session.
 * @author SSharma17
 */
@Component
@Scope(value = "session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class Configuration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, String> bundleMap = new HashMap<Integer, String>();
	private List<String> objectNamesList = new ArrayList<String>();
	private String baseUrl;
	private User user;
	//to store the configuration of active server
	private List<ServerConfigBean> activeServersConfig;
	//to store the simple type objectNames
	private List<String> simpleQuartzObjName = new ArrayList<String>();
	//to store the complex type objectNames
	private List<String> complexQuartzObjName = new ArrayList<String>();
	//to store oracle-ds obj-names
	private List<String> oracleObjNames = new ArrayList<String>();
	//to store sql-server-ds obj-names
	private List<String> sqlServerObjNames = new ArrayList<String>();
	//to store unknown ds obj-names
	private List<String> unknownDSObjNames = new ArrayList<String>();
	//to store ip and its jolokia path
	private Map<String, String> ipJolokiaPathMap = new HashMap<String, String>();
	// to store env names under a group
	private  Map<String, List<String>> envGroupsMap = new HashMap<String, List<String>>();
	// to store server configuration (ip, scheme, jolokiapath, 
	private  Map<String, List<ServerConfigBean>> serverIpConfigMap = new HashMap<String, List<ServerConfigBean>>();
	
	
	
	public  Map<String, List<String>> getEnvGroupsMap() {
		return envGroupsMap;
	}


	public void setEnvGroupsMap(Map<String, List<String>> envGroupsMap) {
		this.envGroupsMap = envGroupsMap;
	}


	public  Map<String, List<ServerConfigBean>> getServerIpConfigMap() {
		return serverIpConfigMap;
	}


	public  void setServerIpConfigMap(
			Map<String, List<ServerConfigBean>> serverIpConfigMap) {
		this.serverIpConfigMap = serverIpConfigMap;
	}


	public Map<Integer, String> getBundleMap() {
		return bundleMap;
	}


	public void setBundleMap(Map<Integer, String> bundleMap) {
		this.bundleMap = bundleMap;
	}


	public List<String> getObjectNamesList() {
		return objectNamesList;
	}


	public void setObjectNamesList(List<String> objectNamesList) {
		this.objectNamesList = objectNamesList;
	}


	public String getBaseUrl() {
		return baseUrl;
	}


	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getSimpleQuartzObjName() {
		return simpleQuartzObjName;
	}


	public void setSimpleQuartzObjName(List<String> simpleQuartzObjName) {
		this.simpleQuartzObjName = simpleQuartzObjName;
	}


	public List<String> getComplexQuartzObjName() {
		return complexQuartzObjName;
	}


	public void setComplexQuartzObjName(List<String> complexQuartzObjName) {
		this.complexQuartzObjName = complexQuartzObjName;
	}


	public List<String> getOracleObjNames() {
		return oracleObjNames;
	}


	public void setOracleObjNames(List<String> oracleObjNames) {
		this.oracleObjNames = oracleObjNames;
	}


	public List<String> getSqlServerObjNames() {
		return sqlServerObjNames;
	}


	public void setSqlServerObjNames(List<String> sqlServerObjNames) {
		this.sqlServerObjNames = sqlServerObjNames;
	}


	public List<String> getUnknownDSObjNames() {
		return unknownDSObjNames;
	}


	public void setUnknownDSObjNames(List<String> unknownDSObjNames) {
		this.unknownDSObjNames = unknownDSObjNames;
	}


	public List<ServerConfigBean> getActiveServersConfig() {
		return activeServersConfig;
	}


	public void setActiveServersConfig(List<ServerConfigBean> activeServersConfig) {
		this.activeServersConfig = activeServersConfig;
	}


	public Map<String, String> getIpJolokiaPathMap() {
		return ipJolokiaPathMap;
	}


	public void setIpJolokiaPathMap(Map<String, String> ipJolokiaPathMap) {
		this.ipJolokiaPathMap = ipJolokiaPathMap;
	}
	
	
}
