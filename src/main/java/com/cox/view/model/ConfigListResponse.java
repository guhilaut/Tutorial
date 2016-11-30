package com.cox.view.model;

import java.util.List;

public class ConfigListResponse {
	
	private List<String> configList;
	private String message;
	private boolean flag;
	
	public List<String> getConfigList() {
		return configList;
	}
	public void setConfigList(List<String> configList) {
		this.configList = configList;
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
