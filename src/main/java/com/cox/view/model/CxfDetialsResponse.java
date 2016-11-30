package com.cox.view.model;

import java.util.List;

public class CxfDetialsResponse {
	
	private String endpoint;
	private List<String> operations;
	private String url;
	private boolean isWsdl;
	
	public boolean isWsdl() {
		return isWsdl;
	}
	public void setWsdl(boolean isWsdl) {
		this.isWsdl = isWsdl;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public List<String> getOperations() {
		return operations;
	}
	public void setOperations(List<String> operations) {
		this.operations = operations;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
