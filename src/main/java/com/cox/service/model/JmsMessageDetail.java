package com.cox.service.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JmsMessageDetail {

	@JsonProperty("BooleanProperties")
	private Map<String, Boolean> booleanProperties;
	
	@JsonProperty("BrokerPath")
	private String brokerPath;
	
	@JsonProperty("ByteProperties")
	private Map<String, Byte> byteProperties;
	
	@JsonProperty("DoubleProperties")
	private Map<String, Double> doubleProperties;
	
	@JsonProperty("FloatProperties")
	private Map<String, Float> floatProperties;
	
	@JsonProperty("IntProperties")
	private Map<String, Integer> intProperties;
	
	@JsonProperty("JMSCorrelationID")
	private String jmsCorrelationId;
	
	@JsonProperty("JMSDeliveryMode")
	private String jmsDeliveryMode;
	
	@JsonProperty("JMSDestination")
	private String jmsDestination;
	
	@JsonProperty("JMSExpiration")
	private long jmsExpiration;
		
	@JsonProperty("JMSMessageID")
	private String jmsMessageId;
	
	@JsonProperty("JMSPriority")
	private long jmsPriority;
	
	@JsonProperty("JMSRedelivered")
	private boolean jmsRedelivered;
	
	@JsonProperty("JMSReplyTo")
	private String jmsReplyTo;
	
	@JsonProperty("JMSTimestamp")
	private String jmsTimestamp;
	
	@JsonProperty("JMSType")
	private String jmsType;
	
	@JsonProperty("JMSXGroupID")
	private String jmsXGroupId;
	
	@JsonProperty("JMSXGroupSeq")
	private long jmsXGroupSeq;
	
	@JsonProperty("JMSXUserID")
	private String jmsXUserId;
	
	@JsonProperty("LongProperties")
	private Map<String, Long> longProperties;
	
	@JsonProperty("OriginalDestination")
	private String originalDestination;
	
	@JsonProperty("PropertiesText")
	private Object propertiesText;
	
	@JsonProperty("ShortProperties")
	private Map<String, Short> shortProperties;
	
	@JsonProperty("StringProperties")
	private Map<String, String> stringProperties;
	
	@JsonProperty("Text")
	private String message;

	public Map<String, Boolean> getBooleanProperties() {
		return booleanProperties;
	}

	public void setBooleanProperties(Map<String, Boolean> booleanProperties) {
		this.booleanProperties = booleanProperties;
	}

	public String getBrokerPath() {
		return brokerPath;
	}

	public void setBrokerPath(String brokerPath) {
		this.brokerPath = brokerPath;
	}

	public Map<String, Byte> getByteProperties() {
		return byteProperties;
	}

	public void setByteProperties(Map<String, Byte> byteProperties) {
		this.byteProperties = byteProperties;
	}

	public Map<String, Double> getDoubleProperties() {
		return doubleProperties;
	}

	public void setDoubleProperties(Map<String, Double> doubleProperties) {
		this.doubleProperties = doubleProperties;
	}

	public Map<String, Float> getFloatProperties() {
		return floatProperties;
	}

	public void setFloatProperties(Map<String, Float> floatProperties) {
		this.floatProperties = floatProperties;
	}

	public Map<String, Integer> getIntProperties() {
		return intProperties;
	}

	public void setIntProperties(Map<String, Integer> intProperties) {
		this.intProperties = intProperties;
	}

	public String getJmsCorrelationId() {
		return jmsCorrelationId;
	}

	public void setJmsCorrelationId(String jmsCorrelationId) {
		this.jmsCorrelationId = jmsCorrelationId;
	}

	public String getJmsDeliveryMode() {
		return jmsDeliveryMode;
	}

	public void setJmsDeliveryMode(String jmsDeliveryMode) {
		this.jmsDeliveryMode = jmsDeliveryMode;
	}

	public String getJmsDestination() {
		return jmsDestination;
	}

	public void setJmsDestination(String jmsDestination) {
		this.jmsDestination = jmsDestination;
	}

	public long getJmsExpiration() {
		return jmsExpiration;
	}

	public void setJmsExpiration(long jmsExpiration) {
		this.jmsExpiration = jmsExpiration;
	}

	public String getJmsMessageId() {
		return jmsMessageId;
	}

	public void setJmsMessageId(String jmsMessageId) {
		this.jmsMessageId = jmsMessageId;
	}

	public long getJmsPriority() {
		return jmsPriority;
	}

	public void setJmsPriority(long jmsPriority) {
		this.jmsPriority = jmsPriority;
	}

	public boolean isJmsRedelivered() {
		return jmsRedelivered;
	}

	public void setJmsRedelivered(boolean jmsRedelivered) {
		this.jmsRedelivered = jmsRedelivered;
	}

	public String getJmsReplyTo() {
		return jmsReplyTo;
	}

	public void setJmsReplyTo(String jmsReplyTo) {
		this.jmsReplyTo = jmsReplyTo;
	}

	public String getJmsTimestamp() {
		return jmsTimestamp;
	}

	public void setJmsTimestamp(String jmsTimestamp) {
		this.jmsTimestamp = jmsTimestamp;
	}

	public String getJmsType() {
		return jmsType;
	}

	public void setJmsType(String jmsType) {
		this.jmsType = jmsType;
	}

	public String getJmsXGroupId() {
		return jmsXGroupId;
	}

	public void setJmsXGroupId(String jmsXGroupId) {
		this.jmsXGroupId = jmsXGroupId;
	}

	public long getJmsXGroupSeq() {
		return jmsXGroupSeq;
	}

	public void setJmsXGroupSeq(long jmsXGroupSeq) {
		this.jmsXGroupSeq = jmsXGroupSeq;
	}

	public String getJmsXUserId() {
		return jmsXUserId;
	}

	public void setJmsXUserId(String jmsXUserId) {
		this.jmsXUserId = jmsXUserId;
	}

	public Map<String, Long> getLongProperties() {
		return longProperties;
	}

	public void setLongProperties(Map<String, Long> longProperties) {
		this.longProperties = longProperties;
	}

	public String getOriginalDestination() {
		return originalDestination;
	}

	public void setOriginalDestination(String originalDestination) {
		this.originalDestination = originalDestination;
	}

	public Map<String, Short> getShortProperties() {
		return shortProperties;
	}

	public Object getPropertiesText() {
		return propertiesText;
	}

	public void setPropertiesText(Object propertiesText) {
		this.propertiesText = propertiesText;
	}

	public void setShortProperties(Map<String, Short> shortProperties) {
		this.shortProperties = shortProperties;
	}

	public Map<String, String> getStringProperties() {
		return stringProperties;
	}

	public void setStringProperties(Map<String, String> stringProperties) {
		this.stringProperties = stringProperties;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
