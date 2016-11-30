package com.cox.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionProperty {
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private DBProperty dbProperty;
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
	public DBProperty getDbProperty() {
		return dbProperty;
	}
	public void setDbProperty(DBProperty dbProperty) {
		this.dbProperty = dbProperty;
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
