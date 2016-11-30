package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobDetail {
	
	@JsonProperty("CamelQuartzTriggerCronExpression")
	private String cronExpression;
	@JsonProperty("CamelQuartzEndpoint")
	private String camelQuartzEndpoint;
	@JsonProperty("CamelQuartzTriggerType")
	private String triggerType;
	@JsonProperty("CamelQuartzCamelContextName")
	private String contextName;
	
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getCamelQuartzEndpoint() {
		return camelQuartzEndpoint;
	}
	public void setCamelQuartzEndpoint(String camelQuartzEndpoint) {
		this.camelQuartzEndpoint = camelQuartzEndpoint;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public String getContextName() {
		return contextName;
	}
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

}
