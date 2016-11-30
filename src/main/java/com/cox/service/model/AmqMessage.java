package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmqMessage {
	
	@JsonProperty("JMSType")
	private String jMSType;
	
	@JsonProperty("JMSMessageID")
	private String jMSMessageID;
	
	@JsonProperty("JMSExpiration")
	private long jMSExpiration;
	
	@JsonProperty("OriginalDestination")
	private String originalDestination;
	
	@JsonProperty("JMSRedelivered")
	private boolean jMSRedelivered;
	
	@JsonProperty("JMSTimestamp")
	private String jMSTimestamp;
	
	
    @JsonProperty("JMSXUserID")
	private String jMSXUserID;
	
	@JsonProperty("JMSXGroupSeq")
	private int jMSXGroupSeq;
	
	@JsonProperty("JMSDeliveryMode")
	private String jMSDeliveryMode;
    
    @JsonProperty("JMSPriority")
	private int jMSPriority;
	
	@JsonProperty("JMSReplyTo")
	private String jMSReplyTo;
	
	@JsonProperty("JMSXGroupID")
	private String jMSXGroupID;
	
	@JsonProperty("JMSDestination")
	private String jMSDestination;
	
	@JsonProperty("BrokerPath")
	private String brokerPath;
	
	@JsonProperty("JMSCorrelationID")
	private String jMSCorrelationID;
	
	@JsonProperty("PropertiesText")
	private Object propertiesTextMap;
	
	@JsonProperty("IntProperties")
	private Object intProperties;
	
	@JsonProperty("StringProperties")
	private Object stringProperties;
	
	@JsonProperty("ShortProperties")
	private Object shortProperties;
	
	@JsonProperty("ByteProperties")
	private Object byteProperties;
	
	@JsonProperty("LongProperties")
	private Object longProperties;
	
	@JsonProperty("DoubleProperties")
	private Object doubleProperties;
	
	@JsonProperty("BooleanProperties")
	private Object booleanProperties;
	
	@JsonProperty("FloatProperties")
	private Object floatProperties;

	public Object getFloatProperties() {
		return floatProperties;
	}

	public void setFloatProperties(Object floatProperties) {
		this.floatProperties = floatProperties;
	}

	public Object getBooleanProperties() {
		return booleanProperties;
	}

	public void setBooleanProperties(Object booleanProperties) {
		this.booleanProperties = booleanProperties;
	}

	public Object getLongProperties() {
		return longProperties;
	}

	public Object getDoubleProperties() {
		return doubleProperties;
	}

	public void setDoubleProperties(Object doubleProperties) {
		this.doubleProperties = doubleProperties;
	}

	public void setLongProperties(Object longProperties) {
		this.longProperties = longProperties;
	}

	public Object getByteProperties() {
		return byteProperties;
	}

	public void setByteProperties(Object byteProperties) {
		this.byteProperties = byteProperties;
	}

	public Object getStringProperties() {
		return stringProperties;
	}

	public Object getShortProperties() {
		return shortProperties;
	}

	public void setShortProperties(Object shortProperties) {
		this.shortProperties = shortProperties;
	}

	public void setStringProperties(Object stringProperties) {
		this.stringProperties = stringProperties;
	}

	public String getjMSType() {
		return jMSType;
	}

	public void setjMSType(String jMSType) {
		this.jMSType = jMSType;
	}

	public String getjMSMessageID() {
		return jMSMessageID;
	}

	public void setjMSMessageID(String jMSMessageID) {
		this.jMSMessageID = jMSMessageID;
	}

	
	public long getjMSExpiration() {
		return jMSExpiration;
	}

	public void setjMSExpiration(long jMSExpiration) {
		this.jMSExpiration = jMSExpiration;
	}

	public String getOriginalDestination() {
		return originalDestination;
	}

	public void setOriginalDestination(String originalDestination) {
		this.originalDestination = originalDestination;
	}

	public boolean isjMSRedelivered() {
		return jMSRedelivered;
	}

	public void setjMSRedelivered(boolean jMSRedelivered) {
		this.jMSRedelivered = jMSRedelivered;
	}

	public String getjMSTimestamp() {
		return jMSTimestamp;
	}

	public void setjMSTimestamp(String jMSTimestamp) {
		this.jMSTimestamp = jMSTimestamp;
	}

	public String getjMSXUserID() {
		return jMSXUserID;
	}

	public void setjMSXUserID(String jMSXUserID) {
		this.jMSXUserID = jMSXUserID;
	}

	public int getjMSXGroupSeq() {
		return jMSXGroupSeq;
	}

	public void setjMSXGroupSeq(int jMSXGroupSeq) {
		this.jMSXGroupSeq = jMSXGroupSeq;
	}

	public String getjMSDeliveryMode() {
		return jMSDeliveryMode;
	}

	public void setjMSDeliveryMode(String jMSDeliveryMode) {
		this.jMSDeliveryMode = jMSDeliveryMode;
	}

	public int getjMSPriority() {
		return jMSPriority;
	}

	public void setjMSPriority(int jMSPriority) {
		this.jMSPriority = jMSPriority;
	}

	public String getjMSReplyTo() {
		return jMSReplyTo;
	}

	public void setjMSReplyTo(String jMSReplyTo) {
		this.jMSReplyTo = jMSReplyTo;
	}

	public String getjMSXGroupID() {
		return jMSXGroupID;
	}

	public void setjMSXGroupID(String jMSXGroupID) {
		this.jMSXGroupID = jMSXGroupID;
	}

	public String getjMSDestination() {
		return jMSDestination;
	}

	public void setjMSDestination(String jMSDestination) {
		this.jMSDestination = jMSDestination;
	}

	public String getBrokerPath() {
		return brokerPath;
	}

	public void setBrokerPath(String brokerPath) {
		this.brokerPath = brokerPath;
	}

	public String getjMSCorrelationID() {
		return jMSCorrelationID;
	}

	public void setjMSCorrelationID(String jMSCorrelationID) {
		this.jMSCorrelationID = jMSCorrelationID;
	}

	public Object getIntProperties() {
		return intProperties;
	}

	public Object getPropertiesTextMap() {
		return propertiesTextMap;
	}

	public void setPropertiesTextMap(Object propertiesTextMap) {
		this.propertiesTextMap = propertiesTextMap;
	}

	public void setIntProperties(Object intProperties) {
		this.intProperties = intProperties;
	}
	
}
