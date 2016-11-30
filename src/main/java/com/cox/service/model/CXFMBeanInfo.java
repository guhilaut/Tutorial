package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CXFMBeanInfo {
	
	@JsonProperty("Address")
	private String address;
	@JsonProperty("State")
	private String state;
	@JsonProperty("Swagger")
	private boolean swagger;
	@JsonProperty("ServletContext")
	private String servletContext;
	@JsonProperty("WADL")
	private boolean wadl;
	@JsonProperty("WSDL")
	private boolean wsdl;
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isSwagger() {
		return swagger;
	}
	public void setSwagger(boolean swagger) {
		this.swagger = swagger;
	}
	public String getServletContext() {
		return servletContext;
	}
	public void setServletContext(String servletContext) {
		this.servletContext = servletContext;
	}
	public boolean isWadl() {
		return wadl;
	}
	public void setWadl(boolean wadl) {
		this.wadl = wadl;
	}
	public boolean isWsdl() {
		return wsdl;
	}
	public void setWsdl(boolean wsdl) {
		this.wsdl = wsdl;
	}
	
}
