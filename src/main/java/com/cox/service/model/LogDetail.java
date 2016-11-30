package com.cox.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogDetail {
	
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private LogValue logValue;
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonProperty("status")
	private String status;

	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public LogValue getLogValue() {
		return logValue;
	}
	public void setLogValue(LogValue logValue) {
		this.logValue = logValue;
	}
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
	
}
