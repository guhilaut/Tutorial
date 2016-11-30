package com.cox.service.impl;

import java.io.IOException;
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
import com.cox.service.AmqService;
import com.cox.service.model.AmqBrokerResult;
import com.cox.service.model.AmqMessageResult;
import com.cox.service.model.Consumer;
import com.cox.service.model.ConsumerDetails;
import com.cox.service.model.DestinationDetailResult;
import com.cox.service.model.JmsMessageResult;
import com.cox.service.model.MBeanSearchResult;
import com.cox.service.model.SubscriberDetailResult;
import com.cox.service.model.SubscriberDetails;
import com.cox.view.model.ObjectNames;
import com.cox.view.model.Subscriber;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AmqServiceImpl implements AmqService {

	@Autowired
	private Configuration configService;
	
	@Autowired
	private CommonService commonService;

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AmqServiceImpl.class);

	// find broker
	@Override
	public MBeanSearchResult getBrokerName() throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName() + " :: getBrokerName");
		}

		try {
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pSearchRequest request = new J4pSearchRequest(
					"org.apache.activemq:type=Broker,brokerName=*");
			J4pSearchResponse response = client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();
			JSONObject jsonObject = response.asJSONObject();
			MBeanSearchResult result = null;
			result = mapper.convertValue(jsonObject, MBeanSearchResult.class);
			return result;
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: getAllMBeans :::: error",
					e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: getAllMBeans :::: error",
					e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: getAllMBeans :::: error",
					e);
			throw new ServiceException("IO Connection Error", e);
		}

	}

	// broker details
	public AmqBrokerResult brokerDetails(String brokerName)
			throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName() + " :: brokerDetails");
		}
		try {
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pReadRequest request = new J4pReadRequest(brokerName,
					"BrokerName", "BrokerId", "BrokerVersion", "Uptime",
					"StorePercentUsage", "MemoryPercentUsage",
					"TempPercentUsage", "Queues", "Topics");
			J4pReadResponse response = client.execute(request);
			JSONObject obj = response.asJSONObject();
			((CloseableHttpClient) client.getHttpClient()).close();
			AmqBrokerResult mBeanListResult = mapper.convertValue(obj,
					AmqBrokerResult.class);
			return mBeanListResult;
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: brokerDetails :::: error",
					e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: brokerDetails :::: error",
					e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: brokerDetails :::: error",
					e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	public void purgeQueue(String queueObjName) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName() + " :: purgeQueue");
		}

		try {
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = new J4pExecRequest(queueObjName, "purge");
			client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: purgeQueue :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: purgeQueue :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: purgeQueue :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}

	public void sendMsg(String objectName, Map<String, String> jmsHeader,
			String message, int count) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName() + " :: sendMsg");
		}
		try {
			String baseUrl = configService.getBaseUrl();
			J4pExecRequest request = null;
			J4pClient client = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			if (jmsHeader != null) {
				String headers = mapper.writeValueAsString(jmsHeader);
				request = new J4pExecRequest(objectName,
						"sendTextMessage(java.util.Map, java.lang.String, java.lang.String, java.lang.String)",
						headers, message, configService.getUser().getName(), configService.getUser().getPassword());
			} else {
				request = new J4pExecRequest(objectName,
						"sendTextMessage(java.lang.String, java.lang.String, java.lang.String)", message, configService.getUser().getName(), configService.getUser().getPassword());
			}
			for(int i=0; i<count; i++){
				client.execute(request);
			}
			((CloseableHttpClient) client.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: sentToQueue :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: sentToQueue :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: sentToQueue :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}

	@Override
	public AmqMessageResult browse(String objName) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName() + " :: browse");
		}
		try {
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = new J4pExecRequest(objName,
					"browseAsTable()");
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			AmqMessageResult result = mapper.convertValue(obj,
					AmqMessageResult.class);
			return result;
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: browse :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: browse :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: browse :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}

	@Override
	public JmsMessageResult browseMsg(String objName, String msgId)
			throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName() + " :: browse");
		}
		try {
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = new J4pExecRequest(objName, "getMessage",
					msgId);
			J4pExecResponse response = client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();
			JSONObject obj = response.asJSONObject();
			//LOGGER.info("list of msg >>>>> " + obj.toJSONString());
			JmsMessageResult result = mapper.convertValue(obj,
					JmsMessageResult.class);
			return result;

		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: browse :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: browse :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: browse :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}

	@Override
	public List<DestinationDetailResult> getDestinationDetails(
			List<ObjectNames> destinatinObjList) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName()
					+ " :: getQueueDetails :::: params :: objName = {}",
					destinatinObjList);
		}
		try {
			List<DestinationDetailResult> destinationDetailsResultList = new ArrayList<>();
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			List<J4pReadRequest> reqList = new ArrayList<>();
			for (ObjectNames destinationObjName : destinatinObjList) {
				reqList.add(new J4pReadRequest(destinationObjName
						.getObjectName(), "Name", "QueueSize", "EnqueueCount",
						"DequeueCount", "ProducerCount", "ConsumerCount","Subscriptions"));
			}
			List<J4pReadResponse> respList = client.execute(reqList);
			for (J4pReadResponse response : respList) {
				JSONObject obj = response.asJSONObject();
				((CloseableHttpClient) client.getHttpClient()).close();
				DestinationDetailResult destinationDetailResult = mapper
						.convertValue(obj, DestinationDetailResult.class);
				destinationDetailResult.getDestinationDetails().setObjName(
						destinationDetailResult.getRequest().getMbean());
				destinationDetailsResultList.add(destinationDetailResult);
			}
			return destinationDetailsResultList;
		} catch (J4pException e) {
			LOGGER.error(getClass().getName()
					+ " :: getQueueDetails :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()
					+ " :: getQueueDetails :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()
					+ " :: getQueueDetails :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	@Override
	public void addDestination(String amqObjName, String destinationName,
			boolean isQueue) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					getClass().getName()
							+ " :: addDestination :::: params :: amqObjName = {0} :: destinationName = {1}",
					amqObjName, destinationName);
		}
		try {
			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = null;
			if (isQueue) {
				request = new J4pExecRequest(amqObjName, "addQueue",
						destinationName);
			} else {
				request = new J4pExecRequest(amqObjName, "addTopic",
						destinationName);
			}
			client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(
					getClass().getName() + " :: addDestination :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(
					getClass().getName() + " :: addDestination :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(
					getClass().getName() + " :: addDestination :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}

	@Override
	public void removeDestination(String amqObjName, String destinationName,
			boolean isQueue) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					getClass().getName()
							+ " :: removeDestination :::: params :: amqObjName = {0} :: destinationName = {1}",
					amqObjName, destinationName);
		}
		try {

			String baseUrl = configService.getBaseUrl();
			J4pClient client = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = null;
			if (isQueue) {
				request = new J4pExecRequest(amqObjName, "removeQueue",
						destinationName);
			} else {
				request = new J4pExecRequest(amqObjName, "removeTopic",
						destinationName);
			}

			client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName()
					+ " :: removeDestination :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()
					+ " :: removeDestination :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()
					+ " :: removeDestination :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}
	
	
	
	@Override
	public void createDurableSubscriber(String amqObjName, String client,
			String subscriberName, String topicName, String selector)
			throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					getClass().getName()
							+ " :: createDurableSubscriber :::: params :: amqObjName = {0} :: client = {1} :: subscriberName = {2} :: topicName = {3} :: selector = {4}",
					amqObjName, client, subscriberName, topicName, selector);
		}
		try {
			String baseUrl = configService.getBaseUrl();
			J4pClient jclient = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = new J4pExecRequest(amqObjName,
					"createDurableSubscriber", client, subscriberName,
					topicName, selector);
			jclient.execute(request);
			((CloseableHttpClient) jclient.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName()
					+ " :: createDurableSubscriber :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()
					+ " :: createDurableSubscriber :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()
					+ " :: createDurableSubscriber :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}
	
	@Override
	public List<Subscriber> getSubscribersForTopic(String stringToSearch) throws ServiceException{
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName()+" :: getSubscribersForTopic :::: stringToSearch :: "+stringToSearch);
		}
		List<Subscriber> subscribers = new ArrayList<>();
		//org.apache.activemq:brokerName=brokerN,type=Broker,destinationName=A,destinationType=Topic,endpoint=Consumer,*
		List<String> subscriberObjNames = commonService.searchMBeans(stringToSearch);
		if(subscriberObjNames != null){
			
			List<ObjectNames> objNameList = new ArrayList<>(subscriberObjNames.size());
			for(String subscriberName : subscriberObjNames){
				ObjectNames objName = new ObjectNames();
				objName.setObjectName(subscriberName);
				objNameList.add(objName);
			}
			
			List<SubscriberDetails> subscriberList = getSubscriberDetails(objNameList);
			
			for(SubscriberDetails subscriberDetails: subscriberList){
				Subscriber subscriber = new Subscriber();
				subscriber.setActive(subscriberDetails.isActive());
				subscriber.setClientId(subscriberDetails.getClientId());
				subscriber.setDequeueCounter(subscriberDetails.getDequeueCounter());
				subscriber.setDestinationName(subscriberDetails.getDestinationName());
				subscriber.setEnqueueCounter(subscriberDetails.getEnqueueCounter());
				subscriber.setInflight(subscriberDetails.getDispatchedCounter()); // inflight UI property
				subscriber.setNetwork(subscriberDetails.isNetwork());
				subscriber.setPendingQueueSize(subscriberDetails.getPendingQueueSize());
				subscriber.setPrefetchSize(subscriberDetails.getPrefetchSize());
				subscriber.setSelector(subscriberDetails.getSelector());
				subscriber.setSessionId(subscriberDetails.getSessionId());
				subscriber.setSubscriptionId(subscriberDetails.getSubscriptionId());
				subscriber.setSubscriptionName(subscriberDetails.getSubscriptionName());
				subscriber.setObjectName(subscriberDetails.getObjectName());
				subscribers.add(subscriber);
			}
		}
		return subscribers;
	}

	@Override
	public void destroyDurableSubscriber(String amqObjName, String client,
			String subscriberName) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					getClass().getName()
							+ " :: destroyDurableSubscriber :::: params :: amqObjName = {0} :: client = {1} :: subscriberName = {2} ",
					amqObjName, client, subscriberName);
		}
		try {
			String baseUrl = configService.getBaseUrl();
			J4pClient jclient = J4pClient.url(baseUrl)
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = new J4pExecRequest(amqObjName,
					"destroyDurableSubscriber", client, subscriberName);
			System.out.println(jclient.execute(request).asJSONObject()
					.toJSONString());
			((CloseableHttpClient) jclient.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName()
					+ " :: destroyDurableSubscriber :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()
					+ " :: destroyDurableSubscriber :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()
					+ " :: destroyDurableSubscriber :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	public void stop(String amqObjName) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName()
					+ " :: stop :::: params :: amqObjName = {0}", amqObjName);
		}
		try {
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = new J4pExecRequest(amqObjName, "stop");
			System.out.println(client.execute(request).asJSONObject()
					.toJSONString());
			((CloseableHttpClient) client.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: stop :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: stop :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: stop :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	public void start(String amqObjName) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName()
					+ " :: start :::: params :: amqObjName = {0}", amqObjName);
		}
		try {
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pExecRequest request = new J4pExecRequest(amqObjName, "start");
			// jclient.execute(request);
			System.out.println(client.execute(request).asJSONObject()
					.toJSONString());
			((CloseableHttpClient) client.getHttpClient()).close();

		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: start :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: start :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: start :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

	@Override
	public void removeMsg(String objName, String msgId) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName()
			+ " :: removeMsg :::: params :: objName = {0} :: msgId :: {1}",
					objName);
		}
		try {
			J4pClient client = J4pClient.url(configService.getBaseUrl())
			.user(configService.getUser().getName())
			.password(configService.getUser().getPassword())
			.authenticator(new BasicAuthenticator().preemptive())
			.build();
			J4pExecRequest request = new J4pExecRequest(objName,
					"removeMessage", msgId);
			// jclient.execute(request);
			System.out.println(client.execute(request).asJSONObject()
			.toJSONString());
			((CloseableHttpClient) client.getHttpClient()).close();
		} catch (J4pException e) {
			LOGGER.error(getClass().getName() + " :: removeMsg :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName() + " :: removeMsg :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName() + " :: removeMsg :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}

	}
	
	@Override
	public List<SubscriberDetails> getSubscriberDetails(List<ObjectNames> subscriberNameList) throws ServiceException{
		try {
			
			List<SubscriberDetails> subscriberList = new ArrayList<>();
			
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			
			List<J4pReadRequest> reqList = new ArrayList<>();
			for(ObjectNames subscriberName: subscriberNameList){
				reqList.add(new J4pReadRequest(subscriberName.getObjectName(),"Network","DestinationName","ClientId","SessionId","EnqueueCounter"
					,"SubcriptionId","Selector","PendingQueueSize","SubscriptionName","PrefetchSize","Active","DequeueCounter"));
			}
			
			List<J4pReadResponse> respList = client.execute(reqList);
			
			for (J4pReadResponse response : respList) {
				JSONObject obj = response.asJSONObject();
				((CloseableHttpClient) client.getHttpClient()).close();
				
				SubscriberDetailResult subscriberDetailResult = mapper
						.convertValue(obj, SubscriberDetailResult.class);
				subscriberDetailResult.getSubscriberDetails().setObjectName(subscriberDetailResult.getRequest().getMbean());
				subscriberList.add(subscriberDetailResult.getSubscriberDetails());
			}
			
			return subscriberList;
		} catch (J4pException e) {
			LOGGER.error(getClass().getName()
					+ " :: getSubscriberDetails :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()
					+ " :: getSubscriberDetails :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()
					+ " :: getSubscriberDetails :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	@Override
	public List<Consumer> getConsumerDetails(List<ObjectNames> consumerObjList) throws ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getClass().getName()+ " :: getConsumerDetails :::: params :: objName = {}",consumerObjList);
		}
		try {
			List<Consumer> listconsumerDetails = new ArrayList<>();
			J4pClient client = J4pClient.url(configService.getBaseUrl())
					.user(configService.getUser().getName())
					.password(configService.getUser().getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			List<J4pReadRequest> reqList = new ArrayList<>();
			for (ObjectNames destinationObjName : consumerObjList) {
				reqList.add(new J4pReadRequest(destinationObjName.getObjectName(), "Retroactive", "Exclusive", "ConnectionId",
						"MaximumPendingMessageLimit", "ClientId", "SessionId","EnqueueCounter","Selector","PrefetchSize","DequeueCounter","DispatchedCounter"));
			}
			List<J4pReadResponse> respList = client.execute(reqList);
			for (J4pReadResponse response : respList) {
				JSONObject obj = response.asJSONObject();
				((CloseableHttpClient) client.getHttpClient()).close();
				ConsumerDetails consumerDetails=mapper.convertValue(obj, ConsumerDetails.class);
				listconsumerDetails.add(consumerDetails.getValue());
			}
			return listconsumerDetails;
		} catch (J4pException e) {
			LOGGER.error(getClass().getName()
					+ " :: getConsumerDetails :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(getClass().getName()
					+ " :: getConsumerDetails :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(getClass().getName()
					+ " :: getConsumerDetails :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}
	
	

}
