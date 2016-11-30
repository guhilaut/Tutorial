package com.cox.view.model;

import java.util.List;

public class EnvResponse {
	
	private List<EnvServerBean> envServerList;
	private String message;
	private boolean flag;
	
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
	public List<EnvServerBean> getEnvServerList() {
		return envServerList;
	}
	public void setEnvServerList(List<EnvServerBean> envServerList) {
		this.envServerList = envServerList;
	}
	
}
