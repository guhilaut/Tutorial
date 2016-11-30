package com.cox.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cox.controller.helper.CommonHelper;
import com.cox.service.BundleService;
import com.cox.service.model.BundleDetailResult;
import com.cox.service.model.BundleDetails;
import com.cox.service.model.BundleInfo;
import com.cox.service.model.CXFMBeanResult;
import com.cox.service.model.ConfigListBean;
import com.cox.service.model.MBeanListResult;
import com.cox.service.model.MConfigDetail;
import com.cox.view.mapper.ViewVOMapper;
import com.cox.view.model.BundleDetailVO;
import com.cox.view.model.BundleListInfo;
import com.cox.view.model.BundleVersion;
import com.cox.view.model.BundleVersionResponse;
import com.cox.view.model.ConfigListResponse;
import com.cox.view.model.ConfigProp;
import com.cox.view.model.ConfigPropResponse;
import com.cox.view.model.CxfDetialsResponse;
import com.cox.view.model.CxfResponse;
import com.cox.view.model.DependentBundle;
import com.cox.view.model.DependentBundleResponse;
import com.cox.view.model.RouteDefinition;
import com.cox.view.model.RouteDefinitionResponse;
import com.cox.view.model.SOAPRequest;
import com.cox.view.model.SOAPResponse;
import com.cox.view.model.StateResponse;

@Controller
@RequestMapping(value = "/bundle")
public class BundleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BundleController.class);

	@Autowired
	BundleService bundleService;
	@Autowired
	private ViewVOMapper mapper;
	@Autowired
	private CommonHelper helper;

	@RequestMapping(value = "/list/{url:.+}", method = RequestMethod.GET)
	public ResponseEntity<BundleListInfo> getBundleList(HttpServletRequest request, @PathVariable String url, HttpServletResponse resp) throws IOException{
		
		ResponseEntity<BundleListInfo> response = null;
		BundleListInfo bundleListInfo = null;
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getBundleList");
		}
		try{
			helper.loadConfigService(url);
			long start = System.currentTimeMillis();
			MBeanListResult bundleList = bundleService.listBundles();
			LOGGER.info("Service - bundleService: listBundles: time to process = "+(System.currentTimeMillis() - start));
			Map<Integer, BundleInfo> bundleMap = bundleList.getValue();
			List<BundleInfo> bundleInfoList = new ArrayList<BundleInfo>(bundleMap.values());
			bundleListInfo = new BundleListInfo();
			bundleListInfo.setBundleList(bundleInfoList);
			bundleListInfo.setFlag(true);
			bundleListInfo.setMessage(null);
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getBundleList error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<BundleListInfo>(bundleListInfo, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/getBundleInfo/{bundleId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<BundleDetailVO> getObjectInfo(
			@PathVariable String bundleId, HttpServletRequest request, HttpServletResponse resp) throws IOException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getObjectInfo :::: params :: bundleId = {0}", bundleId);
		}
		ResponseEntity<BundleDetailVO> response = null;
		BundleDetailVO bundleDetailVO = new BundleDetailVO();
		try {
			long start = System.currentTimeMillis();
			BundleDetailResult bundleDetailResult = bundleService.getMBeanInfo(bundleId);
			LOGGER.info("Service - bundleService: getMBeanInfo :: time to process = "+(System.currentTimeMillis() - start));
			BundleDetails bundleDetails = bundleDetailResult.getBundleDetails();
			bundleDetailVO = mapper.convertBundleDetails(bundleDetails);
			bundleDetailVO.setFlag(true);
			bundleDetailVO.setMessage(null);
			response = new ResponseEntity<BundleDetailVO>(bundleDetailVO, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+" :: getBundleList() error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	@RequestMapping(value="getAllConfig/{name:.+}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<ConfigListResponse> getAllConfig(@PathVariable String name, HttpServletResponse resp) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: loadConfig :::: params :: name = {0}", name);
		}
		ConfigListResponse configListResponse = null;
		ResponseEntity<ConfigListResponse> response = null;
		try{
			long start = System.currentTimeMillis();
			ConfigListBean configList = bundleService.listAllConfigFiles();
			LOGGER.info("Service - bundleService: listAllConfigFiles :: time to process = "+(System.currentTimeMillis() - start));
			if(configList != null){
				configListResponse  = mapper.createConfigList(configList, name);
			}else{
				configListResponse = new ConfigListResponse();
				configListResponse.setConfigList(null);
				configListResponse.setFlag(false);
				configListResponse.setMessage("Configuration Not Found!!!");
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getAllConfig error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<ConfigListResponse>(configListResponse, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "loadConfig/{name:.+}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<ConfigPropResponse> loadConfig(@PathVariable String name, HttpServletResponse resp) throws IOException {
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: loadConfig :::: params :: name = {0}", name);
		}
		ResponseEntity<ConfigPropResponse> response = null;
		ConfigPropResponse configPropResponse = new ConfigPropResponse();
		try {
			long start = System.currentTimeMillis();
			MConfigDetail mConfigDetail = bundleService.loadConfiguration(name);
			LOGGER.info("Service - bundleService: listAllConfigFiles :: time to process = "+(System.currentTimeMillis() - start));
			if(mConfigDetail != null){
				Map<String, String> propList = mConfigDetail.getProperties();
				if(propList != null && propList.size() > 0){
					List<ConfigProp> configPropList = mapper.createConfigPropList(propList);
					configPropResponse.setConfigPropList(configPropList);
					configPropResponse.setFlag(true);
					configPropResponse.setMessage(null);
					response = new ResponseEntity<ConfigPropResponse>(configPropResponse, HttpStatus.OK);
				}else{
					configPropResponse.setConfigPropList(null);
					configPropResponse.setFlag(false);
					configPropResponse.setMessage("Configuration Not Found!!!");
					response = new ResponseEntity<ConfigPropResponse>(configPropResponse, HttpStatus.OK);
				}
			}else{
				configPropResponse.setConfigPropList(null);
				configPropResponse.setFlag(false);
				configPropResponse.setMessage("Response Object Not Found!!!");
				response = new ResponseEntity<ConfigPropResponse>(configPropResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+" :: loadConfig error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	@RequestMapping(value = "getDependent/{name:.+}/{bundleId}")
	public ResponseEntity<DependentBundleResponse> getDependentBundle(
			@PathVariable String name, @PathVariable String bundleId, HttpServletResponse resp) throws IOException {
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getDependentBundle :::: params :: name = {0}, bundleId = {1}", name, bundleId);
		}
		DependentBundleResponse dependentBundleResponse = new DependentBundleResponse();
		ResponseEntity<DependentBundleResponse> response = null;
		try {
			long start = System.currentTimeMillis();
			Set<DependentBundle> dependentBundleList = bundleService.getDependentBundles(bundleId);
			LOGGER.info("Service - bundleService: getDependentBundles :: time to process = "+(System.currentTimeMillis() - start));
			if(dependentBundleList != null && dependentBundleList.size() >0){
				dependentBundleResponse.setDependentBundleList(dependentBundleList);
				dependentBundleResponse.setFlag(true);
				dependentBundleResponse.setMessage(null);
				response = new ResponseEntity<DependentBundleResponse>(dependentBundleResponse, HttpStatus.OK);
			}else{
				dependentBundleResponse.setDependentBundleList(null);
				dependentBundleResponse.setFlag(false);
				dependentBundleResponse.setMessage("Dependent Not Found!!!");
				response = new ResponseEntity<DependentBundleResponse>(dependentBundleResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+" :: getDependentBundle error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	@RequestMapping(value="getRoute/{name:.+}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RouteDefinitionResponse> getRouteDefinitions(@PathVariable String name, HttpServletResponse resp) throws IOException{
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getRouteDefinitions :::: params :: name = {0}", name);
		}
		RouteDefinitionResponse routeDefinitionResponse = new RouteDefinitionResponse();
		ResponseEntity<RouteDefinitionResponse> response = null;
		try{
			long start = System.currentTimeMillis();
			List<RouteDefinition> routeDefList = bundleService.getRouteDefinitions(name);
			LOGGER.info("Service - bundleService: getRouteDefinitions :: time to process = "+(System.currentTimeMillis() - start));
			if(routeDefList != null && routeDefList.size() > 0){
				routeDefinitionResponse.setRouteDefinitionResponse(routeDefList);
				routeDefinitionResponse.setFlag(true);
				routeDefinitionResponse.setMessage(null);
				response = new ResponseEntity<RouteDefinitionResponse>(routeDefinitionResponse, HttpStatus.OK);
			}else{
				routeDefinitionResponse.setRouteDefinitionResponse(null);
				routeDefinitionResponse.setFlag(false);
				routeDefinitionResponse.setMessage("Route Definition Not Found!!!");
				response = new ResponseEntity<RouteDefinitionResponse>(routeDefinitionResponse, HttpStatus.OK);
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getRouteDefinitions error", e);
			routeDefinitionResponse.setRouteDefinitionResponse(null);
			routeDefinitionResponse.setFlag(false);
			routeDefinitionResponse.setMessage("Route Definition Not Found!!!");
			response = new ResponseEntity<RouteDefinitionResponse>(routeDefinitionResponse, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value="checkVersion/{name:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<BundleVersionResponse> checkVersionMismatch(@PathVariable String name, HttpServletResponse resp) throws IOException{
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: checkVersionMismatch :::: params :: name = {0}", name);
		}
		ResponseEntity<BundleVersionResponse> response = null;
		BundleVersionResponse bundleVersionResponse = new BundleVersionResponse();
		try{
			long start = System.currentTimeMillis();
			List<BundleVersion> serverVersionList = bundleService.checkVersionMismatchOnAllServers(name);
			LOGGER.info("Service - bundleService: checkVersionMismatchOnAllServers :: time to process = "+(System.currentTimeMillis() - start));
			if(serverVersionList != null && serverVersionList.size() > 0){
				bundleVersionResponse.setBundleVersionList(serverVersionList);
				bundleVersionResponse.setFlag(true);
				bundleVersionResponse.setMessage(null);
			}else{
				bundleVersionResponse.setBundleVersionList(null);
				bundleVersionResponse.setFlag(false);
				bundleVersionResponse.setMessage("VersionList Not Found!!!");
			}
		}catch(Exception e){
			bundleVersionResponse.setBundleVersionList(null);
			bundleVersionResponse.setFlag(false);
			bundleVersionResponse.setMessage(e.getMessage());
		}
		response = new ResponseEntity<BundleVersionResponse>(bundleVersionResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value="changeBundleState", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<StateResponse> switchToBundleState(@RequestParam String bundleId, @RequestParam String state, HttpServletResponse resp) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: switchToBundleState called with params :: bundleId = {0}, state = {1}", bundleId, state);
		}
		StateResponse stateResponse = new StateResponse();
		try{
			long start = System.currentTimeMillis();
			boolean isUpdated = bundleService.switchBundleState(bundleId, state);
			LOGGER.info("Service - bundleService: switchBundleState :: time to process = "+(System.currentTimeMillis() - start));
			if(isUpdated){
				if("START".equalsIgnoreCase(state)){
					stateResponse.setFlag(true);
					stateResponse.setState("STARTED");
				}else if("STOP".equalsIgnoreCase(state)){
					stateResponse.setFlag(true);
					stateResponse.setState("STOPPED");
				}else if("REFRESH".equalsIgnoreCase(state)){
					stateResponse.setFlag(true);
					stateResponse.setState("REFRESHED");
				}
			}else{
				stateResponse.setFlag(false);
				stateResponse.setMessage("Error while changing bundle state to "+state);
			}
		}catch(Exception e){
			LOGGER.error(getClass().getName()+ " :: switchToBundleState", e);
			stateResponse.setFlag(false);
			stateResponse.setMessage("Error while changing bundle state to "+state);
		}
		return new ResponseEntity<StateResponse>(stateResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="updateConfig", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<StateResponse> updateConfiguration(@RequestParam String bundleName, @RequestBody List<ConfigProp> configPropList, HttpServletResponse resp) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: updateConfiguration called with params :: bundleId = {0}, state = {1}", bundleName, configPropList);
		}
		StateResponse stateResponse = new StateResponse();
		try{
			long start = System.currentTimeMillis();
			bundleService.updateConfiguration(bundleName, configPropList);
			LOGGER.info("Service - bundleService: updateConfiguration :: time to process = "+(System.currentTimeMillis() - start));
			stateResponse.setFlag(true);
			stateResponse.setState("Updated");
		}catch(Exception e){
			LOGGER.error(getClass().getName()+ " :: updateConfiguration", e);
			stateResponse.setFlag(false);
			stateResponse.setMessage(e.getMessage());
		}
		return new ResponseEntity<StateResponse>(stateResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value="/cxf/{name:.+}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<CxfResponse> getCxfDetails(HttpServletRequest request,@PathVariable String name, HttpServletResponse resp) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getCxfDetails :::: params :: name = {0}", name);
		}
		CxfResponse cxfResponse = new CxfResponse();
		ResponseEntity<CxfResponse> response = null;
		String url = null;
		try{
			name = name.trim();
			if(!StringUtils.isEmpty(name)){
				long start = System.currentTimeMillis();
				List<CXFMBeanResult> cxfMBeanResultList = bundleService.getCxfMBean(name);
				LOGGER.info("Service - bundleService: getCxfMBean :: time to process = "+(System.currentTimeMillis() - start));
				if(!cxfMBeanResultList.isEmpty()){
					for(CXFMBeanResult cxfMBeanResult : cxfMBeanResultList){
						start = System.currentTimeMillis();
						url = bundleService.getCxfEndpointURL(cxfMBeanResult.getValue());
						LOGGER.info("Service - bundleService: getCxfEndpointURL :: time to process = "+(System.currentTimeMillis() - start));
						if(url != null){
							start = System.currentTimeMillis();
							List<String> operationList = bundleService.getOperationList(url,cxfMBeanResult.getValue().isWsdl());
							LOGGER.info("Service - bundleService: getOperationList :: time to process = "+(System.currentTimeMillis() - start));
							CxfDetialsResponse cxfDetialsResponse = new CxfDetialsResponse();
							cxfDetialsResponse.setEndpoint(cxfMBeanResult.getValue().getAddress());
							cxfDetialsResponse.setUrl(url);
							cxfDetialsResponse.setOperations(operationList);
							cxfDetialsResponse.setWsdl(cxfMBeanResult.getValue().isWsdl());
							cxfResponse.getCxfDetialsResponse().add(cxfDetialsResponse);
						}
						else{
							if(LOGGER.isWarnEnabled()){
								LOGGER.warn(this.getClass().getName()+" :: getCxfDetails :: Could not create cxf endpoint URL for Bundle = "+name);
							}
						}
					}
					cxfResponse.setFlag(true);
				}else{
					cxfResponse.setFlag(false);
					cxfResponse.setMessage("URL for WSDL/WADL not found for bundle : "+name+"!");
				}
			}else{
				cxfResponse.setFlag(false);
				cxfResponse.setMessage("Please provide a valid bundle name! :: "+name);
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getCxfDetails error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<CxfResponse>(cxfResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value="/buildsoaprequest", method = RequestMethod.POST, produces="application/json")
	public ResponseEntity<SOAPRequest> getSOAPRequestString(@RequestParam String wsdlurl,@RequestParam String opname,HttpServletResponse resp) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getSOAPRequestString :::: params :: soapurl = {0} and opname ={1}", wsdlurl,opname);
		}
		SOAPRequest soapRequest = new SOAPRequest();
		ResponseEntity<SOAPRequest> response = null;
		try{
			wsdlurl = wsdlurl.trim();
			opname = opname.trim();
			if(!StringUtils.isEmpty(wsdlurl) && !StringUtils.isEmpty(opname)){
				long start = System.currentTimeMillis();
				String requestString = bundleService.getSOAPRequest(wsdlurl, opname);
				LOGGER.info("Service - bundleService: getSOAPRequest :: time to process = "+(System.currentTimeMillis() - start));
				if(requestString != null){
					soapRequest.setFlag(true);
					soapRequest.setRequestXML(requestString);
				}else{
					soapRequest.setFlag(false);
					soapRequest.setMessage("Operation "+opname+" does not exist for wsdl "+wsdlurl);
				}
			}else{
				soapRequest.setFlag(false);
				soapRequest.setMessage("Please provide a valid wsdl url :: "+wsdlurl);
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getSOAPRequestString error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<SOAPRequest>(soapRequest, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value="/soaprequest", method = RequestMethod.POST, produces="application/json")
	public ResponseEntity<SOAPResponse> getSOAPResponseString(@RequestParam String wsdlurl,@RequestParam String opname,@RequestBody String soaprequest,HttpServletResponse resp) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getSOAPRequestString :::: params :: soapurl = {0} and opname ={1} and soapreqest ={2}", wsdlurl,opname,soaprequest);
		}
		SOAPResponse soapResponse = new SOAPResponse();
		ResponseEntity<SOAPResponse> response = null;
		try{
			wsdlurl = wsdlurl.trim();
			opname = opname.trim();
			soaprequest = soaprequest.trim();
			if(!StringUtils.isEmpty(wsdlurl) && !StringUtils.isEmpty(opname) && !StringUtils.isEmpty(soaprequest)){
				long start = System.currentTimeMillis();
				String responseString = bundleService.testServiceOperationForWSDL(wsdlurl, opname, soaprequest);
				LOGGER.info("Service - bundleService: testServiceOperationForWSDL :: time to process = "+(System.currentTimeMillis() - start));
				if(responseString != null){
					soapResponse.setFlag(true);
					soapResponse.setResponseXML(responseString);
				}else{
					soapResponse.setFlag(false);
					soapResponse.setMessage("Operation "+opname+" does not exist for wsdl "+wsdlurl);
				}
			}else{
				soapResponse.setFlag(false);
				soapResponse.setMessage("Please provide a valid wsdl url :: "+wsdlurl);	
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getSOAPRequestString error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<SOAPResponse>(soapResponse, HttpStatus.OK);
		return response;
	}
	
}
