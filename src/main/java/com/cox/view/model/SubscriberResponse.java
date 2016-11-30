package com.cox.view.model;

import java.util.List;

public class SubscriberResponse {
	
	private boolean flag;
	private String message;
	private List<Subscriber> subscribers;
	
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
	public List<Subscriber> getSubscriber() {
		return subscribers;
	}
	public void setSubscriber(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
	
}
