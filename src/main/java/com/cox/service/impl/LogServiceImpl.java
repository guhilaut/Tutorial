package com.cox.service.impl;

import java.io.IOException;

import javax.management.MalformedObjectNameException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pExecResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.exception.ServiceException;
import com.cox.service.LogService;
import com.cox.service.model.LogFilter;
import com.cox.service.model.LogValue;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private Configuration configuration;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public LogValue getLogTail(int line) throws ServiceException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+" :: getLogTail");
		}
		try{
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			/*J4pExecRequest request = new J4pExecRequest("io.fabric8.insight:type=LogQuery","getLogResults", line);*/
			J4pExecRequest request = new J4pExecRequest("io.fabric8.insight:type=LogQuery","allLogResults");
			J4pExecResponse response = client.execute(request);
			JSONObject value = response.getValue();
			((CloseableHttpClient)client.getHttpClient()).close();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("log tail :: "+value);
			}
			/*LogDetail logDetail = mapper.convertValue(obj, LogDetail.class);*/
			LogValue logValue = mapper.convertValue(value, LogValue.class);
			return logValue;
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: getLogTail :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: getLogTail :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: getLogTail :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	public LogValue filterLogEvents(LogFilter logFilter) throws ServiceException{
		try{
			String filter = mapper.writeValueAsString(logFilter);
			System.out.println("Filter :: "+filter);
			String baseUrl = configuration.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl).user(configuration.getUser().getName()).password(configuration.getUser().getPassword()).authenticator(new BasicAuthenticator().preemptive()).build();
			/*J4pExecRequest request = new J4pExecRequest("io.fabric8.insight:type=LogQuery","filterLogEvents", filter);*/
			J4pExecRequest request = new J4pExecRequest("io.fabric8.insight:type=LogQuery","jsonQueryLogResults", filter);
			J4pExecResponse response = client.execute(request);
			/*String value = response.getValue();*/
			JSONObject value = response.getValue();
			((CloseableHttpClient)client.getHttpClient()).close();
			if(LOGGER.isDebugEnabled()){
				LOGGER.info("filtered logs :: "+value);
			}
			LogValue logValue = mapper.convertValue(value, LogValue.class);
			return logValue;
		} catch(J4pException e){
			LOGGER.error(getClass().getName()+" :: filterLogEvents :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()+" :: filterLogEvents :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()+" :: filterLogEvents :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
}
