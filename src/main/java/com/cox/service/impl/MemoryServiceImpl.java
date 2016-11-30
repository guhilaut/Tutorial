package com.cox.service.impl;

import java.io.IOException;

import javax.management.MalformedObjectNameException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.exception.ServiceException;
import com.cox.service.MemoryService;
import com.cox.service.model.MemoryInfoBean;
import com.cox.service.model.OperatingSystemInfoBean;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MemoryServiceImpl implements MemoryService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryServiceImpl.class);

	@Autowired
	private Configuration configService;
	private static final ObjectMapper mapper = new ObjectMapper();

	public MemoryInfoBean getAllMemory() throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getAllMemory called.");
		}
		try{
			String baseUrl = configService.getBaseUrl();
			String objectName = "java.lang:type=Memory";
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objectName,"HeapMemoryUsage", "NonHeapMemoryUsage");
			J4pReadResponse response = client.execute(request);
			JSONObject obj = response.asJSONObject();
			String str = obj.toJSONString();
			if(LOGGER.isDebugEnabled()){
				LOGGER.info("memory object returned >>> "+str);
			}
			((CloseableHttpClient)client.getHttpClient()).close();
			MemoryInfoBean memoryInfoBean = mapper.convertValue(obj, MemoryInfoBean.class);
			return memoryInfoBean;
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
	 
	public OperatingSystemInfoBean getOperatingSystemInfo() throws ServiceException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getOperatingSystemInfo called.");
		}
		try{
			String baseUrl = configService.getBaseUrl();
			String objectName = "java.lang:type=OperatingSystem";
			J4pClient client = J4pClient.url(baseUrl).user(configService.getUser().getName()).password(configService.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			J4pReadRequest request = new J4pReadRequest(objectName,"SystemCpuLoad","TotalPhysicalMemorySize","FreePhysicalMemorySize");
			J4pReadResponse response = client.execute(request);
			JSONObject obj = response.asJSONObject();
			String str = obj.toJSONString();
			if(LOGGER.isDebugEnabled()){
				LOGGER.info("osMemory object returned >>> "+str);
			}
			((CloseableHttpClient)client.getHttpClient()).close();
			OperatingSystemInfoBean cpuLoadInfoBean = mapper.convertValue(obj, OperatingSystemInfoBean.class);
			return cpuLoadInfoBean;
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

}
