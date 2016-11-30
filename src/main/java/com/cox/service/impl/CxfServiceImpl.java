package com.cox.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.management.MalformedObjectNameException;

import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pResponse;
import org.jolokia.client.request.J4pSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CxfServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceServiceImpl.class);

	@Autowired
	private Configuration configService;
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static void main(String[] args) throws MalformedObjectNameException, J4pException {
		loadCxfInfoForBundle("test");
	}
	public static void loadCxfInfoForBundle(String bundleName) throws MalformedObjectNameException, J4pException{
		J4pClient client = J4pClient.url("http://10.62.61.241:8181/jolokia").user("admin").password("admin").authenticator(new BasicAuthenticator().preemptive()).build();
		J4pSearchRequest request = new J4pSearchRequest("io.fabric8.cxf:*,type=Bus.Service.Endpoint");
		J4pResponse response = client.execute(request);
		System.out.println("Response === "+response.asJSONObject());
		List<String> servicesMBeans = mapper.convertValue(response.getValue(), List.class);
		bundleName = "com.cox.bis.customerinquireservices";
		List<String> cutomerServices = new ArrayList<String>();
		for(String str : servicesMBeans){
			if(str.contains(bundleName)){
				cutomerServices.add(str);
			}
		}
		for(String str : cutomerServices){
			System.out.println("MBeans >> "+str);
		}
	}
}
