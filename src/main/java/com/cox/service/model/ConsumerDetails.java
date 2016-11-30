package com.cox.service.model;

import java.util.Date;

public class ConsumerDetails {
	private Request request;
	private Consumer value;
	private Date timestamp;
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public Consumer getValue() {
		return value;
	}
	public void setValue(Consumer value) {
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
