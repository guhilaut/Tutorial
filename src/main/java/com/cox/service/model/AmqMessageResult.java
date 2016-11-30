package com.cox.service.model;

import java.util.Date;
import java.util.Map;

public class AmqMessageResult {

	private Request request;
	private Map<String, AmqMessage> value;
	private Date timestamp;
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
	public Map<String, AmqMessage> getValue() {
		return value;
	}
	public void setValue(Map<String, AmqMessage> value) {
		this.value = value;
	}
	
}
