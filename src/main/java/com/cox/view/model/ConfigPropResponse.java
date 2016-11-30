package com.cox.view.model;

import java.util.List;

public class ConfigPropResponse {
	
	private List<ConfigProp> configPropList;
	private boolean flag;
	private String message;
	
	public List<ConfigProp> getConfigPropList() {
		return configPropList;
	}
	public void setConfigPropList(List<ConfigProp> configPropList) {
		this.configPropList = configPropList;
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
