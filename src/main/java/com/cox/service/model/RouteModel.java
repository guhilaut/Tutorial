package com.cox.service.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteModel {
	
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private Map<String, RouteObj> routeMap;
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonProperty("status")
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<String, RouteObj> getRouteMap() {
		return routeMap;
	}
	public void setRouteMap(Map<String, RouteObj> routeMap) {
		this.routeMap = routeMap;
	}
	
}	
	
