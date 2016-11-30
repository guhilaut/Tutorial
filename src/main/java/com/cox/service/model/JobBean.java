package com.cox.service.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobBean {
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private Map<String, Map<String, Map<String, Object>>> timerBean;
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
	public Map<String, Map<String, Map<String, Object>>> getTimerBean() {
		return timerBean;
	}
	public void setTimerBean(Map<String, Map<String, Map<String, Object>>> timerBean) {
		this.timerBean = timerBean;
	}
	@Override
	public String toString(){
		return timerBean.toString();
	}
	
}
