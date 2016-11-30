package com.cox.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemoryInfoBean {
	
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonProperty("status")
	private String status;
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private MemoryInfo memoryInfo;
	
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
	public MemoryInfo getMemoryInfo() {
		return memoryInfo;
	}
	public void setMemoryInfo(MemoryInfo memoryInfo) {
		this.memoryInfo = memoryInfo;
	}
	
}