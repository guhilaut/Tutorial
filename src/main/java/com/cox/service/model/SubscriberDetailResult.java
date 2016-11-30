package com.cox.service.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberDetailResult {
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonProperty("status")
	private String status;
	@JsonProperty("request")
	private Request request;
	@JsonProperty("value")
	private SubscriberDetails subscriberDetails;
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public SubscriberDetails getSubscriberDetails() {
		return subscriberDetails;
	}
	public void setSubscriberDetails(SubscriberDetails subscriberDetails) {
		this.subscriberDetails = subscriberDetails;
	}
}

