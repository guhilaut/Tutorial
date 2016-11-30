package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchedulerInfo {
	
	@JsonProperty("SchedulerName")
	private String schedulerName;
	@JsonProperty("SchedulerInstanceId")
	private String schedulerInstance;
	@JsonProperty("Version")
	private String version;
	@JsonProperty("Started")
	private boolean status;
	@JsonProperty("SampledStatisticsEnabled")
	private boolean sampleStatisticsEnabled;
	@JsonProperty("JobStoreClassName")
	private String jobStoreClassName;
	@JsonProperty("ThreadPoolClassName")
	private String threadPoolClassName;
	@JsonProperty("ThreadPoolSize")
	private String threadPoolSize;
	
	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	public String getSchedulerInstance() {
		return schedulerInstance;
	}
	public void setSchedulerInstance(String schedulerInstance) {
		this.schedulerInstance = schedulerInstance;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isSampleStatisticsEnabled() {
		return sampleStatisticsEnabled;
	}
	public void setSampleStatisticsEnabled(boolean sampleStatisticsEnabled) {
		this.sampleStatisticsEnabled = sampleStatisticsEnabled;
	}
	public String getJobStoreClassName() {
		return jobStoreClassName;
	}
	public void setJobStoreClassName(String jobStoreClassName) {
		this.jobStoreClassName = jobStoreClassName;
	}
	public String getThreadPoolClassName() {
		return threadPoolClassName;
	}
	public void setThreadPoolClassName(String threadPoolClassName) {
		this.threadPoolClassName = threadPoolClassName;
	}
	public String getThreadPoolSize() {
		return threadPoolSize;
	}
	public void setThreadPoolSize(String threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
}
