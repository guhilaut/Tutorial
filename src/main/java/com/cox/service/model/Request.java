package com.cox.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Request {
	
	private String mbean;
	private String type;
	@JsonIgnore
	private String operation;
	@JsonIgnore
	private List<String> arguments;
	@JsonIgnore
	private List<String> attribute;
	@JsonIgnore
	private String preferredHttpMethod;
	@JsonIgnore
	private String targetConfig;
	@JsonIgnore
	private String objectName;
	
	public String getMbean() {
		return mbean;
	}
	public void setMbean(String mbean) {
		this.mbean = mbean;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public List<String> getArguments() {
		return arguments;
	}
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
	public List<String> getAttribute() {
		return attribute;
	}
	public void setAttribute(List<String> attribute) {
		this.attribute = attribute;
	}
	public String getPreferredHttpMethod() {
		return preferredHttpMethod;
	}
	public void setPreferredHttpMethod(String preferredHttpMethod) {
		this.preferredHttpMethod = preferredHttpMethod;
	}
	public String getTargetConfig() {
		return targetConfig;
	}
	public void setTargetConfig(String targetConfig) {
		this.targetConfig = targetConfig;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
}
