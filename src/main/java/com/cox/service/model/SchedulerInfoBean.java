package com.cox.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchedulerInfoBean {
	
	@JsonProperty("request")
	private Request request;
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonProperty("status")
	private boolean status;
	@JsonProperty("value")
	private SchedulerInfo schedulerInfo;
	
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public SchedulerInfo getSchedulerInfo() {
		return schedulerInfo;
	}
	public void setSchedulerInfo(SchedulerInfo schedulerInfo) {
		this.schedulerInfo = schedulerInfo;
	}

}
