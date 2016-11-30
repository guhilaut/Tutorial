package com.cox.service;

import java.util.List;

import com.cox.exception.ServiceException;
import com.cox.service.model.JobBean;
import com.cox.service.model.SchedulerInfoBean;
import com.cox.service.model.TriggerBean;
import com.cox.service.model.TriggerState;

public interface QuartzService {

	public List<String> getSchedulerNameList() throws ServiceException;
	
	public SchedulerInfoBean getSchedulerInfo(String objName) throws ServiceException;
	
	public TriggerBean getAllTriggers(String objName) throws ServiceException;
	
	public JobBean getAllJobDetails(String objName) throws ServiceException;
	
	public void getJobGroupNames(String objName) throws ServiceException;
	
	public void getSchedulerName(String objName) throws ServiceException;
	
	public void getThreadPoolSize(String objName) throws ServiceException;
	
	public void getTriggerGroupNames(String objName) throws ServiceException;
	
	public void pauseJob(String objName, String jobName, String groupName) throws ServiceException;
	
	public void resumeJob(String objName, String jobName, String groupName) throws ServiceException;
	
	public TriggerState pauseTrigger(String objName, String triggerName, String triggerGroupName)  throws ServiceException;
	
	public TriggerState resumeTrigger(String objName, String triggerName, String triggerGroupName)  throws ServiceException;

	/*public void updateTrigger(String objName, String groupName, String timerName,
			Map<String, Object> timerMap)throws ServiceException;*/

	public boolean playOrPauseScheduler(String objName, String status) throws ServiceException;

	public void updateTrigger(String objName, String jobName, String groupName,
			String misfireInstruction, String cronExpression) throws ServiceException;
	
}
