package com.cox.view.model;

import java.util.List;

public class ServerStatusResponse {
	
	private List<ServerInfo> serverInfoList;
	private boolean flag;
	private String message;
	
	public List<ServerInfo> getServerInfoList() {
		return serverInfoList;
	}
	public void setServerInfoList(List<ServerInfo> serverInfoList) {
		this.serverInfoList = serverInfoList;
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
