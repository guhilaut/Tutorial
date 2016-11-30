package com.cox.service.model;

import java.util.Date;
import java.util.List;

public class MBeanSearchResult {
		
	private Request request;
	private List<String> value;
	private Date timestamp;
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
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
