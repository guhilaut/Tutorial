package com.cox.util;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pExecResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchRequestClient implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchRequestClient.class);
			
	private List<J4pExecResponse> responseList;
	private List<J4pExecRequest> requestList;
	private CountDownLatch latch;
	private J4pClient client;
	
	public BatchRequestClient(J4pClient client, List<J4pExecRequest> requestList, List<J4pExecResponse> responseList, CountDownLatch latch){
		this.client = client;
		this.responseList = responseList;
		this.requestList = requestList;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			LOGGER.info(Thread.currentThread().getName()+" executing run method");
			List<J4pExecResponse> responses = client.execute(requestList);
			LOGGER.info(Thread.currentThread().getName()+" got response >> "+responses);
			responseList.addAll(responses);
		} catch (J4pException e) {
			LOGGER.error(getClass().getName()+" :: run :::: error", e);
		} finally{
			latch.countDown();
		}
	}

}
