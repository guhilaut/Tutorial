package com.cox.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.management.MalformedObjectNameException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pExecResponse;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.jolokia.client.request.J4pSearchRequest;
import org.jolokia.client.request.J4pSearchResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.exception.ServiceException;
import com.cox.service.QuartzService;
import com.cox.service.model.JobBean;
import com.cox.service.model.QuartzSchedulerBean;
import com.cox.service.model.SchedulerInfoBean;
import com.cox.service.model.TriggerBean;
import com.cox.service.model.TriggerDetail;
import com.cox.service.model.TriggerState;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuartzServiceImpl implements QuartzService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzServiceImpl.class);
	
	@Autowired
	private Configuration configuration;
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public List<String> getSchedulerNameList() throws ServiceException{
		try{
			String type = "quartz:type=QuartzScheduler,*";
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pSearchRequest request = new J4pSearchRequest(type);
			J4pSearchResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("quartz mbeans names >>>> "+obj);
			}
			QuartzSchedulerBean quartzBean = mapper.convertValue(obj, QuartzSchedulerBean.class);
			return quartzBean.getSchedulerName();
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerNameList :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerNameList :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getSchedulerNameList :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public TriggerBean getAllTriggers(String objName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName, "AllTriggers");
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("AllTriggers :: "+obj);
			}
			TriggerBean triggerBean = mapper.convertValue(obj, TriggerBean.class);
			//call to get all job details
			JobBean jobBean = getAllJobDetails(objName);
			Map<String, Map<String, Map<String, Object>>> valueBean = jobBean.getTimerBean();
			for(TriggerDetail triggerDetail : triggerBean.getTriggerDetailList()){
				String jobName = triggerDetail.getJobName();
				String groupName = triggerDetail.getJobGroup();
				TriggerState triggerState = getTriggerState(objName, jobName, groupName);
				triggerDetail.setState(triggerState.getTriggerState());
				Map<String, Map<String, Object>> timerMap = valueBean.get(jobName);
				Map<String, Object> groupMap = timerMap.get(groupName);
				Object jobDataMap = groupMap.get("jobDataMap");
				triggerDetail.setJobDataMap(jobDataMap);
			}
			
			return triggerBean;
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getAllTriggers :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getAllTriggers :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getAllTriggers :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public JobBean getAllJobDetails(String objName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName, "AllJobDetails");
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("AllJobDetails :: "+obj);
			}
			JobBean jobBean = mapper.convertValue(obj, JobBean.class);
			return jobBean;
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getAllJobDetails :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getAllJobDetails :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getAllJobDetails :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public void getJobGroupNames(String objName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName, "JobGroupNames");
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("JobGroupNames :: "+obj);
			}
			
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getJobGroupNames :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getJobGroupNames :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getJobGroupNames :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public void getSchedulerName(String objName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName, "SchedulerName");
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SchedulerName :: "+obj);
			}
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerName :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerName :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getSchedulerName :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public void getThreadPoolSize(String objName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName, "ThreadPoolSize");
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("ThreadPoolSize :: "+obj);
			}
			
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getThreadPoolSize :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getThreadPoolSize :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getThreadPoolSize :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public void getTriggerGroupNames(String objName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName, "TriggerGroupNames");
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("TriggerGroupNames :: "+obj);
			}
			
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getTriggerGroupNames :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getTriggerGroupNames :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getTriggerGroupNames :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public void pauseJob(String objName, String jobName, String groupName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest(objName, "pauseJob", jobName, groupName);
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("pauseJob :: "+obj);
			}
			
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: pauseJob :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: pauseJob :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: pauseJob :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	} 
	
	public void resumeJob(String objName, String jobName, String groupName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest(objName, "resumeJob", jobName, groupName);
			J4pExecResponse response = client.execute(request);
			request = new J4pExecRequest(objName, "getTriggerState", jobName, groupName);
			response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("resumeJob :: "+obj);
			}
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: resumeJob :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: resumeJob :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: resumeJob :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	} 
	
	public TriggerState pauseTrigger(String objName, String triggerName, String triggerGroupName)  throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest(objName, "pauseTrigger", triggerName, triggerGroupName);
			J4pExecResponse response = client.execute(request);
			request = new J4pExecRequest(objName, "getTriggerState", triggerName, triggerGroupName);
			response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("pauseTrigger :: "+obj);
			}
			TriggerState triggerState = mapper.convertValue(obj, TriggerState.class);
			return triggerState;
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: pauseTrigger :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: pauseTrigger :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: pauseTrigger :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public TriggerState getTriggerState(String objName, String triggerName, String triggerGroupName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest(objName, "getTriggerState", triggerName, triggerGroupName);
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("getTriggerState :: "+obj);
			}
			TriggerState triggerState = mapper.convertValue(obj, TriggerState.class);
			return triggerState;
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: pauseTrigger :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: pauseTrigger :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: pauseTrigger :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public TriggerState resumeTrigger(String objName, String triggerName, String triggerGroupName)  throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest(objName, "resumeTrigger", triggerName, triggerGroupName);
			J4pExecResponse response = client.execute(request);
			request = new J4pExecRequest(objName, "getTriggerState", triggerName, triggerGroupName);
			response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("resumeTrigger :: "+obj);
			}
			TriggerState triggerState = mapper.convertValue(obj, TriggerState.class);
			return triggerState;
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: resumeTrigger :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: resumeTrigger :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: resumeTrigger :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public SchedulerInfoBean getSchedulerInfo(String objName) throws ServiceException{
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName, "SchedulerName", "SchedulerInstanceId", "Version", "Started", "SampledStatisticsEnabled", "JobStoreClassName", "ThreadPoolClassName", "ThreadPoolSize");
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("getSchedulerInfo :: "+obj);
			}
			SchedulerInfoBean schedulerInfoBean = mapper.convertValue(obj, SchedulerInfoBean.class);
			return schedulerInfoBean;
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerInfo :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerInfo :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getSchedulerInfo :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	/*@Override
	public void updateTrigger(String objName, String groupName, String timerName, Map<String, Object> timerMap) throws ServiceException {
		
		try{
//			List<Map<String, Map<String, Object>>> timerList = new ArrayList(timerMap.values());
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			JSONObject data =  new JSONObject(timerMap);
			System.out.println("jobDetailClass >>>> "+timerMap.get("jobDetailClass"));
			System.out.println("update cron job data >>>>>>>  "+data);
			J4pExecRequest request = new J4pExecRequest(objName, "addJob(java.util.Map,boolean)", data, true);
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			LOGGER.info("getSchedulerInfo :: "+obj);
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerInfo :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: getSchedulerInfo :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getSchedulerInfo :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}*/

	@Override
	public boolean playOrPauseScheduler(String objName, String status) throws ServiceException {
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = null;
			if("Started".equalsIgnoreCase(status)){
				request = new J4pExecRequest(objName, "standby");
			}else if("Paused".equalsIgnoreCase(status)){
				request = new J4pExecRequest(objName, "start");
			}else{
				throw new ServiceException("Invalid status of scheduler found!!!");
			}
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("playOrPauseScheduler :: "+obj);
			}
			return true;
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: playOrPauseScheduler :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: playOrPauseScheduler :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: playOrPauseScheduler :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	@Override
	public void updateTrigger(String objName, String jobName, String groupName,
			String misfireInstruction, String cronExpression)
			throws ServiceException {
		
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest("hawtio:type=QuartzFacade", "updateCronTrigger", objName, jobName, groupName, misfireInstruction, cronExpression, null);
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("updateTrigger :: "+obj);
			}
		}catch(MalformedObjectNameException ex){
			LOGGER.error(getClass().getName()+" :: updateTrigger :::: error", ex);
			throw new ServiceException("Malformed Url Error", ex);
		}catch(J4pException ex){
			LOGGER.error(getClass().getName()+" :: updateTrigger :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: updateTrigger :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
		
	}
}
