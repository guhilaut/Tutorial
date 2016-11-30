package com.cox.view.model;

import java.util.List;

public class ServerListResponse {
	
	private List<String> serverNameList;
	private String message;
	private boolean flag;
	
	public List<String> getServerNameList() {
		return serverNameList;
	}
	public void setServerNameList(List<String> serverNameList) {
		this.serverNameList = serverNameList;
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
