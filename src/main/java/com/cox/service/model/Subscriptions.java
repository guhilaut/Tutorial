package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscriptions {
	
	@JsonProperty("objectName")
	private String objectName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
}
