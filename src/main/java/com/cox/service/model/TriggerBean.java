package com.cox.service.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TriggerBean {
	
	private Request request;
	@JsonProperty("value")
	private List<TriggerDetail> triggerDetailList;
	private Date timestamp;
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public List<TriggerDetail> getTriggerDetailList() {
		return triggerDetailList;
	}
	public void setTriggerDetailList(List<TriggerDetail> triggerDetailList) {
		this.triggerDetailList = triggerDetailList;
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
