package com.cox.view.model;

import java.util.Set;

public class EnvGroupsResp {
	
	private Set<String> envGroups;
	private boolean flag;
	private String message;
	
	public Set<String> getEnvGroups() {
		return envGroups;
	}
	public void setEnvGroups(Set<String> envGroups) {
		this.envGroups = envGroups;
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
