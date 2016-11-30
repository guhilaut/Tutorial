package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Queues {

	
	@JsonProperty("objectName")
	private String objectName;


	@JsonProperty("propertyPattern")
	private boolean propertyPattern;
	
	@JsonProperty("domainPattern")
	private boolean domainPattern;
	
	
	@JsonProperty("keyPropertyList")
	private KeyPropertyList keyPropertyList;
	
	@JsonProperty("domain")
	private String domain;
	
	@JsonProperty("pattern")
	private boolean pattern;
	
	@JsonProperty("propertyValuePattern")
	private boolean propertyValuePattern;
	
	@JsonProperty("canonicalKeyPropertyListString")
	private String canonicalKeyPropertyListString;
	
	
	@JsonProperty("propertyListPattern")
	private boolean propertyListPattern;
	
	@JsonProperty("keyPropertyListString")
	private String keyPropertyListString;
	
	@JsonProperty("canonicalName")
	private String canonicalName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public boolean isPropertyPattern() {
		return propertyPattern;
	}

	public void setPropertyPattern(boolean propertyPattern) {
		this.propertyPattern = propertyPattern;
	}

	public boolean isDomainPattern() {
		return domainPattern;
	}

	public void setDomainPattern(boolean domainPattern) {
		this.domainPattern = domainPattern;
	}

	

	public KeyPropertyList getKeyPropertyList() {
		return keyPropertyList;
	}

	public void setKeyPropertyList(KeyPropertyList keyPropertyList) {
		this.keyPropertyList = keyPropertyList;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isPattern() {
		return pattern;
	}

	public void setPattern(boolean pattern) {
		this.pattern = pattern;
	}

	public boolean isPropertyValuePattern() {
		return propertyValuePattern;
	}

	public void setPropertyValuePattern(boolean propertyValuePattern) {
		this.propertyValuePattern = propertyValuePattern;
	}

	public String getCanonicalKeyPropertyListString() {
		return canonicalKeyPropertyListString;
	}

	public void setCanonicalKeyPropertyListString(
			String canonicalKeyPropertyListString) {
		this.canonicalKeyPropertyListString = canonicalKeyPropertyListString;
	}

	public boolean isPropertyListPattern() {
		return propertyListPattern;
	}

	public void setPropertyListPattern(boolean propertyListPattern) {
		this.propertyListPattern = propertyListPattern;
	}

	public String getKeyPropertyListString() {
		return keyPropertyListString;
	}

	public void setKeyPropertyListString(String keyPropertyListString) {
		this.keyPropertyListString = keyPropertyListString;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}
	
	
	
	
}
