package com.cox.view.model;

import java.util.List;

import com.cox.service.model.Consumer;

public class ConsumerResponse {
	
	private String message;
	private boolean flag;
	private List<Consumer> consumers;
	
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
	public List<Consumer> getConsumers() {
		return consumers;
	}
	public void setConsumers(List<Consumer> consumers) {
		this.consumers = consumers;
	}
	
	

}
