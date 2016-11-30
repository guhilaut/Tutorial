package com.cox.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TriggerState {
	
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonProperty("status")
	private String status;
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private String triggerState;
	
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
	public String getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}
	
}
