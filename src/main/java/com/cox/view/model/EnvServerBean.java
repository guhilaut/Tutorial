package com.cox.view.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnvServerBean {
	
	private String envName;
	private List<ServerConfigBean> serverList;
	
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public List<ServerConfigBean> getServerList() {
		return serverList;
	}
	public void setServerList(List<ServerConfigBean>  serverList) {
		this.serverList = serverList;
	}
	
	
}
