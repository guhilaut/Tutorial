package com.cox.view.model;

import java.util.List;

public class DSDependentResponse {

	private List<String> dependentList;
	private boolean flag;
	private String message;
	
	public List<String> getDependentList() {
		return dependentList;
	}
	public void setDependentList(List<String> dependentList) {
		this.dependentList = dependentList;
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
