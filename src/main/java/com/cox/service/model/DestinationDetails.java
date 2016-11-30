package com.cox.service.model;

import java.util.List;

import com.cox.view.model.ObjectNames;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationDetails implements Comparable<DestinationDetails>{
	
	@JsonProperty("Name")
	private String name;
	@JsonProperty("QueueSize")
	private long queueSize;
	@JsonProperty("EnqueueCount")
	private long enqueueCount;
	@JsonProperty("DequeueCount")
	private long dequeueCount;
	@JsonProperty("ProducerCount")
	private long producerCount;
	@JsonProperty("ConsumerCount")
	private long consumerCount;
	private String objName;
	@JsonProperty("Subscriptions")
	private List<ObjectNames> subscriptions;
	
	
	public List<ObjectNames> getSubscriptions() {
		return subscriptions;
	}
	public void setSubscriptions(List<ObjectNames> subscriptions) {
		this.subscriptions = subscriptions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getQueueSize() {
		return queueSize;
	}
	public void setQueueSize(long queueSize) {
		this.queueSize = queueSize;
	}
	public long getEnqueueCount() {
		return enqueueCount;
	}
	public void setEnqueueCount(long enqueueCount) {
		this.enqueueCount = enqueueCount;
	}
	public long getDequeueCount() {
		return dequeueCount;
	}
	public void setDequeueCount(long dequeueCount) {
		this.dequeueCount = dequeueCount;
	}
	public long getProducerCount() {
		return producerCount;
	}
	public void setProducerCount(long producerCount) {
		this.producerCount = producerCount;
	}
	public long getConsumerCount() {
		return consumerCount;
	}
	public void setConsumerCount(long consumerCount) {
		this.consumerCount = consumerCount;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	@Override
	public int compareTo(DestinationDetails o) {
		return this.name.compareTo(o.name);
	}
}
