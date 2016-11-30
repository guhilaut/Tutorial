package com.cox.view.model;

import java.util.List;

import com.cox.service.model.DestinationDetails;

public class DestinationDetailsResponse {
	
	private List<DestinationDetails> destinationDetailsList;
	private boolean flag;
	private String message;

	public List<DestinationDetails> getDestinationDetailsList() {
		return destinationDetailsList;
	}
	public void setDestinationDetailsList(
			List<DestinationDetails> destinationDetailsList) {
		this.destinationDetailsList = destinationDetailsList;
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
