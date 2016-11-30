package com.cox.view.model;

import java.util.List;

import com.cox.service.model.LogEvents;

public class LogDetailResponse {
	
	private List<LogEvents> logEvents;
	private boolean flag;
	private String message;

	public List<LogEvents> getLogEvents() {
		return logEvents;
	}
	public void setLogEvents(List<LogEvents> logEvents) {
		this.logEvents = logEvents;
	}
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
	
}
