package com.cox.service.impl;

import java.io.IOException;
import java.util.List;

import javax.management.MalformedObjectNameException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pSearchRequest;
import org.jolokia.client.request.J4pSearchResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.exception.ServiceException;
import com.cox.service.model.MBeanSearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommonService {
	
	@Autowired
	private Configuration configService;
	
	private static final ObjectMapper mapper = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommonService.class);
	
	
	public List<String> searchMBeans(String stringToSearch) throws ServiceException{
		long start = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName() + " :: searchMBeans");
		}

		try {
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pSearchRequest request = new J4pSearchRequest(stringToSearch);
			J4pSearchResponse response = client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();
			JSONObject jsonObject = response.asJSONObject();
			MBeanSearchResult result = null;
			result = mapper.convertValue(jsonObject, MBeanSearchResult.class);
			LOGGER.info(getClass().getName()+" :: searchMBeans: time to process = "+(start-System.currentTimeMillis()));
			return result.getValue();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: searchMBeans :::: error",
					e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: searchMBeans :::: error",
					e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: searchMBeans :::: error",
					e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

}
