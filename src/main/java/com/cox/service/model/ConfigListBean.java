package com.cox.service.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigListBean {
	
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private List<String> configList;
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
	public List<String> getConfigList() {
		return configList;
	}
	public void setConfigList(List<String> configList) {
		this.configList = configList;
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
