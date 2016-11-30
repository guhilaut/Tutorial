package com.cox.view.model;

import com.cox.service.model.SchedulerInfo;

public class SchedulerInfoResponse {
	
	private SchedulerInfo schedulerInfo;
	private String message;
	private boolean flag;
	
	public SchedulerInfo getSchedulerInfo() {
		return schedulerInfo;
	}
	public void setSchedulerInfo(SchedulerInfo schedulerInfo) {
		this.schedulerInfo = schedulerInfo;
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
