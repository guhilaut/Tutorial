package com.cox.view.model;

import java.util.Map;

public class ConnectionModelResponse {
	
	private Map<String, String> connectionAttr;
	private boolean flag;
	private String message;
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, String> getConnectionAttr() {
		return connectionAttr;
	}
	public void setConnectionAttr(Map<String, String> connectionAttr) {
		this.connectionAttr = connectionAttr;
	}
	
}
