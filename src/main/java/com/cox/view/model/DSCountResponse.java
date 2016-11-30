package com.cox.view.model;

import java.util.Map;

public class DSCountResponse {
	
	private Map<String, Integer> dsGroup;
	private String message;
	private boolean flag;

	public Map<String, Integer> getDsGroup() {
		return dsGroup;
	}
	public void setDsGroup(Map<String, Integer> dsGroup) {
		this.dsGroup = dsGroup;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
