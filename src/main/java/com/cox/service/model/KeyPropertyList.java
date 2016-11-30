package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyPropertyList {
	
	@JsonProperty("destinationName")
	private String destinationName;
	@JsonProperty("destinationType")
	private String destinationType;
	@JsonProperty("brokerName")
	private String brokerName;
	@JsonProperty("type")
	private String type;
	
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getDestinationType() {
		return destinationType;
	}
	public void setDestinationType(String destinationType) {
		this.destinationType = destinationType;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
