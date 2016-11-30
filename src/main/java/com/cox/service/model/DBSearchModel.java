package com.cox.service.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DBSearchModel {

	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private List<String> objectNames;
	private Date timestamp;
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public List<String> getObjectNames() {
		return objectNames;
	}
	public void setObjectNames(List<String> objectNames) {
		this.objectNames = objectNames;
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
}
