package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupBean {
	
	@JsonProperty("jobDataMap")
	private JobDetail jobBean;
	@JsonProperty("shouldRecover")
	private boolean isRecover;
	@JsonProperty("jobClass")
	private String jobClass;
	private boolean durability;
	private String name;
	private String description;
	private String group;
	
	public JobDetail getJobBean() {
		return jobBean;
	}
	public void setJobBean(JobDetail jobBean) {
		this.jobBean = jobBean;
	}
	public boolean isRecover() {
		return isRecover;
	}
	public void setRecover(boolean isRecover) {
		this.isRecover = isRecover;
	}
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public boolean isDurability() {
		return durability;
	}
	public void setDurability(boolean durability) {
		this.durability = durability;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
}
