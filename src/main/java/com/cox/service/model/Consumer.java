package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Consumer {
	
	@JsonProperty("Retroactive")
	private boolean retroactive;
	
	@JsonProperty("Exclusive")
	private boolean exclusive;
	
	@JsonProperty("ConnectionId")
	private String connectionId;
	
	@JsonProperty("MaximumPendingMessageLimit")
	private long maximumPendingMessageLimit;
	
	@JsonProperty("ClientId")
	private String clientId;
	
	@JsonProperty("SessionId")
	private long sessionId;
	
	@JsonProperty("EnqueueCounter")
	private long enqueueCounter;
	
	@JsonProperty("Selector")
	private long selector;
	
	@JsonProperty("PrefetchSize")
	private long prefetchSize;
	
	@JsonProperty("DequeueCounter")
	private long dequeueCounter;
	
	@JsonProperty("DispatchedCounter")
	private long dispatchedCounter;

	public boolean isRetroactive() {
		return retroactive;
	}

	public void setRetroactive(boolean retroactive) {
		this.retroactive = retroactive;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public long getMaximumPendingMessageLimit() {
		return maximumPendingMessageLimit;
	}

	public void setMaximumPendingMessageLimit(long maximumPendingMessageLimit) {
		this.maximumPendingMessageLimit = maximumPendingMessageLimit;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public long getEnqueueCounter() {
		return enqueueCounter;
	}

	public void setEnqueueCounter(long enqueueCounter) {
		this.enqueueCounter = enqueueCounter;
	}

	public long getSelector() {
		return selector;
	}

	public void setSelector(long selector) {
		this.selector = selector;
	}

	public long getPrefetchSize() {
		return prefetchSize;
	}

	public void setPrefetchSize(long prefetchSize) {
		this.prefetchSize = prefetchSize;
	}

	public long getDequeueCounter() {
		return dequeueCounter;
	}

	public void setDequeueCounter(long dequeueCounter) {
		this.dequeueCounter = dequeueCounter;
	}

	public long getDispatchedCounter() {
		return dispatchedCounter;
	}

	public void setDispatchedCounter(long dispatchedCounter) {
		this.dispatchedCounter = dispatchedCounter;
	}
	
	
	

}
