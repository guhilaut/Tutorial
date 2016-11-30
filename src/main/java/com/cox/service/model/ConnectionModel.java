package com.cox.service.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionModel {
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private Map<String, String> value;
	private Date timestamp;
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
	public Map<String, String> getValue() {
		return value;
	}
	public void setValue(Map<String, String> value) {
		this.value = value;
	}
	
}
