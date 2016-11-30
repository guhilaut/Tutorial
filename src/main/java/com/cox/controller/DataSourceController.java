package com.cox.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.cox.controller.helper.CommonHelper;
import com.cox.controller.helper.DSControllerHelper;
import com.cox.service.DataSourceService;
import com.cox.service.model.ConnectionModel;
import com.cox.service.model.DbPropsInfo;
import com.cox.view.model.Category;
import com.cox.view.model.CheckConnectionResponse;
import com.cox.view.model.ConnectionModelResponse;
import com.cox.view.model.DSCountResponse;
import com.cox.view.model.DSDependentResponse;
import com.cox.view.model.DataSourceResponse;
import com.cox.view.model.ServerListResponse;

@Controller
@RequestMapping(value = "/ds")
public class DataSourceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);
	
	@Autowired
	private DataSourceService dbService;
	@Autowired
	private CommonHelper helper;
	@Autowired
	private DSControllerHelper dsControllerHelper;
	
	@RequestMapping(value="/getDSCount/{url:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<DSCountResponse> getDSCount(@PathVariable String url, HttpServletResponse resp){
		
		DSCountResponse dsCountResponse = new DSCountResponse();
		try{
			helper.loadConfigService(url);
			Map<String, Integer> dsGroup = dsControllerHelper.getDSCount();
			if(dsGroup != null && !dsGroup.isEmpty()){
				dsCountResponse.setDsGroup(dsGroup);
				dsCountResponse.setFlag(true);
			}else{
				dsCountResponse.setFlag(false);
				dsCountResponse.setMessage("DataSource not found!!!");
			}
		}catch(Exception e){
			dsCountResponse.setFlag(false);
			dsCountResponse.setMessage(e.getMessage());
		}
		return new ResponseEntity<DSCountResponse>(dsCountResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<DataSourceResponse> getDataSourceList(HttpServletResponse resp) throws IOException{
		
		DataSourceResponse dataSourceBean = new DataSourceResponse();
		try{
			List<Category> dsObjNames = dsControllerHelper.getDSObjectNames();
			if(dsObjNames != null && !dsObjNames.isEmpty()){
				dataSourceBean.setDataSouceNames(dsObjNames);
				dataSourceBean.setFlag(true);
			}else{
				dataSourceBean.setMessage("DataSource not found!!!");
				dataSourceBean.setFlag(false);
			}
		}catch(Exception e){
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<DataSourceResponse>(dataSourceBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getDbInfo/{name:.+}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ConnectionModelResponse> getDBInfo(@PathVariable String name, HttpServletResponse resp) throws IOException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getDBInfo :::: params :: name = {0}", name);
		}
		ConnectionModelResponse connectionModelResponse = new ConnectionModelResponse();
		ResponseEntity<ConnectionModelResponse> response = null;
		try {
			ConnectionModel conModel = dbService.checkDBConnection(name);
			if(conModel != null){
				Map<String, String> connectionAttrMap = conModel.getValue();
				connectionModelResponse.setConnectionAttr(connectionAttrMap);
				connectionModelResponse.setFlag(true);
				connectionModelResponse.setMessage(null);
				response = new ResponseEntity<ConnectionModelResponse>(connectionModelResponse, HttpStatus.OK);
			}else{
				connectionModelResponse.setConnectionAttr(null);
				connectionModelResponse.setFlag(false);
				connectionModelResponse.setMessage("Connection Attribute Not Found!!!");
				response = new ResponseEntity<ConnectionModelResponse>(connectionModelResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+" :: getDBInfo() error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	@RequestMapping(value = "testConnection/{name:.+}", method = RequestMethod.GET)
	public ResponseEntity<CheckConnectionResponse> testConnection(@PathVariable String name,
			HttpServletRequest request) {
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: testConnection :::: params :: name = {0}", name);
		}
		CheckConnectionResponse checkConnectionResponse = new CheckConnectionResponse();
		try {
			String type = null;
			List<Category> categoryList = dsControllerHelper.getDSObjectNames();
			for(Category category : categoryList){
				if(category.getNames().contains(name)){
					type = category.getType();
				}
			}
			checkConnectionResponse = dbService.testConnection(name, type);
		} catch (Exception e) {
			checkConnectionResponse.setFlag(false);
			checkConnectionResponse.setMessage(e.getMessage());
			LOGGER.error(this.getClass().getName()+" :: testConnection error", e);
		}
		return new ResponseEntity<CheckConnectionResponse>(checkConnectionResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "getDSDependent/{name:.+}")
	public ResponseEntity<DSDependentResponse> getDSDependentBundle(
			@PathVariable String name) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getDSDependentBundle :::: params :: name = {0}", name);
		}
		DSDependentResponse dsDependentResponse = new DSDependentResponse();
		ResponseEntity<DSDependentResponse> response = null;
		try {
			List<String> dependentBundleList = dbService.getDSDependent(name);
			if(dependentBundleList != null && dependentBundleList.size() > 0){
				dsDependentResponse.setDependentList(dependentBundleList);
				dsDependentResponse.setFlag(true);
				dsDependentResponse.setMessage(null);
				response = new ResponseEntity<DSDependentResponse>(dsDependentResponse, HttpStatus.OK);
			}else{
				dsDependentResponse.setDependentList(null);
				dsDependentResponse.setFlag(false);
				dsDependentResponse.setMessage("Dependents Not Found!!!");
				response = new ResponseEntity<DSDependentResponse>(dsDependentResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+" :: getDSDependentBundle error", e);
			dsDependentResponse.setDependentList(null);
			dsDependentResponse.setFlag(false);
			dsDependentResponse.setMessage(e.getMessage());
			response = new ResponseEntity<DSDependentResponse>(dsDependentResponse, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value="getAS400Servers", method=RequestMethod.GET)
	public ResponseEntity<ServerListResponse> getAS400Servers(){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: testAs400Connection");
		}
		ResponseEntity<ServerListResponse> response = null;
		ServerListResponse serverListResponse = new ServerListResponse();
		//TODO: load from props.
		response = new ResponseEntity<ServerListResponse>(serverListResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value="getIcomServers", method=RequestMethod.GET)
	public ResponseEntity<ServerListResponse> getIcomServers(){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: testAs400Connection");
		}
		ResponseEntity<ServerListResponse> response = null;
		ServerListResponse serverListResponse = new ServerListResponse();
		//TODO: load from props.
		response = new ResponseEntity<ServerListResponse>(serverListResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value="testAS400Connection/{name:.+}", method=RequestMethod.GET)
	public ResponseEntity<CheckConnectionResponse> testAs400Connection(@PathVariable String name){
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: testAs400Connection");
		}
		DbPropsInfo dbPropsInfo = dsControllerHelper.getDBConfiguration(name);
		CheckConnectionResponse checkConnectionResponse = dbService.testAs400Connection(dbPropsInfo);
		return new ResponseEntity<CheckConnectionResponse>(checkConnectionResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="testICOMConnection", method=RequestMethod.GET)
	public ResponseEntity<CheckConnectionResponse> testICOMConnection(){
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: testICOMConnection");
		}
		String type = "ICOMS";
		DbPropsInfo dbPropsInfo = dsControllerHelper.getDBConfiguration(type);
		CheckConnectionResponse checkConnectionResponse = dbService.testICOMConnection(dbPropsInfo);
		
		return new ResponseEntity<CheckConnectionResponse>(checkConnectionResponse, HttpStatus.OK);
	}

}
