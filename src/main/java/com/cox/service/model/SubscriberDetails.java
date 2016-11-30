package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberDetails {
	
	@JsonProperty("Retroactive")
	private boolean retroactive;
	@JsonProperty("CursorMemoryUsage")
	private long cursorMemoryUsage;
	@JsonProperty("Exclusive")
	private boolean exclusive;
	@JsonProperty("SlowConsumer")
	private boolean slowConsumer;
	@JsonProperty("DestinationTemporary")
	private boolean destinationTemporary;
	@JsonProperty("ConnectionId")
	private String connectionId;
	@JsonProperty("MaximumPendingMessageLimit")
	private long maximumPendingMessageLimit;
	@JsonProperty("SubscriptionId")
	private long subscriptionId;
	@JsonProperty("Network")
	private boolean network;
	@JsonProperty("DestinationName")
	private String destinationName;
	@JsonProperty("ClientId")
	private String clientId;
	@JsonProperty("DestinationQueue")
	private boolean destinationQueue;
	@JsonProperty("SessionId")
	private long sessionId;
	@JsonProperty("SubcriptionId")
	private long subcriptionId;
	@JsonProperty("EnqueueCounter")
	private long enqueueCounter;
	@JsonProperty("Selector")
	private String selector;
	@JsonProperty("DestinationTopic")
	private boolean destinationTopic;
	@JsonProperty("SubcriptionName")
	private String subcriptionName;
	@JsonProperty("UserName")
	private Object userName;
	@JsonProperty("CursorFull")
	private boolean cursorFull;
	@JsonProperty("Priority")
	private long priority;
	@JsonProperty("DispatchedQueueSize")
	private long dispatchedQueueSize;
	@JsonProperty("Connection")
	private Object connection;
	@JsonProperty("PendingQueueSize") 
	private long pendingQueueSize;
	@JsonProperty("MessageCountAwaitingAcknowledge")
	private long messageCountAwaitingAcknowledge;
	@JsonProperty("SubscriptionName")
	private String subscriptionName;
	@JsonProperty("PrefetchSize")
	private long prefetchSize;
	@JsonProperty("CursorPercentUsage")
	private long cursorPercentUsage;
	@JsonProperty("Active")
	private boolean active;
	@JsonProperty("NoLocal")
	private boolean noLocal;
	@JsonProperty("Durable")
	private boolean durable;
	@JsonProperty("DequeueCounter")
	private long dequeueCounter;
	@JsonProperty("ConsumedCount")
	private long consumedCount;
	@JsonProperty("DispatchedCounter")
	private long dispatchedCounter;
	
	private String objectName;
	
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public boolean isRetroactive() {
		return retroactive;
	}
	public void setRetroactive(boolean retroactive) {
		this.retroactive = retroactive;
	}
	public long getCursorMemoryUsage() {
		return cursorMemoryUsage;
	}
	public void setCursorMemoryUsage(long cursorMemoryUsage) {
		this.cursorMemoryUsage = cursorMemoryUsage;
	}
	public boolean isExclusive() {
		return exclusive;
	}
	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}
	public boolean isSlowConsumer() {
		return slowConsumer;
	}
	public void setSlowConsumer(boolean slowConsumer) {
		this.slowConsumer = slowConsumer;
	}
	public boolean isDestinationTemporary() {
		return destinationTemporary;
	}
	public void setDestinationTemporary(boolean destinationTemporary) {
		this.destinationTemporary = destinationTemporary;
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
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
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
	public boolean isDestinationQueue() {
		return destinationQueue;
	}
	public void setDestinationQueue(boolean destinationQueue) {
		this.destinationQueue = destinationQueue;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public long getSubcriptionId() {
		return subcriptionId;
	}
	public void setSubcriptionId(long subcriptionId) {
		this.subcriptionId = subcriptionId;
	}
	public long getEnqueueCounter() {
		return enqueueCounter;
	}
	public void setEnqueueCounter(long enqueueCounter) {
		this.enqueueCounter = enqueueCounter;
	}
	public String getSelector() {
		return selector;
	}
	public void setSelector(String selector) {
		this.selector = selector;
	}
	public boolean isDestinationTopic() {
		return destinationTopic;
	}
	public void setDestinationTopic(boolean destinationTopic) {
		this.destinationTopic = destinationTopic;
	}
	public String getSubcriptionName() {
		return subcriptionName;
	}
	public void setSubcriptionName(String subcriptionName) {
		this.subcriptionName = subcriptionName;
	}
	public Object getUserName() {
		return userName;
	}
	public void setUserName(Object userName) {
		this.userName = userName;
	}
	public boolean isCursorFull() {
		return cursorFull;
	}
	public void setCursorFull(boolean cursorFull) {
		this.cursorFull = cursorFull;
	}
	public long getPriority() {
		return priority;
	}
	public void setPriority(long priority) {
		this.priority = priority;
	}
	public long getDispatchedQueueSize() {
		return dispatchedQueueSize;
	}
	public void setDispatchedQueueSize(long dispatchedQueueSize) {
		this.dispatchedQueueSize = dispatchedQueueSize;
	}
	public Object getConnection() {
		return connection;
	}
	public void setConnection(Object connection) {
		this.connection = connection;
	}
	public long getPendingQueueSize() {
		return pendingQueueSize;
	}
	public void setPendingQueueSize(long pendingQueueSize) {
		this.pendingQueueSize = pendingQueueSize;
	}
	public long getMessageCountAwaitingAcknowledge() {
		return messageCountAwaitingAcknowledge;
	}
	public void setMessageCountAwaitingAcknowledge(
			long messageCountAwaitingAcknowledge) {
		this.messageCountAwaitingAcknowledge = messageCountAwaitingAcknowledge;
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
	public long getCursorPercentUsage() {
		return cursorPercentUsage;
	}
	public void setCursorPercentUsage(long cursorPercentUsage) {
		this.cursorPercentUsage = cursorPercentUsage;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isNoLocal() {
		return noLocal;
	}
	public void setNoLocal(boolean noLocal) {
		this.noLocal = noLocal;
	}
	public boolean isDurable() {
		return durable;
	}
	public void setDurable(boolean durable) {
		this.durable = durable;
	}
	public long getDequeueCounter() {
		return dequeueCounter;
	}
	public void setDequeueCounter(long dequeueCounter) {
		this.dequeueCounter = dequeueCounter;
	}
	public long getConsumedCount() {
		return consumedCount;
	}
	public void setConsumedCount(long consumedCount) {
		this.consumedCount = consumedCount;
	}
	public long getDispatchedCounter() {
		return dispatchedCounter;
	}
	public void setDispatchedCounter(long dispatchedCounter) {
		this.dispatchedCounter = dispatchedCounter;
	}
}
