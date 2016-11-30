package com.cox.service.model;

import java.util.Date;

public class CXFMBeanResult {
	
	private Request request;
	private CXFMBeanInfo value;
	private Date timestamp;
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public CXFMBeanInfo getValue() {
		return value;
	}
	public void setValue(CXFMBeanInfo value) {
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
