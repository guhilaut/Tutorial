package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DBProperty {
	@JsonProperty("Url")
	private String url;
	@JsonProperty("Username")
	private String username;
	@JsonProperty("Password")
	private String password;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
