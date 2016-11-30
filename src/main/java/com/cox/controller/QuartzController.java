package com.cox.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cox.controller.helper.CommonHelper;
import com.cox.controller.helper.QuartzControllerHelper;
import com.cox.exception.ServiceException;
import com.cox.service.QuartzService;
import com.cox.service.model.JobBean;
import com.cox.service.model.SchedulerInfoBean;
import com.cox.service.model.TriggerBean;
import com.cox.service.model.TriggerState;
import com.cox.view.mapper.ViewVOMapper;
import com.cox.view.model.Category;
import com.cox.view.model.JobBeanResponse;
import com.cox.view.model.SchedulerCountResponse;
import com.cox.view.model.SchedulerInfoResponse;
import com.cox.view.model.SchedulerNameResponse;
import com.cox.view.model.StateResponse;
import com.cox.view.model.TriggerBeanResponse;

@Controller
@RequestMapping(value = "/quartz")
public class QuartzController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzController.class);
	
	@Autowired
	private CommonHelper helper;
	@Autowired
	private QuartzService quartzService;
	@Autowired
	private ViewVOMapper mapper;
	@Autowired
	private QuartzControllerHelper quartzHelper;
	
	@RequestMapping(value="/getSchedulerCount/{url:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<SchedulerCountResponse> getSchedulerCount(@PathVariable String url){
		SchedulerCountResponse schedulerCountResponse = new SchedulerCountResponse();
		try{
			helper.loadConfigService(url);
			Map<String, Integer> schedulerCountMap = quartzHelper.getAllSchedulers();
			if(schedulerCountMap != null && schedulerCountMap.size() > 0){
				schedulerCountResponse.setSchedulerGroup(schedulerCountMap);
				schedulerCountResponse.setFlag(true);
				schedulerCountResponse.setMessage(null);
			}else{
				schedulerCountResponse.setSchedulerGroup(null);
				schedulerCountResponse.setFlag(false);
				schedulerCountResponse.setMessage("Quartz Scheduler Not Found!!!");
			}
		}catch(Exception ex){
			schedulerCountResponse.setSchedulerGroup(null);
			schedulerCountResponse.setFlag(false);
			schedulerCountResponse.setMessage("Quartz Scheduler Not Found!!!");
		}
		return new ResponseEntity<SchedulerCountResponse>(schedulerCountResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getSchedulerList", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<SchedulerNameResponse> getSimpleOrComplexObjList(HttpServletResponse resp) throws IOException{
		SchedulerNameResponse schedulerNameResponse = new SchedulerNameResponse();
		try{
			List<Category> objNameList = quartzHelper.getSchedulerList();
			if(objNameList != null){
				schedulerNameResponse.setSchedulerNameList(objNameList);
				schedulerNameResponse.setMessage(null);
				schedulerNameResponse.setFlag(true);
			}else{
				schedulerNameResponse.setSchedulerNameList(null);
				schedulerNameResponse.setMessage("Quartz Scheduler Not Found!!!");
				schedulerNameResponse.setFlag(false);
			}
		}catch(Exception e){
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<SchedulerNameResponse>(schedulerNameResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getSchedulerDetail/{objName:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<SchedulerInfoResponse> getSchedulerInfo(@PathVariable String objName, HttpServletResponse resp) throws IOException{
		SchedulerInfoResponse schedulerInfoResponse = new SchedulerInfoResponse();
		try{
			long start = System.currentTimeMillis();
			SchedulerInfoBean schedulerInfoBean = quartzService.getSchedulerInfo(objName);
			LOGGER.info("Service - quartzService: getSchedulerInfo: time to process = "+(System.currentTimeMillis() - start));
			if(schedulerInfoBean != null && schedulerInfoBean.getSchedulerInfo() != null){
				schedulerInfoResponse.setFlag(true);
				schedulerInfoResponse.setSchedulerInfo(schedulerInfoBean.getSchedulerInfo());
				schedulerInfoResponse.setMessage(null);
			}else{
				schedulerInfoResponse.setFlag(false);
				schedulerInfoResponse.setSchedulerInfo(null);
				schedulerInfoResponse.setMessage("Scheduler detail not found!!!");
			}
		}catch(Exception ex){
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<SchedulerInfoResponse>(schedulerInfoResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="/playOrPauseScheduler/{objName:.+}/{status}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<StateResponse> playOrPauseScheduler(@PathVariable String objName, @PathVariable String status){
		boolean isChanged = false;
		StateResponse stateResponse = new StateResponse();
		try{
			long start = System.currentTimeMillis();
			isChanged = quartzService.playOrPauseScheduler(objName, status);
			LOGGER.info("Service - quartzService: playOrPauseScheduler: time to process = "+(System.currentTimeMillis() - start));
			if(isChanged){
				if("Started".equalsIgnoreCase(status)){
					stateResponse.setState("Paused");
				}else{
					stateResponse.setState("Started");
				}
				stateResponse.setFlag(true);
			}else{
				stateResponse.setFlag(false);
				stateResponse.setMessage("Scheduler state has not been updated!!!");
			}
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::playOrPauseScheduler", ex);
			stateResponse.setFlag(false);
			stateResponse.setMessage("Scheduler state has not been updated!!!");
		}
		return new ResponseEntity<StateResponse>(stateResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/allTrigger/{objName:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<TriggerBeanResponse> getAllTrigger(@PathVariable String objName, HttpServletResponse resp) throws IOException{
		ResponseEntity<TriggerBeanResponse> response = null;
		TriggerBeanResponse triggerBeanResponse = new TriggerBeanResponse();
		try {
			long start = System.currentTimeMillis();
			TriggerBean triggerBean = quartzService.getAllTriggers(objName);
			LOGGER.info("Service - quartzService: getAllTriggers: time to process = "+(System.currentTimeMillis() - start));
			if(triggerBean != null){
				if(triggerBean.getTriggerDetailList() != null && triggerBean.getTriggerDetailList().size() > 0){
					triggerBeanResponse.setTriggerDetailList(triggerBean.getTriggerDetailList());
					triggerBeanResponse.setFlag(true);
					triggerBeanResponse.setMessage(null);
					response = new ResponseEntity<TriggerBeanResponse>(triggerBeanResponse, HttpStatus.OK);
				}else{
					triggerBeanResponse.setTriggerDetailList(null);
					triggerBeanResponse.setFlag(false);
					triggerBeanResponse.setMessage("Trigger List Not Found!!!");
					response = new ResponseEntity<TriggerBeanResponse>(triggerBeanResponse, HttpStatus.OK);
				}
			}else{
				triggerBeanResponse.setTriggerDetailList(null);
				triggerBeanResponse.setFlag(false);
				triggerBeanResponse.setMessage("Trigger Object Not Found!!!");
				response = new ResponseEntity<TriggerBeanResponse>(triggerBeanResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Error in "+getClass().getName()+"::getAllTrigger", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	@RequestMapping(value="/allJob/{objName:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<JobBeanResponse> getAllJob(@PathVariable String objName){
		JobBeanResponse jobBeanResponse = new JobBeanResponse();
		ResponseEntity<JobBeanResponse> response = null;
		try {
			long start = System.currentTimeMillis();
			JobBean jobBean = quartzService.getAllJobDetails(objName);
			LOGGER.info("Service - quartzService: getAllJobDetails: time to process = "+(System.currentTimeMillis() - start));
			if(jobBean != null){
				Map<String, Map<String, Map<String, Object>>> timerBean = jobBean.getTimerBean();
				if(timerBean != null){
					jobBeanResponse.setTimerBean(timerBean);
					jobBeanResponse.setFlag(true);
					jobBeanResponse.setMessage(null);
					response = new ResponseEntity<JobBeanResponse>(jobBeanResponse, HttpStatus.OK);
				}else{
					jobBeanResponse.setTimerBean(null);
					jobBeanResponse.setFlag(false);
					jobBeanResponse.setMessage("Job Not Found!!!");
					response = new ResponseEntity<JobBeanResponse>(jobBeanResponse, HttpStatus.OK);
				}
			}else{
				jobBeanResponse.setTimerBean(null);
				jobBeanResponse.setFlag(false);
				jobBeanResponse.setMessage("JobObject Not Found!!!");
				response = new ResponseEntity<JobBeanResponse>(jobBeanResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Error in "+getClass().getName()+"::getAllJob", e);
			jobBeanResponse.setTimerBean(null);
			jobBeanResponse.setFlag(false);
			jobBeanResponse.setMessage(e.getMessage());
			response = new ResponseEntity<JobBeanResponse>(jobBeanResponse, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value="/pauseTrigger/{objName:.+}/{triggerName:.+}/{triggerGroupName:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<StateResponse> pauseTrigger(@PathVariable String objName, @PathVariable String triggerName, @PathVariable String triggerGroupName){
		StateResponse triggerStateResponse = new StateResponse();
		ResponseEntity<StateResponse> response = null;
		try {
			long start = System.currentTimeMillis();
			TriggerState triggerState = quartzService.pauseTrigger(objName, triggerName, triggerGroupName);
			LOGGER.info("Service - quartzService: pauseTrigger: time to process = "+(System.currentTimeMillis() - start));
			if(triggerState != null && triggerState.getTriggerState() != null){
				triggerStateResponse.setState(triggerState.getTriggerState());
				triggerStateResponse.setFlag(true);
				triggerStateResponse.setMessage(null);
				response = new ResponseEntity<StateResponse>(triggerStateResponse, HttpStatus.OK);
			}else{
				triggerStateResponse.setState(null);
				triggerStateResponse.setFlag(false);
				triggerStateResponse.setMessage("Trigger state not found after pause!!!");
				response = new ResponseEntity<StateResponse>(triggerStateResponse, HttpStatus.OK);
			}
		} catch (ServiceException e) {
			LOGGER.error("Error in "+getClass().getName()+"::pauseTrigger", e);
			triggerStateResponse.setState(null);
			triggerStateResponse.setFlag(false);
			triggerStateResponse.setMessage(e.getMessage());
			response = new ResponseEntity<StateResponse>(triggerStateResponse, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value="/resumeTrigger/{objName:.+}/{triggerName:.+}/{triggerGroupName:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<StateResponse> resumeTrigger(@PathVariable String objName, @PathVariable String triggerName, @PathVariable String triggerGroupName){
		StateResponse triggerStateResponse = new StateResponse();
		ResponseEntity<StateResponse> response = null;
		try {
			long start = System.currentTimeMillis();
			TriggerState triggerState = quartzService.resumeTrigger(objName, triggerName, triggerGroupName);
			LOGGER.info("Service - quartzService: resumeTrigger: time to process = "+(System.currentTimeMillis() - start));
			if(triggerState != null && triggerState.getTriggerState() != null){
				triggerStateResponse.setState(triggerState.getTriggerState());
				triggerStateResponse.setFlag(true);
				triggerStateResponse.setMessage(null);
				response = new ResponseEntity<StateResponse>(triggerStateResponse, HttpStatus.OK);
			}else{
				triggerStateResponse.setState(null);
				triggerStateResponse.setFlag(false);
				triggerStateResponse.setMessage("Trigger state not found after resume!!!");
				response = new ResponseEntity<StateResponse>(triggerStateResponse, HttpStatus.OK);
			}
		} catch (ServiceException e) {
			LOGGER.error("Error in "+getClass().getName()+"::resumeTrigger", e);
			triggerStateResponse.setState(null);
			triggerStateResponse.setFlag(false);
			triggerStateResponse.setMessage(e.getMessage());
			response = new ResponseEntity<StateResponse>(triggerStateResponse, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value="/updateTrigger", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<StateResponse> updateTrigger(@RequestParam String objName, @RequestParam String jobName, 
			@RequestParam String groupName, @RequestParam String misfireInstruction, @RequestParam String cronExpression){
		
		StateResponse stateResponse = new StateResponse();
		try{
			long start = System.currentTimeMillis();
			quartzService.updateTrigger(objName, jobName, groupName, misfireInstruction, cronExpression);
			LOGGER.info("Service - updateTrigger: resumeTrigger: time to process = "+(System.currentTimeMillis() - start));
			stateResponse.setState("Updated");
			stateResponse.setMessage(null);
			stateResponse.setFlag(true);
		}catch(Exception e){
			LOGGER.error("Error in "+getClass().getName()+"::updateTrigger", e);
			stateResponse.setState(null);
			stateResponse.setMessage(e.getMessage());
			stateResponse.setFlag(false);
		}
		return new ResponseEntity<StateResponse>(stateResponse, HttpStatus.OK);
	}
	
}
