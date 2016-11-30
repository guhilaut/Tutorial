package com.cox.service.model;

import java.util.Date;
import java.util.Map;

public class MBeanListResult {
	
	private Request request;
	private Map<Integer, BundleInfo> value;
	private Date timestamp;
	private int status;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public Map<Integer, BundleInfo> getValue() {
		return value;
	}
	public void setValue(Map<Integer, BundleInfo> value) {
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
