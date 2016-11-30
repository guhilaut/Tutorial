package com.cox.view.model;

import com.cox.service.model.Memory;

public class UsedMemoryResponse {
	
	private Memory heapMemory;
	private Memory nonHeapMemory;
	private boolean flag;
	private String message;
	
	public boolean isFlag() {
		return flag;
	}
	public Memory getHeapMemory() {
		return heapMemory;
	}
	public void setHeapMemory(Memory heapMemory) {
		this.heapMemory = heapMemory;
	}
	public Memory getNonHeapMemory() {
		return nonHeapMemory;
	}
	public void setNonHeapMemory(Memory nonHeapMemory) {
		this.nonHeapMemory = nonHeapMemory;
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
