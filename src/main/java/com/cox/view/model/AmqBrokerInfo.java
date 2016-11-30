package com.cox.view.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AmqBrokerInfo {
	
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("BrokerVersion")
	private String brokerVersion;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("Uptime")
	private String  uptime;
	
	@JsonProperty("StorePercentUsage")
	private int storePercentUsage;
	
	@JsonProperty("MemoryPercentUsage")
	private int memoryPercentUsage;
	
	@JsonProperty("TempPercentUsage")
	private int tempPercentUsage;
	
	@JsonProperty("Queues")
	private List<ObjectNames> queueObjNameList;
	
	@JsonProperty("Topics")
	private List<ObjectNames> topicObjNameList;
	
	private String objName;
	
	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getBrokerVersion() {
		return brokerVersion;
	}

	public void setBrokerVersion(String brokerVersion) {
		this.brokerVersion = brokerVersion;
	}

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public int getStorePercentUsage() {
		return storePercentUsage;
	}

	public void setStorePercentUsage(int storePercentUsage) {
		this.storePercentUsage = storePercentUsage;
	}

	public int getMemoryPercentUsage() {
		return memoryPercentUsage;
	}

	public void setMemoryPercentUsage(int memoryPercentUsage) {
		this.memoryPercentUsage = memoryPercentUsage;
	}

	public int getTempPercentUsage() {
		return tempPercentUsage;
	}

	public void setTempPercentUsage(int tempPercentUsage) {
		this.tempPercentUsage = tempPercentUsage;
	}

	public List<ObjectNames> getQueueObjNameList() {
		return queueObjNameList;
	}

	public void setQueueObjNameList(List<ObjectNames> queueObjNameList) {
		this.queueObjNameList = queueObjNameList;
	}

	public List<ObjectNames> getTopicObjNameList() {
		return topicObjNameList;
	}

	public void setTopicObjNameList(List<ObjectNames> topicObjNameList) {
		this.topicObjNameList = topicObjNameList;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

}
