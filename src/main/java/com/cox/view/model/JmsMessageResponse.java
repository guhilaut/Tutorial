package com.cox.view.model;

import com.cox.service.model.JmsMessageDetail;

public class JmsMessageResponse {
	
	private JmsMessageDetail messageDetail;
	private String message;
	private boolean flag;

	public JmsMessageDetail getMessageDetail() {
		return messageDetail;
	}
	public void setMessageDetail(JmsMessageDetail messageDetail) {
		this.messageDetail = messageDetail;
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
