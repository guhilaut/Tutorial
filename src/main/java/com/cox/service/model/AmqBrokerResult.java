package com.cox.service.model;

import java.util.Date;

import com.cox.view.model.AmqBrokerInfo;

public class AmqBrokerResult {
	
	private Request request;
	private AmqBrokerInfo value;
	private Date timestamp;
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public AmqBrokerInfo getValue() {
		return value;
	}
	public void setValue(AmqBrokerInfo value) {
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
