package com.cox.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.cox.service.BundleService;
import com.cox.service.model.BundleDetailResult;
import com.cox.service.model.BundleInfo;
import com.cox.service.model.CXFMBeanInfo;
import com.cox.service.model.CXFMBeanResult;
import com.cox.service.model.ConfigListBean;
import com.cox.service.model.MBeanListResult;
import com.cox.service.model.MBeanSearchResult;
import com.cox.service.model.MConfigDetail;
import com.cox.service.model.RouteDTO;
import com.cox.service.model.RouteModel;
import com.cox.service.model.RouteObj;
import com.cox.service.model.User;
import com.cox.util.BatchRequestClient;
import com.cox.view.model.BundleVersion;
import com.cox.view.model.ConfigProp;
import com.cox.view.model.DependentBundle;
import com.cox.view.model.RouteDefinition;
import com.cox.view.model.ServerConfigBean;
import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.rest.support.WadlImporter;
import com.eviware.soapui.impl.support.AbstractHttpRequestInterface;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BundleServiceImpl implements BundleService{
	
	@Autowired
	private Configuration configService;
	
	@Autowired
	private CommonService commonService;
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BundleServiceImpl.class);
	
	
	
	public MBeanListResult listBundles() throws ServiceException{
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: listBundles");
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest("org.apache.karaf:type=bundles,name=root","list");
			J4pExecResponse response = client.execute(request);
			JSONObject obj = response.asJSONObject();
			((CloseableHttpClient)client.getHttpClient()).close();
			MBeanListResult mBeanListResult = mapper.convertValue(obj, MBeanListResult.class);
			
			return mBeanListResult;
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: checkDBConnection :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: checkDBConnection :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: listBundles :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	/*public MBeanSearchResult getAllMBeans() throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getAllMBeans");
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pSearchRequest request = new J4pSearchRequest("*:*");
			J4pSearchResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject jsonObject = response.asJSONObject();
			MBeanSearchResult result = null;
			result = mapper.convertValue(jsonObject, MBeanSearchResult.class);
			
			return result;
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getAllMBeans :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getAllMBeans :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getAllMBeans :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}*/
	
	public BundleDetailResult getMBeanInfo(String bundleId) throws ServiceException{
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getMBeanInfo :::: params :: bundleId = {}", bundleId);
		}
		try{
			String type = "osgi.core:framework=org.apache.felix.framework,type=bundleState,*";
			String objectName = getObjectName(type);
			if(objectName == null){
				throw new ServiceException("Object Name Not Found.");
			}
			else{
				String baseUrl = configService.getBaseUrl();
				J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
				J4pExecRequest request = new J4pExecRequest(objectName,"getBundle", bundleId);
				J4pExecResponse response = client.execute(request);
				((CloseableHttpClient)client.getHttpClient()).close();
				JSONObject obj = response.asJSONObject();
				BundleDetailResult bundleDetails = mapper.convertValue(obj, BundleDetailResult.class);
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("bundle Info == \n"+obj.toJSONString());
				}
				
				return bundleDetails;
			}
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getMBeanInfo :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getMBeanInfo :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getMBeanInfo :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	private String getObjectName(String type) throws ServiceException {
		
		//List<String> nameList = configService.getObjectNamesList();
		List<String> nameList = commonService.searchMBeans(type);
		String objectName = null;
		for(String name : nameList){
			//if(name.contains(type)){
				objectName = name;
				break;
			//}
		}
		return objectName;
	}

	public List<BundleDetailResult> getMBeanInfoList(List<String> bundleIdList) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getMBeanInfoList :::: params :: bundleId = {}", bundleIdList);
		}
		J4pClient client = null;
		try{
			List<BundleDetailResult> bundleDetailResultList = new ArrayList<BundleDetailResult>();
			String type = "osgi.core:framework=org.apache.felix.framework,type=bundleState,*";
			String objectName = getObjectName(type);
			
			if(objectName == null){
				throw new ServiceException("Object Name Not Found.");
			}
			else{
				String baseUrl = configService.getBaseUrl();
				//increase the connection timeout and connection-pool size
				client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).pooledConnections().maxTotalConnections(50).maxConnectionPoolTimeout(180000).build();
				
				List<J4pExecResponse> responseList = new CopyOnWriteArrayList<J4pExecResponse>();
				if(bundleIdList.size() > 49){
					if(LOGGER.isDebugEnabled()){
						LOGGER.debug(getClass().getName()+":: getMBeanInfoList >> bundleIdList size = "+bundleIdList.size());
					}
					//create 50 requests batch and process them asynchronously.
					/*int latchCount = 1 + (bundleIdList.size() / 50);*/
					ExecutorService executorService = Executors.newFixedThreadPool(10);
					List<List<J4pExecRequest>> mutliRequestList = new ArrayList<List<J4pExecRequest>>();
					List<J4pExecRequest> requestList = null;
					int latchCount = 0;
					for(int i = 0; i<bundleIdList.size(); i++){
						J4pExecRequest request = new J4pExecRequest(objectName,"getBundle", bundleIdList.get(i));
						if(i%50 == 0){
							requestList = new ArrayList<J4pExecRequest>();
							requestList.add(request);
							mutliRequestList.add(requestList);
							latchCount++;
						}else{
							requestList.add(request);
						}
					}
					//Adding last to the batch request
					mutliRequestList.add(requestList);
					long startTime = System.currentTimeMillis();
					if(LOGGER.isDebugEnabled()){
						LOGGER.debug(getClass().getName()+":: getMBeanInfoList >> initializing latch with size = "+(latchCount+1));
					}
					CountDownLatch latch = new CountDownLatch(latchCount + 1);
					for(List<J4pExecRequest> requests : mutliRequestList){
						executorService.submit(new BatchRequestClient(client, requests, responseList, latch));
					}
					try {
						if(LOGGER.isDebugEnabled()){
							LOGGER.debug(getClass().getName()+":: getMBeanInfoList >> waiting on latch to complete ....");
						}
						latch.await();
					} catch (InterruptedException e) {
						LOGGER.error(getClass().getName()+" :: getMBeanInfoList :::: error", e);
					}finally{
						executorService.shutdownNow();
					}
					long endTime = System.currentTimeMillis() - startTime;
					LOGGER.info(this.getClass().getName()+":: got response in "+endTime+" milli seconds from all thread >> "+responseList);
				}else{
					List<J4pExecRequest> requestList = new ArrayList<J4pExecRequest>();
					for(String bundleId : bundleIdList){
						J4pExecRequest request = new J4pExecRequest(objectName,"getBundle", bundleId);
						requestList.add(request);
					}
					responseList = client.execute(requestList);
				}
				
				for(J4pExecResponse response : responseList){
					JSONObject obj = response.asJSONObject();
					BundleDetailResult bundleDetails = mapper.convertValue(obj, BundleDetailResult.class);
					if(LOGGER.isDebugEnabled()){
						LOGGER.debug("bundle Info == \n"+obj.toJSONString());
					}
					bundleDetailResultList.add(bundleDetails);
				}
				return bundleDetailResultList;
			}
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getMBeanInfoList :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getMBeanInfoList :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} finally{
			if(client != null){
				try {
					((CloseableHttpClient)client.getHttpClient()).close();
				} catch (IOException e) {
					LOGGER.error(getClass().getName()+" :: getMBeanInfoList :::: error", e);
					throw new ServiceException("IO Connection Error", e);
				}
			}
		}
	}
	
	public Set<DependentBundle> getDependentBundles(String bundleId) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getDependentBundles :::: params :: bundleId = {}", bundleId);
		}
		Set<DependentBundle> dependentBundleList = new HashSet<DependentBundle>();
		Set<Integer> otherBundlesIds = configService.getBundleMap().keySet();
		otherBundlesIds.remove(bundleId);
		BundleDetailResult bundleDetailResult = getMBeanInfo(bundleId);
		List<String> exportedPackage = bundleDetailResult.getBundleDetails().getExportedPackages();
		//contains semi-colon so remove that.
		List<String> filteredExportedPackage = exportedPackage;/*new ArrayList<String>();*/
		/*for(String pack : exportedPackage){
			if(pack.contains(";")){
				pack = pack.substring(0, pack.indexOf(";"));
			}
			filteredExportedPackage.add(pack);
		}*/
		List<String> bundleIdList = new ArrayList<String>();
		for(Integer id : otherBundlesIds){
			String bunId = id.toString();
			bundleIdList.add(bunId);
		}
		
		List<BundleDetailResult> detailResultList = getMBeanInfoList(bundleIdList);
		
		for(BundleDetailResult detailResult : detailResultList){
			if(detailResult.getBundleDetails() == null){
				continue;
			}
			if(detailResult.getBundleDetails().getImportedPackages() == null || detailResult.getBundleDetails().getImportedPackages().isEmpty()){
				continue;
			}
			List<String> importedPackage = detailResult.getBundleDetails().getImportedPackages();
			for(String impackage : importedPackage){
				//check only packages, not the versions and other things
				/*if(impackage.contains(";")){
					impackage = impackage.substring(0, impackage.indexOf(";"));
				}*/
//				System.out.println("Check in list >> "+impackage);
				if(filteredExportedPackage.contains(impackage)){
					DependentBundle dependentBundle = new DependentBundle();
					dependentBundle.setBundleId(String.valueOf(detailResult.getBundleDetails().getIdentifier()));
					dependentBundle.setBundleName(configService.getBundleMap().get(detailResult.getBundleDetails().getIdentifier())+"("+detailResult.getBundleDetails().getVersion()+")");
					dependentBundleList.add(dependentBundle);
					break;
				}
			}
		}
		return dependentBundleList;
	}
	
	public ConfigListBean listAllConfigFiles() throws ServiceException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: listAllConfigFiles");
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			String objectName = "org.apache.karaf:type=config,name=root";
			J4pExecRequest request = new J4pExecRequest(objectName,"list");
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			ConfigListBean configList = mapper.convertValue(obj, ConfigListBean.class);
			return configList;
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: listAllConfigFiles :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: listAllConfigFiles :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: listAllConfigFiles :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public MConfigDetail loadConfiguration(String bundleName) throws ServiceException{
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: loadConfiguration :::: params :: bundleName = {}", bundleName);
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			String objectName = "org.apache.karaf:type=config,name=root";
			J4pExecRequest request = new J4pExecRequest(objectName,"listProperties", bundleName);
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			MConfigDetail mConfigDetail = mapper.convertValue(obj, MConfigDetail.class);
			
			return mConfigDetail;
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: loadConfiguration :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: loadConfiguration :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: loadConfiguration :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public void updateConfiguration(String bundleName, List<ConfigProp> configPropList) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: updateConfiguration :::: params :: bundleName = {0} :: configMap = {1}", bundleName, configPropList);
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			String objectName = "org.apache.karaf:type=config,name=root";
			J4pExecRequest request = null;
			for(ConfigProp configProp : configPropList){
				request = new J4pExecRequest(objectName,"deleteProperty", bundleName, configProp.getName());
				client.execute(request);
				if(configProp.getValue() != null){
					request = new J4pExecRequest(objectName,"setProperty", bundleName, configProp.getName(), configProp.getValue());
					client.execute(request);
				}
			}
			((CloseableHttpClient)client.getHttpClient()).close();
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: updateConfiguration :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: updateConfiguration :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: updateConfiguration :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public List<RouteDefinition> getRouteDefinitions(String objName) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getRouteDefinitions :::: params :: objName = {}", objName);
		}
		try{
			String type = "org.apache.camel:context="+objName+",type=routes,name=*";
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(type);
			J4pReadResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject jsonObj = response.asJSONObject();
			RouteModel routeModel = mapper.convertValue(jsonObj, RouteModel.class);
			List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
			for(Entry<String, RouteObj> entry : routeModel.getRouteMap().entrySet()){
				//if(entry.getValue().getEndpointUri().contains("cxf:")){
					getRouteInfo(objName, entry.getValue().getRouteId(), routeDefinitions, entry.getValue().getEndpointUri());
				//}
			}
			
			return routeDefinitions;
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getRouteDefinitions :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getRouteDefinitions :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getRouteDefinitions :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	
	private List<RouteDefinition> populateRouteInfoRequiredAttributes(RouteDTO routeInfo,List<RouteDefinition> routeDefinitions, String routeId, String endpoint) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: populateRouteInfoRequiredAttributes :::: params :: routeInfo = {0}, "
					+ "routeDefinitions = {1}, routeId = {2}, endpoint = {3}", routeInfo, routeDefinitions, routeId, endpoint);
		}
		RouteDefinition routeDefinition = new RouteDefinition();
		routeDefinition.setRouteId(routeId);
		routeDefinition.setRouteXML(routeInfo.getValue());
		/*List<String> complexOperationNames = ReadXMLFile.marshal("simple", routeInfo.getValue());
		List<String> operations = ReadXMLFile.getRawOperation(complexOperationNames);
		routeDefinition.setOperations(operations);*/
		routeDefinitions.add(routeDefinition);
		return routeDefinitions;
	}

	//TODO: Need to process in batch
	public RouteDTO getRouteInfo(String objName, String routeId, List<RouteDefinition> routeDefinitions, String endpoint) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getRouteInfo :::: params :: objName = {0}, "
					+ "routeId = {1}, routeDefinitions = {2}, endpoint = {3}", objName, routeId, routeDefinitions, endpoint);
		}
		try{
			String type = "org.apache.camel:context="+objName+",type=routes,name=\""+routeId+"\"";
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest(type, "dumpRouteAsXml");
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			if(LOGGER.isDebugEnabled()){
				LOGGER.info("Route info response >>> "+response.asJSONObject());
			}
			RouteDTO routeInfo = mapper.convertValue(response, RouteDTO.class);
			populateRouteInfoRequiredAttributes(routeInfo, routeDefinitions, routeId, endpoint);
			return routeInfo;
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getRouteInfo :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getRouteInfo :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getRouteInfo :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	//TODO: Need to process in multi-threaded way to improve performance 
	public List<BundleVersion> checkVersionMismatchOnAllServers(String bundleName) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: checkVersionMismatchOnAllServers :::: params :: bundleName = {0}, ", bundleName);
		}
		List<BundleVersion> serverVersionList = new ArrayList<BundleVersion>();
		
		for(ServerConfigBean serverConfigBean : configService.getActiveServersConfig()){
			try{
//				String baseUrl = ContextListener.getBaseUrlScheme()+"://"+serverConfigBean+":"+ContextListener.getBaseUrlPort()+"/jolokia";
				String jolokiaPath = serverConfigBean.getScheme()+"://"+serverConfigBean.getIp()+":"+serverConfigBean.getPort()+serverConfigBean.getJolokiaUrl();
				J4pClient client = J4pClient.url(jolokiaPath).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
				J4pExecRequest request = new J4pExecRequest("org.apache.karaf:type=bundles,name=root","list");
				J4pExecResponse response = client.execute(request);
				JSONObject obj = response.asJSONObject();
				MBeanListResult mBeanListResult = mapper.convertValue(obj, MBeanListResult.class);
				Map<Integer, BundleInfo> bundleMap = mBeanListResult.getValue();
				boolean isAdded = false;
				for(Entry<Integer, BundleInfo> entry: bundleMap.entrySet()){
					if(entry.getValue().getName().equals(bundleName)){
						BundleVersion bundleVersion = new BundleVersion();
						bundleVersion.setServerName(serverConfigBean.getIp());
						bundleVersion.setVersion(entry.getValue().getVersion());
						serverVersionList.add(bundleVersion);
						isAdded = true;
						break;
					}
				}
				if(!isAdded){
					BundleVersion bundleVersion = new BundleVersion();
					bundleVersion.setServerName(serverConfigBean.getIp());
					bundleVersion.setVersion("Not Deployed");
					serverVersionList.add(bundleVersion);
				}
			}catch(Exception ex){
				BundleVersion bundleVersion = new BundleVersion();
				bundleVersion.setServerName(serverConfigBean.getIp());
				bundleVersion.setVersion("Not Deployed");
				serverVersionList.add(bundleVersion);
				ex.printStackTrace();
			}
		}
		return serverVersionList;
	}
	
	public int getBundleCount(User user, String jolokiaPath) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getBundleCount called with params :: user = {0}, ip = {1}", user, jolokiaPath);
		}
		int count = 0;
		try{
			J4pClient client = J4pClient.url(jolokiaPath).user(user.getName()).password(user.getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = new J4pExecRequest("org.apache.karaf:type=bundles,name=root","list");
			J4pExecResponse response = client.execute(request);
			JSONObject obj = response.asJSONObject();
			((CloseableHttpClient)client.getHttpClient()).close();
			MBeanListResult mBeanListResult = mapper.convertValue(obj, MBeanListResult.class);
			count = mBeanListResult.getValue().size();
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}/* catch (NoSuchAlgorithmException e) {
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("NoSuchAlgorithmException Error", e);
		} catch (KeyManagementException e) {
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("KeyManagementException Error", e);
		}*/
		return count;
	}
	
	public boolean switchBundleState(String bundleId, String status) throws ServiceException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+"::startBundle called with params :: {0}", bundleId);
		}
		String type = "osgi.core:framework=org.apache.felix.framework,type=framework,*";
		String objName = getObjectName(type);
		boolean flag = false;
		LOGGER.info("Object name for the framework :: "+objName);
		if(objName == null){
			throw new ServiceException("Object Name Not Found.");
		}
		try{
			J4pClient client = J4pClient.url(configService.getBaseUrl()).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pExecRequest request = null;
			if("START".equalsIgnoreCase(status)){
				request = new J4pExecRequest(objName,"startBundle", bundleId);
			}else if("STOP".equalsIgnoreCase(status)){
				request = new J4pExecRequest(objName,"stopBundle", bundleId);
			}else if("REFRESH".equalsIgnoreCase(status)){
				request = new J4pExecRequest(objName,"refreshBundle", bundleId);
			}else{
				throw new ServiceException("Invalid status of bundle!!!");
			}
			if(request != null){
				client.execute(request);
				((CloseableHttpClient)client.getHttpClient()).close();
				flag = true;
			}
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getBundleCount :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
		return flag;
	}
	
	public List<CXFMBeanResult> getCxfMBean(String bundleName) throws ServiceException{
		List<CXFMBeanResult> cxfMBeanResultList = new ArrayList<CXFMBeanResult>();
		//if(!configService.getObjectNamesList().isEmpty()){
			//List<String> MbeanNames = configService.getObjectNamesList();
			String stringToSearch = "io.fabric8.cxf:bus.id="+bundleName+"*,type=Bus.Service.Endpoint,*";
			List<String> MbeanNames = commonService.searchMBeans(stringToSearch);
			for(String objectName : MbeanNames){
				//if(objectName.contains("io.fabric8.cxf:bus.id="+bundleName) && objectName.contains("type=Bus.Service.Endpoint")){
					try {
						J4pClient client = J4pClient.url(configService.getBaseUrl()).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
						J4pReadRequest request = new J4pReadRequest(objectName);
						J4pReadResponse response = client.execute(request);
						JSONObject jsonObj = response.asJSONObject();
						((CloseableHttpClient)client.getHttpClient()).close();
						CXFMBeanResult cxfMBeanResult = mapper.convertValue(jsonObj, CXFMBeanResult.class);
						cxfMBeanResultList.add(cxfMBeanResult);
					} catch (J4pException e) {
						LOGGER.error(getClass().getName()+" :: getCxfMBean :::: error", e);
						throw new ServiceException("Server Connection Error", e);
					} catch (MalformedObjectNameException e) {
						LOGGER.error(getClass().getName()+" :: getCxfMBean :::: error", e);
						throw new ServiceException("Malformed Url Error", e);
					} catch (IOException e) {
						LOGGER.error(getClass().getName()+" :: getCxfMBean :::: error", e);
						throw new ServiceException("IO Connection Error", e);
					}
				//}
			}
		//}
		return cxfMBeanResultList;
	}

	public String getCxfEndpointURL(CXFMBeanInfo cxfMBeanInfo)
			throws ServiceException {
		String url = null;
		if(cxfMBeanInfo != null){
			String baseUrl = configService.getBaseUrl().replace("/hawtio/jolokia", "");
//			String baseUrl = ContextListener.getBaseUrlScheme()+"://"+hostname+":"+ContextListener.getBaseUrlPort();
			String type ="";
			if(cxfMBeanInfo.isWsdl()){
				type="?wsdl";
			}else{
				type="?_wadl";
			}
			url= baseUrl+cxfMBeanInfo.getServletContext()+cxfMBeanInfo.getAddress()+type;
			
		}
		return url;
	}
	
	public List<String> getOperationList(String serviceUrl, boolean isWSDL) throws ServiceException{
		if(isWSDL){
			return getOperationListForWSDL(serviceUrl,isWSDL);
		}else{
			return getOperationListForWADL(serviceUrl,isWSDL);
		}
	}
	
	private List<String> getOperationListForWSDL(String serviceUrl, boolean isWSDL) throws ServiceException{
		List<String> operationNameList = new ArrayList<String>();
		try{
			WsdlProject project = new WsdlProject();
			WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project,serviceUrl, true )[0];
			List<Operation> operationList =  iface.getOperationList();
			for(Operation op : operationList){
				System.out.println(op.getName());
				operationNameList.add(op.getName());
			}
		}catch(Exception ex){
			LOGGER.error(getClass().getName()+" :: getOperationListForWSDL :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}
		return operationNameList;
	}
	
	private List<String> getOperationListForWADL(String serviceUrl, boolean isWSDL) throws ServiceException{
		List<String> operationNameList = new ArrayList<String>();;
		try{
			WsdlProject project = new WsdlProject();
			project.setName("RestService");
		    RestService restService = (RestService)project.addNewInterface( project.getName(), RestServiceFactory.REST_TYPE );
		    new WadlImporter(restService).initFromWadl(serviceUrl);
		    List<RestResource> list =  restService.getResourceList();
		    for(RestResource restResource : list){
		    	if(!"/api-docs".equals(restResource.getPath())){
			    	RestResource[] childRestResources = restResource.getAllChildResources();
			    	for(RestResource childRestResource : childRestResources){
			    		operationNameList.add(childRestResource.getName());
			    	}
		    	}
		    }
		}catch(Exception ex){
			LOGGER.error(getClass().getName()+" :: getOperationListForWADL :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}
		return operationNameList;
	}
	
	/*public String testServiceOperation(String serviceUrl, String operationName, String requestString, boolean isWSDL) throws ServiceException{
		if(isWSDL){
			return testServiceOperationForWSDL(serviceUrl, operationName, requestString);
		}else{
			return testServiceOperationForWADL(serviceUrl, operationName, requestString);
		}
	}*/
	
	public String testServiceOperationForWSDL(String serviceUrl, String operationName, String requestString) throws ServiceException{
		String responseString = null;
		try{
			WsdlProject project = new WsdlProject();
			WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project,serviceUrl, true )[0];
			WsdlOperation operation = (WsdlOperation) iface.getOperationByName(operationName);
			WsdlRequest request = operation.addNewRequest("SOAP Request");
			request.setRequestContent(requestString);
			WsdlSubmit<AbstractHttpRequestInterface<?>> submit = (WsdlSubmit) request.submit( new WsdlSubmitContext(request), false );
			Response response = submit.getResponse();
			responseString = response.getContentAsString();
		}catch(Exception ex){
			LOGGER.error(getClass().getName()+" :: testServiceOperationForWSDL :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}
		return responseString;
	}
	
	/*private String testServiceOperationForWADL(String serviceUrl, String operationName, String requestString) throws ServiceException{
		String responseString = null;
		try{
			
		}catch(Exception ex){
			LOGGER.error(getClass().getName()+" :: testServiceOperationForWADL :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}
		return responseString;
	}*/
	
	public String getSOAPRequest(String serviceUrl, String operationName) throws ServiceException{
		String soapRequest = null;
		try{
			WsdlProject project = new WsdlProject();
			WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project,serviceUrl, true )[0];
			WsdlOperation operation = (WsdlOperation) iface.getOperationByName(operationName);
			soapRequest = operation.createRequest(true);
		}catch(Exception ex){
			LOGGER.error(getClass().getName()+" :: makeSOAPRequest :::: error", ex);
			throw new ServiceException("Server Connection Error", ex);
		}
		return soapRequest;
	}

}
