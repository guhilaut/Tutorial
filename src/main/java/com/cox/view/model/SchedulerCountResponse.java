package com.cox.view.model;

import java.util.Map;

public class SchedulerCountResponse {
	
	private Map<String, Integer> schedulerGroup;
	private String message;
	private boolean flag;

	public Map<String, Integer> getSchedulerGroup() {
		return schedulerGroup;
	}
	public void setSchedulerGroup(Map<String, Integer> schedulerGroup) {
		this.schedulerGroup = schedulerGroup;
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
