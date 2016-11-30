package com.cox.service.model;

import java.util.Date;

public class TriggerDetail {
	
	private String state;
	private String jobName;
	private String fireInstanceId;
	private int misfireInstruction;
	private Date nextFireTime;
	private String description;
	private String jobGroup;
	private int priority;
	private Object jobDataMap;
	private Date finalFireTime;
	private String calendarName;
	private String name;
	private Date previousFireTime;
	private Date startTime;
	private Date endTime;
	private String group;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getFireInstanceId() {
		return fireInstanceId;
	}
	public void setFireInstanceId(String fireInstanceId) {
		this.fireInstanceId = fireInstanceId;
	}
	public int getMisfireInstruction() {
		return misfireInstruction;
	}
	public void setMisfireInstruction(int misfireInstruction) {
		this.misfireInstruction = misfireInstruction;
	}
	public Date getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getFinalFireTime() {
		return finalFireTime;
	}
	public void setFinalFireTime(Date finalFireTime) {
		this.finalFireTime = finalFireTime;
	}
	public String getCalendarName() {
		return calendarName;
	}
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getPreviousFireTime() {
		return previousFireTime;
	}
	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Object getJobDataMap() {
		return jobDataMap;
	}
	public void setJobDataMap(Object jobDataMap) {
		this.jobDataMap = jobDataMap;
	}
	
}
