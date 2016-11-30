package com.cox.view.model;

public class ServerConfigBean {
	
	private String scheme;
	private String ip;
	private String port;
	private String jolokiaUrl;
	private String type;
	
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getJolokiaUrl() {
		return jolokiaUrl;
	}
	public void setJolokiaUrl(String jolokiaUrl) {
		this.jolokiaUrl = jolokiaUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
