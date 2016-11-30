package com.cox.view.model;

public class SOAPRequest {
	
	String requestXML;
	String message;
	boolean flag;
	
	public String getRequestXML() {
		return requestXML;
	}
	public void setRequestXML(String requestXML) {
		this.requestXML = requestXML;
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
