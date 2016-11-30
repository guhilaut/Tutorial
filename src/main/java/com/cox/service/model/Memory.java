package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Memory {
	
	@JsonProperty("init")
	private long initial;
	@JsonProperty("committed")
	private long committed;
	@JsonProperty("max")
	private long max;
	@JsonProperty("used")
	private long used;
	
	public long getInitial() {
		return initial;
	}
	public void setInitial(long initial) {
		this.initial = initial;
	}
	public long getCommitted() {
		return committed;
	}
	public void setCommitted(long committed) {
		this.committed = committed;
	}
	public long getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}

}
