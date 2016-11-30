package com.cox.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cox.controller.helper.EnvControllerHelper;
import com.cox.service.impl.Configuration;
import com.cox.view.model.ServerConfigBean;
import com.cox.view.model.EnvGroupsResp;
import com.cox.view.model.EnvResponse;
import com.cox.view.model.EnvServerBean;

@Controller
@RequestMapping("/envGroup")
public class EnvController {
	
	@Autowired
	private EnvControllerHelper envControllerHelper;
	
	@Autowired
	private Configuration configuration;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnvController.class);
			
	@RequestMapping(value="/{homepage}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<EnvGroupsResp> getEnvGroup(@PathVariable String homepage){
		EnvGroupsResp envGroupsResp = new EnvGroupsResp();
		if(StringUtils.isBlank(homepage)){
			envGroupsResp.setMessage("Please pass homepage name in the url!");
			envGroupsResp.setFlag(false);
			return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
		}
		configuration.setEnvGroupsMap(new HashMap<String, List<String>>());
		try{
			envControllerHelper.loadEnvGroups(homepage);
			Set<String> envGroups = configuration.getEnvGroupsMap().keySet();
			envGroupsResp.setEnvGroups(envGroups);
			envGroupsResp.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::getEnvGroup", ex);
			envGroupsResp.setFlag(false);
			envGroupsResp.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
	}
	
	@RequestMapping(value="/add/{homepage}", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<EnvGroupsResp> addEnvGroup(@PathVariable String homepage, @RequestParam String envGrpName){
		EnvGroupsResp envGroupsResp = new EnvGroupsResp();
		if(StringUtils.isBlank(homepage)){
			envGroupsResp.setMessage("Please pass homepage name in the url!");
			envGroupsResp.setFlag(false);
			return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
		}
		try{
			envControllerHelper.addEnvGroup(homepage,envGrpName);
			Set<String> envGroups = configuration.getEnvGroupsMap().keySet();
			envGroupsResp.setEnvGroups(envGroups);
			envGroupsResp.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::addEnvGroup", ex);
			envGroupsResp.setFlag(false);
			envGroupsResp.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
	}
	
	@RequestMapping(value="/delete/{homepage}", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<EnvGroupsResp> deleteEnvGroup(@PathVariable String homepage,@RequestParam String envGrpName){
		EnvGroupsResp envGroupsResp = new EnvGroupsResp();
		if(StringUtils.isBlank(homepage)){
			envGroupsResp.setMessage("Please pass homepage name in the url!");
			envGroupsResp.setFlag(false);
			return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
		}
		try{
			envControllerHelper.deleteEnvGroup(homepage,envGrpName);
			Set<String> envGroups = configuration.getEnvGroupsMap().keySet();
			envGroupsResp.setEnvGroups(envGroups);
			envGroupsResp.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::deleteEnvGroup", ex);
			envGroupsResp.setFlag(false);
			envGroupsResp.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
	}
	
	@RequestMapping(value="/update/{homepage}", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<EnvGroupsResp> updateEnvGroup(@PathVariable String homepage, @RequestParam String oldGrpName, @RequestParam String newGrpName){
		EnvGroupsResp envGroupsResp = new EnvGroupsResp();
		if(StringUtils.isBlank(homepage)){
			envGroupsResp.setMessage("Please pass homepage name in the url!");
			envGroupsResp.setFlag(false);
			return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
		}
		try{
			envControllerHelper.updateEnvGroup(homepage, oldGrpName, newGrpName);
			Set<String> envGroups = configuration.getEnvGroupsMap().keySet();
			envGroupsResp.setEnvGroups(envGroups);
			envGroupsResp.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::updateEnvGroup", ex);
			envGroupsResp.setFlag(false);
			envGroupsResp.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvGroupsResp>(envGroupsResp, HttpStatus.OK);
	}
	
	@RequestMapping(value="/env/{homepage}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<EnvResponse> getEnv(@PathVariable String homepage,@RequestParam String envGrpName){
		
		EnvResponse envResponse = new EnvResponse();
		if(StringUtils.isBlank(homepage)){
			envResponse.setMessage("Please pass homepage name in the url!");
			envResponse.setFlag(false);
			return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
		}
		try{
			List<EnvServerBean> envServerList = new ArrayList<EnvServerBean>();
			List<String> envList = configuration.getEnvGroupsMap().get(envGrpName);
			if(envList != null){
				for(String env : envList){
					if(!"".equals(env)){
						List<ServerConfigBean> serverList = envControllerHelper.loadEnv(homepage,env, envGrpName);
						EnvServerBean envServerBean = new EnvServerBean();
						envServerBean.setEnvName(env);
						envServerBean.setServerList(serverList);
						envServerList.add(envServerBean);
					}
				}
			}
			envResponse.setEnvServerList(envServerList);
			envResponse.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::getEnv", ex);
			envResponse.setFlag(false);
			envResponse.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="/env/add/{homepage}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<EnvResponse> addEnv(@PathVariable String homepage,@RequestParam String envGrpName, @RequestParam String envName, @RequestBody List<ServerConfigBean> amqServerList){
		EnvResponse envResponse = new EnvResponse();
		if(StringUtils.isBlank(homepage)){
			envResponse.setMessage("Please pass homepage name in the url!");
			envResponse.setFlag(false);
			return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
		}
		try{
			envControllerHelper.addEnv(homepage,envGrpName, envName, amqServerList);
			List<EnvServerBean> envServerList = new ArrayList<EnvServerBean>();
			List<String> envList = configuration.getEnvGroupsMap().get(envGrpName);
			for(String env : envList){
				if(!"".equals(env)){
					EnvServerBean envServerBean = new EnvServerBean();
					envServerBean.setEnvName(env);
					envServerBean.setServerList(configuration.getServerIpConfigMap().get(env));
					envServerList.add(envServerBean);
				}
			}
			envResponse.setEnvServerList(envServerList);
			envResponse.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::addEnv", ex);
			envResponse.setFlag(false);
			envResponse.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="/env/delete/{homepage}", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<EnvResponse> deleteEnv(@PathVariable String homepage,@RequestParam String envGrpName, @RequestParam String envName){
		EnvResponse envResponse = new EnvResponse();
		if(StringUtils.isBlank(homepage)){
			envResponse.setMessage("Please pass homepage name in the url!");
			envResponse.setFlag(false);
			return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
		}
		try{
			envControllerHelper.deleteEnv(homepage,envGrpName, envName);
			List<EnvServerBean> envServerList = new ArrayList<EnvServerBean>();
			List<String> envList = configuration.getEnvGroupsMap().get(envGrpName);
			for(String env : envList){
				if(!"".equals(env)){
					EnvServerBean envServerBean = new EnvServerBean();
					envServerBean.setEnvName(env);
					envServerBean.setServerList(configuration.getServerIpConfigMap().get(env));
					envServerList.add(envServerBean);
				}
			}
			envResponse.setEnvServerList(envServerList);
			envResponse.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::deleteEnv", ex);
			envResponse.setFlag(false);
			envResponse.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="/env/update/{homepage}", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<EnvResponse> updateEnv(@PathVariable String homepage, @RequestParam String envGrpName, @RequestParam String oldEnvName, @RequestParam String newEnvName,@RequestBody  List<ServerConfigBean> serverList){
		EnvResponse envResponse = new EnvResponse();
		if(StringUtils.isBlank(homepage)){
			envResponse.setMessage("Please pass homepage name in the url!");
			envResponse.setFlag(false);
			return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
		}
		try{
			envControllerHelper.updateEnv(homepage, envGrpName, oldEnvName, newEnvName, serverList);
			List<EnvServerBean> envServerList = new ArrayList<EnvServerBean>();
			List<String> envList = configuration.getEnvGroupsMap().get(envGrpName);
			for(String env : envList){
				if(!"".equals(env)){
					EnvServerBean envServerBean = new EnvServerBean();
					envServerBean.setEnvName(env);
					envServerBean.setServerList(configuration.getServerIpConfigMap().get(env));
					envServerList.add(envServerBean);
				}
			}
			envResponse.setEnvServerList(envServerList);
			envResponse.setFlag(true);
		}catch(Exception ex){
			LOGGER.error("Error in "+getClass().getName()+"::updateEnv", ex);
			envResponse.setFlag(false);
			envResponse.setMessage("Caused By: "+ex.getMessage());
		}
		return new ResponseEntity<EnvResponse>(envResponse, HttpStatus.OK);
	}
}
