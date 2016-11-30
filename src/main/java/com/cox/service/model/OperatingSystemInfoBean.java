package com.cox.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperatingSystemInfoBean {
	
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonProperty("status")
	private String status;
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	OSMemory osMemory;
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public OSMemory getOsMemory() {
		return osMemory;
	}
	public void setOsMemory(OSMemory osMemory) {
		this.osMemory = osMemory;
	}
}
