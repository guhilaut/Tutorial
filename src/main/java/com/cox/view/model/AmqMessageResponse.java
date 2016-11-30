package com.cox.view.model;

import java.util.List;

import com.cox.service.model.AmqMessage;

public class AmqMessageResponse {

	private List<AmqMessage> messageList;
	private boolean flag;
	private String message;

	public List<AmqMessage> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<AmqMessage> messageList) {
		this.messageList = messageList;
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
