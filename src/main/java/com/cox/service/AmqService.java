package com.cox.service;

import java.util.List;
import java.util.Map;

import com.cox.exception.ServiceException;
import com.cox.service.model.AmqBrokerResult;
import com.cox.service.model.AmqMessageResult;
import com.cox.service.model.Consumer;
import com.cox.service.model.DestinationDetailResult;
import com.cox.service.model.JmsMessageResult;
import com.cox.service.model.MBeanSearchResult;
import com.cox.service.model.SubscriberDetails;
import com.cox.view.model.ObjectNames;
import com.cox.view.model.Subscriber;


public interface AmqService {

	public MBeanSearchResult getBrokerName() throws ServiceException;
	public AmqBrokerResult brokerDetails(String brokerName) throws ServiceException;
	public List<DestinationDetailResult> getDestinationDetails(List<ObjectNames> destinationObjNames) throws ServiceException;
	public void addDestination(String amqObjName, String destinationName, boolean isQueue) throws ServiceException;
	public void removeDestination(String amqObjName, String destinationName, boolean isQueue) throws ServiceException;
	public void purgeQueue(String queueObjName) throws ServiceException;
	public List<SubscriberDetails> getSubscriberDetails(List<ObjectNames> subscriberNameList) throws ServiceException;
	public void createDurableSubscriber(String amqObjName, String client, String subscriberName, String topicName, String selector) throws ServiceException;
	public void destroyDurableSubscriber(String amqObjName, String client, String subscriberName) throws ServiceException;
	public void sendMsg(String objectName, Map<String, String> map, String message, int count) throws ServiceException;
	public AmqMessageResult browse(String objName) throws ServiceException;
	public JmsMessageResult browseMsg(String objName, String msgId) throws ServiceException;
	public void removeMsg(String objName, String msgId) throws ServiceException;
	public List<Consumer> getConsumerDetails(List<ObjectNames> consumerObjList) throws ServiceException;
	public List<Subscriber> getSubscribersForTopic(String stringToSearch) throws ServiceException;

}
