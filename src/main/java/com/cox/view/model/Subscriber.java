package com.cox.view.model;


public class Subscriber {

	private String clientId;
	private long sessionId;
	private long subscriptionId;
	private String selector;
	private boolean active;
	private boolean network;
	private long pendingQueueSize;
	// dispatched counter
	private long inflight;
	private long enqueueCounter;
	private long dequeueCounter;
	private long prefetchSize;
	private String subscriptionName;
	private String destinationName;
	private String objectName;
	
	
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public boolean isNetwork() {
		return network;
	}
	public void setNetwork(boolean network) {
		this.network = network;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
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
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getSelector() {
		return selector;
	}
	public void setSelector(String selector) {
		this.selector = selector;
	}
	public long getPendingQueueSize() {
		return pendingQueueSize;
	}
	public void setPendingQueueSize(long pendingQueueSize) {
		this.pendingQueueSize = pendingQueueSize;
	}
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}
	public long getPrefetchSize() {
		return prefetchSize;
	}
	public void setPrefetchSize(long prefetchSize) {
		this.prefetchSize = prefetchSize;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public long getDequeueCounter() {
		return dequeueCounter;
	}
	public void setDequeueCounter(long dequeueCounter) {
		this.dequeueCounter = dequeueCounter;
	}
	public long getInflight() {
		return inflight;
	}
	public void setInflight(long inflight) {
		this.inflight = inflight;
	}
	
	
	

}
