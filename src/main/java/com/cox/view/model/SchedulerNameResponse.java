package com.cox.view.model;

import java.util.List;

public class SchedulerNameResponse {
	
	private List<Category> schedulerNameList;
	private boolean flag;
	private String message;
	
	public List<Category> getSchedulerNameList() {
		return schedulerNameList;
	}
	public void setSchedulerNameList(List<Category> schedulerNameList) {
		this.schedulerNameList = schedulerNameList;
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
