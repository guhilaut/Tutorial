package com.cox.view.model;


public class ServerInfo {

	private String name;
	private boolean pingStatus;
	private int dbCount;
	private int bundleCount;
	private String message;
	private String jolokiaPath;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isPingStatus() {
		return pingStatus;
	}
	public void setPingStatus(boolean pingStatus) {
		this.pingStatus = pingStatus;
	}
	public int getDbCount() {
		return dbCount;
	}
	public void setDbCount(int dbCount) {
		this.dbCount = dbCount;
	}
	public int getBundleCount() {
		return bundleCount;
	}
	public void setBundleCount(int bundleCount) {
		this.bundleCount = bundleCount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getJolokiaPath() {
		return jolokiaPath;
	}
	public void setJolokiaPath(String jolokiaPath) {
		this.jolokiaPath = jolokiaPath;
	}

}
