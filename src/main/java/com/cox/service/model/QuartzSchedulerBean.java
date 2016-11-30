package com.cox.service.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuartzSchedulerBean {
	
	@JsonProperty("request")	
	private Request request;
	@JsonProperty("value")
	private List<String> schedulerName;
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
	public List<String> getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(List<String> schedulerName) {
		this.schedulerName = schedulerName;
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
