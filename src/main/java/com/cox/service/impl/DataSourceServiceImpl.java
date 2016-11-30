package com.cox.service.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import com.cox.service.DataSourceService;
import com.cox.service.model.ConnectionModel;
import com.cox.service.model.ConnectionProperty;
import com.cox.service.model.DBSearchModel;
import com.cox.service.model.DbPropsInfo;
import com.cox.service.model.MConfigDetail;
import com.cox.service.model.User;
import com.cox.view.model.CheckConnectionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DataSourceServiceImpl implements DataSourceService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceServiceImpl.class);

	@Autowired
	private Configuration configService;
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public ConnectionModel checkDBConnection(String objName) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: checkDBConnection :::: params {}", objName);
		}
		try{
			String baseUrl = configService.getBaseUrl();
			String objectName = objName+",connectionpool=connections";
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			/*J4pReadRequest request = new J4pReadRequest(objectName,"MaxTotal", "MaxIdle", "MinIdle", "NumActive", "NumIdle");*/
			J4pReadRequest request = new J4pReadRequest(objectName,"Closed","DestroyedByBorrowValidationCount","NumWaiters","BorrowedCount","ReturnedCount","CreatedCount","MaxTotal","MaxIdle","MinIdle","MaxWaitMillis","TestOnCreate","TestOnBorrow","TestOnReturn","TestWhileIdle","NumActive","NumIdle","LogAbandoned","TimeBetweenEvictionRunsMillis","NumTestsPerEvictionRun","MinEvictableIdleTimeMillis","RemoveAbandonedOnBorrow","RemoveAbandonedOnMaintenance","RemoveAbandonedTimeout","AbandonedConfig","BlockWhenExhausted","DestroyedCount","DestroyedByEvictorCount","MeanActiveTimeMillis","MeanIdleTimeMillis","MeanBorrowWaitTimeMillis","MaxBorrowWaitTimeMillis","CreationStackTrace");
			J4pReadResponse response = client.execute(request);
			JSONObject obj = response.asJSONObject();
			((CloseableHttpClient)client.getHttpClient()).close();
			ConnectionModel model = mapper.convertValue(obj, ConnectionModel.class);
			ConnectionProperty connectionProperty = getConnectionProperty(objName);
			model.getValue().put("UserName", connectionProperty.getDbProperty().getUsername());
			model.getValue().put("Url", connectionProperty.getDbProperty().getUrl());
			return model;
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: checkDBConnection :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: checkDBConnection :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: checkDBConnection :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		}
	}
	
	public DBSearchModel searchDataSource() throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: searchDataSource");
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pSearchRequest request = new J4pSearchRequest("*:type=BasicDataSource");
			J4pSearchResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			DBSearchModel model = mapper.convertValue(obj, DBSearchModel.class);
			return model;
		}catch(MalformedObjectNameException e){
			LOGGER.error(getClass().getName()+" :: searchDataSource :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: searchDataSource :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: searchDataSource :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public CheckConnectionResponse testConnection(String objName, String type) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: testConnection :::: params {}", objName);
		}
		if(type == null){
			throw new ServiceException("Database type should not be null...");
		}
		String query = null;
		if("ORACLE".equalsIgnoreCase(type)){
			query = "select 1 from dual";
		}else if("SQL_SERVER".equalsIgnoreCase(type)){
			query = "select 1";
		}else{
			throw new ServiceException("Invalid type is passed, no query is configured to check connection!!!");
		}
		CheckConnectionResponse checkConnectionResponse = new CheckConnectionResponse();
		ConnectionProperty connectionProperty = getConnectionProperty(objName);
		String url = connectionProperty.getDbProperty().getUrl();
		String user = connectionProperty.getDbProperty().getUsername();
		String pass = connectionProperty.getDbProperty().getPassword();
		boolean success = false;
		try {
			Connection con = DriverManager.getConnection(url, user, pass);
			Statement stat = con.createStatement();
			success = stat.execute(query);
			checkConnectionResponse.setFlag(success);
			checkConnectionResponse.setUrl(url);
		} catch (SQLException e) {
			checkConnectionResponse.setMessage(e.getMessage());
			checkConnectionResponse.setFlag(success);
			checkConnectionResponse.setUrl(url);
			return checkConnectionResponse;
		}
		return checkConnectionResponse;
	}
	
	public ConnectionProperty getConnectionProperty(String objName) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getConnectionProperty :::: params {}", objName);
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objName,"Url", "Username", "Password");
			J4pReadResponse response = client.execute(request);
			JSONObject obj = response.asJSONObject();
			((CloseableHttpClient)client.getHttpClient()).close();
			ConnectionProperty connectionProperty = mapper.convertValue(obj, ConnectionProperty.class);
			return connectionProperty;
		}catch(MalformedObjectNameException e){
			LOGGER.error(getClass().getName()+" :: getConnectionProperty :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getConnectionProperty :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getConnectionProperty :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	/*public List<String> checkDSDependent(String objName) throws MalformedObjectNameException, J4pException, IOException, ServiceException{
		List<String> dependentBundleList = new ArrayList<String>();
		ConnectionProperty connectionProperty = getConnectionProperty(objName);
		String url = connectionProperty.getDbProperty().getUrl();
		List<String> fileNames = configService.checkEntryInConfigFiles(url);
		Map<Integer, String> bundleMap = configService.getBundleMap();
		
		for(String fileName : fileNames){
			for(Entry<Integer, String> entry : bundleMap.entrySet()){
				if(fileName.contains(entry.getValue())){
					dependentBundleList.add(entry.getValue());
				}
			}
		}
		
		return dependentBundleList;
	}*/
	
	public List<String> getDSDependent(String objName) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getDSDependent :::: params {}", objName);
		}
		List<String> dependentBundleList = new ArrayList<String>();
		//get the connection url of this DB object
		ConnectionProperty connectionProperty = getConnectionProperty(objName);
		String url = connectionProperty.getDbProperty().getUrl();
		
		//contruct the other bundles object name list
		Map<Integer, String> bundleMap = configService.getBundleMap();
		List<String> bundleNameList = new ArrayList<String>(bundleMap.values());
		
		//get the properties of all the bundles
		List<MConfigDetail> mConfigDetailList = getPropertiesList(bundleNameList);
		
		//if url exist in this properties list add to dependentBundleList
		
		for(MConfigDetail mConfigDetail : mConfigDetailList){
			List<String> propertyValueList = new ArrayList<String>(mConfigDetail.getProperties().values());
			if(propertyValueList.contains(url)){
				dependentBundleList.add(mConfigDetail.getRequest().getObjectName());
			}
		}
		
		return dependentBundleList;
	}
	
	public List<MConfigDetail> getPropertiesList(List<String> bundleNameList) throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getPropertiesList :::: params {}", bundleNameList);
		}
		try{
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			String objectName = "org.apache.karaf:type=config,name=root";
			List<J4pExecRequest> requestList = new ArrayList<J4pExecRequest>();
			for(String bundleName : bundleNameList){
				J4pExecRequest request = new J4pExecRequest(objectName,"listProperties", bundleName);
				requestList.add(request);
			}
			List<J4pExecResponse> responseList = client.execute(requestList);
			List<MConfigDetail> mConfigDetailList = new ArrayList<MConfigDetail>();
			for(J4pExecResponse response: responseList){
				JSONObject obj = response.asJSONObject();
				MConfigDetail mConfigDetail = mapper.convertValue(obj, MConfigDetail.class);
				mConfigDetailList.add(mConfigDetail);
			}
			((CloseableHttpClient)client.getHttpClient()).close();
			
			return mConfigDetailList;
		}catch(MalformedObjectNameException e){
			LOGGER.error(getClass().getName()+" :: getPropertiesList :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		}catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getPropertiesList :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getPropertiesList :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public CheckConnectionResponse testAs400Connection(DbPropsInfo dbPropsInfo) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: testAs400Connection");
		}
		CheckConnectionResponse checkConnectionResponse = new CheckConnectionResponse();
		boolean isConnected = false;
		try{
			final String DRIVER = dbPropsInfo.getDriverClassName();
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(dbPropsInfo.getConnectionUrl(), dbPropsInfo.getUsername(), dbPropsInfo.getPassword()); 
			Statement stat = conn.createStatement();
			isConnected = stat.execute("SELECT 1 FROM sysibm.sysdummy1");
			conn.close();
			checkConnectionResponse.setFlag(isConnected);
			checkConnectionResponse.setUrl(dbPropsInfo.getConnectionUrl());
		}catch(Exception e){
			LOGGER.error(getClass().getName()+" :: testAs400Connection caught error", e);
			checkConnectionResponse.setFlag(isConnected);
			checkConnectionResponse.setMessage(e.getMessage());
			checkConnectionResponse.setUrl(dbPropsInfo.getConnectionUrl());
		}
		return checkConnectionResponse;
	}
	
	public CheckConnectionResponse testICOMConnection(DbPropsInfo dbPropsInfo){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: testICOMConnection");
		}
		CheckConnectionResponse checkConnectionResponse = new CheckConnectionResponse();
		String hostPort = dbPropsInfo.getConnectionUrl();
		if(hostPort.contains("https://")){
			hostPort = hostPort.substring(hostPort.lastIndexOf("/")+1);
		}
		
		String[] splitted = hostPort.split(":");
		String host = splitted[0];
		String portStr = splitted[1];
		int port = Integer.parseInt(portStr.trim());
		
		Socket socket = null;
		boolean reachable = false;
		try {
		    socket = new Socket(host, port);
		    reachable = true;
		    checkConnectionResponse.setFlag(reachable);
		    checkConnectionResponse.setUrl(hostPort);
		} catch (UnknownHostException e) {
			LOGGER.error(getClass().getName()+" :: testICOMConnection caught error", e);
			checkConnectionResponse.setFlag(reachable);
			checkConnectionResponse.setMessage(e.toString());
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: testICOMConnection caught error", e);
			checkConnectionResponse.setFlag(reachable);
			checkConnectionResponse.setMessage(e.toString());
		} finally {            
		    if (socket != null) try { socket.close(); } catch(IOException e) {}
		}
		return checkConnectionResponse;
	}

	public int getDBCount(User user, String jolokiaPath) throws ServiceException {
//		String baseUrl = ContextListener.getBaseUrlScheme()+"://"+ip+":"+ContextListener.getBaseUrlPort()+"/jolokia";
		int count = 0;
		try{
			J4pClient client = J4pClient.url(jolokiaPath).user(user.getName()).password(user.getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pSearchRequest request = new J4pSearchRequest("*:type=BasicDataSource");
			J4pSearchResponse response = client.execute(request);
			((CloseableHttpClient)client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			DBSearchModel model = mapper.convertValue(obj, DBSearchModel.class);
			count = model.getObjectNames().size();
		} catch(MalformedObjectNameException e){
			LOGGER.error(getClass().getName()+" :: getDBCount :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getDBCount :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getDBCount :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
		return count;
	}
}
