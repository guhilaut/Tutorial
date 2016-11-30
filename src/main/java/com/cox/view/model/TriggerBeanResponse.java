package com.cox.view.model;

import java.util.List;

import com.cox.service.model.TriggerDetail;

public class TriggerBeanResponse {
	
	private List<TriggerDetail> triggerDetailList;
	private boolean flag;
	private String message;
	
	public List<TriggerDetail> getTriggerDetailList() {
		return triggerDetailList;
	}
	public void setTriggerDetailList(List<TriggerDetail> triggerDetailList) {
		this.triggerDetailList = triggerDetailList;
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
