package com.cox.view.model;

import java.util.Map;

public class JobBeanResponse {
	
	private Map<String, Map<String, Map<String, Object>>> timerBean;
	private boolean flag;
	private String message;
	
	public Map<String, Map<String, Map<String, Object>>> getTimerBean() {
		return timerBean;
	}
	public void setTimerBean(Map<String, Map<String, Map<String, Object>>> timerBean) {
		this.timerBean = timerBean;
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
