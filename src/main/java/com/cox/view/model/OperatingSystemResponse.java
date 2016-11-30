package com.cox.view.model;

import com.cox.service.model.OSMemory;


public class OperatingSystemResponse {
	
	private OSMemory osMemory;
	private boolean flag;
	private String message;
	
	public OSMemory getOsMemory() {
		return osMemory;
	}
	public void setOsMemory(OSMemory osMemory) {
		this.osMemory = osMemory;
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
