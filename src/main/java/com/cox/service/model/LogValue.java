package com.cox.service.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogValue {

	private String host;
	private Date fromTimestamp;
	private Date toTimestamp;
	@JsonProperty("events")
	private List<LogEvents> logEventsList;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Date getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(Date fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public Date getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(Date toTimestamp) {
		this.toTimestamp = toTimestamp;
	}

	public List<LogEvents> getLogEventsList() {
		return logEventsList;
	}

	public void setLogEventsList(List<LogEvents> logEventsList) {
		if(logEventsList != null){
			Collections.sort(logEventsList);
		}
		this.logEventsList = logEventsList;
	}

	

	
}
