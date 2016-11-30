package com.cox.view.model;

public class AmqBrokerResponse{

	private AmqBrokerInfo amqBrokerInfo;
	private boolean flag;
	private String message;

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
	public AmqBrokerInfo getAmqBrokerInfo() {
		return amqBrokerInfo;
	}
	public void setAmqBrokerInfo(AmqBrokerInfo amqBrokerInfo) {
		this.amqBrokerInfo = amqBrokerInfo;
	}
	
}
