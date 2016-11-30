package com.cox.controller.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.controller.QuartzController;
import com.cox.exception.ServiceException;
import com.cox.service.QuartzService;
import com.cox.service.impl.Configuration;
import com.cox.service.model.TriggerBean;
import com.cox.service.model.TriggerDetail;
import com.cox.view.model.Category;

@Service
public class QuartzControllerHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzControllerHelper.class);
	
	@Autowired
	private QuartzService quartzService;
	@Autowired
	private Configuration configuration;
	
	public Map<String, Integer> getAllSchedulers() throws ServiceException{
		if(!configuration.getSimpleQuartzObjName().isEmpty()){
			configuration.setSimpleQuartzObjName(new ArrayList<String>());
		}
		if(!configuration.getComplexQuartzObjName().isEmpty()){
			configuration.setComplexQuartzObjName(new ArrayList<String>());
		}
		long start = System.currentTimeMillis();
		List<String> objNameList = quartzService.getSchedulerNameList();
		LOGGER.info("Service - quartzService: getSchedulerNameList: time to process = "+(System.currentTimeMillis() - start));
		List<TriggerBean> triggerBeanList = new ArrayList<TriggerBean>();
		for(String objName : objNameList){
			start = System.currentTimeMillis();
			TriggerBean triggerBean = quartzService.getAllTriggers(objName);
			LOGGER.info("Service - quartzService: getAllTriggers: time to process = "+(System.currentTimeMillis() - start));
			triggerBeanList.add(triggerBean);
		}
		Map<String, Integer> jobDataMap = getSimpleAndComplexJobDataMap(triggerBeanList);
		return jobDataMap;
	}

	private Map<String, Integer> getSimpleAndComplexJobDataMap(List<TriggerBean> triggerBeanList) {

		Map<String, Integer> dataMap = new HashMap<String, Integer>();
		
		int complexCount = 0;
		int simpleCount = 0;
		
		for(TriggerBean triggerBean : triggerBeanList){
			List<TriggerDetail> triggerDetailList = triggerBean.getTriggerDetailList();
			for(TriggerDetail triggerDetail : triggerDetailList){
				Object jobDataMapObj = triggerDetail.getJobDataMap();
				if(jobDataMapObj instanceof Map){
					Map<String, String> jobDataMap = (Map<String, String>)jobDataMapObj;
					String cronExpression = jobDataMap.get("CamelQuartzTriggerCronExpression");
					String[] splitted = cronExpression.split(" ");
					if(splitted.length >= 5){
						if((!splitted[4].equals("*"))||splitted[5].contains("SUN")||splitted[5].contains("MON")||splitted[5].contains("TUE")||splitted[5].contains("WED")
								||splitted[5].contains("THU")||splitted[5].contains("FRI")||splitted[5].contains("SAT")){
							if(!configuration.getComplexQuartzObjName().contains(triggerBean.getRequest().getMbean())){
								configuration.getComplexQuartzObjName().add(triggerBean.getRequest().getMbean());
							}
							dataMap.put("Complex", ++complexCount);
						}else{
							if(!configuration.getSimpleQuartzObjName().contains(triggerBean.getRequest().getMbean())){
								configuration.getSimpleQuartzObjName().add(triggerBean.getRequest().getMbean());
							}
							dataMap.put("Simple", ++simpleCount);
						}
					}
				}
			}
		}
		
		return dataMap;
	}
	
	public List<Category> getSchedulerList(){
		
		List<Category> schedulerList = new ArrayList<Category>();
		
		if(configuration.getSimpleQuartzObjName() != null && !configuration.getSimpleQuartzObjName().isEmpty()){
			Category simpleScheduler = new Category();
			simpleScheduler.setNames(configuration.getSimpleQuartzObjName());
			simpleScheduler.setType("SIMPLE");
			schedulerList.add(simpleScheduler);
		}
		
		if(configuration.getComplexQuartzObjName() != null && !configuration.getComplexQuartzObjName().isEmpty()){
			Category complexScheduler = new Category();
			complexScheduler.setNames(configuration.getComplexQuartzObjName());
			complexScheduler.setType("COMPLEX");
			schedulerList.add(complexScheduler);
		}
		
		return schedulerList;
	}
	
}
