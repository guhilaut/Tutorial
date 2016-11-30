package com.cox.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cox.controller.helper.CommonHelper;
import com.cox.exception.ServiceException;
import com.cox.service.AmqService;
import com.cox.service.model.AmqBrokerResult;
import com.cox.service.model.AmqMessage;
import com.cox.service.model.AmqMessageResult;
import com.cox.service.model.Consumer;
import com.cox.service.model.DestinationDetailResult;
import com.cox.service.model.DestinationDetails;
import com.cox.service.model.JmsMessageResult;
import com.cox.service.model.MBeanSearchResult;
import com.cox.service.model.SubscriberDetails;
import com.cox.view.model.AmqBrokerInfo;
import com.cox.view.model.AmqBrokerResponse;
import com.cox.view.model.AmqMessageResponse;
import com.cox.view.model.ConsumerResponse;
import com.cox.view.model.DestinationDetailsResponse;
import com.cox.view.model.ObjectNames;
import com.cox.view.model.JmsMessageResponse;
import com.cox.view.model.StatusResponse;
import com.cox.view.model.Subscriber;
import com.cox.view.model.SubscriberResponse;

@Controller
@RequestMapping(value="/amq")
public class AmqController {

	private static final Logger LOGGER=LoggerFactory.getLogger(AmqController.class);
	@Autowired
	private CommonHelper helper;
	@Autowired
	AmqService amqService;
	
	@RequestMapping(value = "/home/{url:.+}", method = RequestMethod.GET)
	public ResponseEntity<AmqBrokerResponse> getHomeBrokerDetails(@PathVariable String url, HttpServletResponse resp) throws IOException{
		ResponseEntity<AmqBrokerResponse> response = null;
		AmqBrokerResponse amqBrokerResponse = new AmqBrokerResponse();
		String brokerName=null;
		AmqBrokerResult amqBrokerResult = null;
		AmqBrokerInfo amqBrokerInfo;
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getBrokerDetails");
		}
		try{
			helper.loadConfigService(url);
			brokerName = searchBroker();
			if(brokerName != null){
				long start = System.currentTimeMillis();
				amqBrokerResult = amqService.brokerDetails(brokerName);
				LOGGER.info("Service - amqService: brokerDetails: time to process = "+(System.currentTimeMillis() - start));
				amqBrokerInfo = amqBrokerResult.getValue();
				amqBrokerInfo.setObjName(amqBrokerResult.getRequest().getMbean());
				amqBrokerResponse.setAmqBrokerInfo(amqBrokerInfo);
				amqBrokerResponse.setFlag(true);
				amqBrokerResponse.setMessage(null);
			}else{
				amqBrokerResponse.setFlag(false);
				amqBrokerResponse.setMessage("No Broker Found on this server!!!");
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getBrokerDetails error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<AmqBrokerResponse>(amqBrokerResponse, HttpStatus.OK);
		return response;
	}
	
	private String searchBroker() throws ServiceException {
		String brokerName = null;
		long start = System.currentTimeMillis();
		MBeanSearchResult amqList = amqService.getBrokerName();
		LOGGER.info("Service - amqService: getBrokerName: time to process = "+(System.currentTimeMillis() - start));
		//expecting only one broker instance in a particular server, with the same port.
		for (int i = 0; i < amqList.getValue().size(); i++) {
			if(amqList.getValue().get(i).contains("org.apache.activemq:brokerName=")){
				brokerName=amqList.getValue().get(i);
			}
		}
		return brokerName;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<DestinationDetailsResponse> getDestinationInfo(@RequestBody List<ObjectNames> objNameList, HttpServletResponse resp) throws IOException{
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: getDestinationInfo");
		}
		ResponseEntity<DestinationDetailsResponse> response = null;
		DestinationDetailsResponse destinationDetailsResponse = new DestinationDetailsResponse();
		List<DestinationDetails> destinationDetailsList = new ArrayList<DestinationDetails>();
		try{
			long start = System.currentTimeMillis();
			List<DestinationDetailResult> destinationDetailResultList = amqService.getDestinationDetails(objNameList);
			LOGGER.info("Service - amqService: getDestinationDetails: time to process = "+(System.currentTimeMillis() - start));
			if(destinationDetailResultList != null && destinationDetailResultList.size() > 0){
				for(DestinationDetailResult destinationDetailResult : destinationDetailResultList){
					DestinationDetails destinationDetails = destinationDetailResult.getDestinationDetails();
					destinationDetailsList.add(destinationDetails);
				}
				Collections.sort(destinationDetailsList);
				destinationDetailsResponse.setDestinationDetailsList(destinationDetailsList);
				destinationDetailsResponse.setFlag(true);
			}else{
				destinationDetailsResponse.setFlag(false);
				destinationDetailsResponse.setMessage("No Destination Found in this broker!!!");
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: getDestinationInfo error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<DestinationDetailsResponse>(destinationDetailsResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<StatusResponse> addDestination(@RequestParam String name, @RequestParam String brokerName, @RequestParam boolean isQueue, HttpServletResponse resp) throws IOException{
		ResponseEntity<StatusResponse> response=null;
		StatusResponse statusResponse=new StatusResponse();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: addDestination");
		}
		try{
			long start = System.currentTimeMillis();
			amqService.addDestination(brokerName, name, isQueue);
			LOGGER.info("Service - amqService: addDestination: time to process = "+(System.currentTimeMillis() - start));
			statusResponse.setFlag(true);
			statusResponse.setMessage("Destination "+name+" added successfully!!!");
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: addDestination error", e);
			statusResponse.setFlag(false);
			statusResponse.setMessage("Exception while adding destination : "+name+" Caused By: " + e);
		}
		response = new ResponseEntity<StatusResponse>(statusResponse,HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ResponseEntity<StatusResponse> removeDestination(@RequestParam String name, @RequestParam String brokerName, @RequestParam boolean isQueue, HttpServletResponse resp) throws IOException{
		ResponseEntity<StatusResponse> response=null;
		StatusResponse statusResponse=new StatusResponse();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: removeDestination");
		}
		try{
			long start = System.currentTimeMillis();
			amqService.removeDestination(brokerName, name, isQueue);
			LOGGER.info("Service - amqService: removeDestination: time to process = "+(System.currentTimeMillis() - start));
			statusResponse.setFlag(true);
			statusResponse.setMessage("Destination "+name+" deleted successfully!!!");
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: removeDestination error", e);
			statusResponse.setFlag(false);
			statusResponse.setMessage("Exception while removing destination : "+name+" Caused By: " + e);
		}
		response = new ResponseEntity<StatusResponse>(statusResponse,HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/purgeQueue", method = RequestMethod.POST)
	public ResponseEntity<StatusResponse> purgeQueue(@RequestParam String queueObjName, HttpServletResponse resp) throws IOException{
		ResponseEntity<StatusResponse> response=null;
		StatusResponse statusResponse=new StatusResponse();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: purgeQueue");
		}
		try{
			long start = System.currentTimeMillis();
			amqService.purgeQueue(queueObjName);
			LOGGER.info("Service - amqService: purgeQueue: time to process = "+(System.currentTimeMillis() - start));
			statusResponse.setFlag(true);
			statusResponse.setMessage("Queue purged successfully!!!");
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: purgeQueue error", e);
			statusResponse.setFlag(false);
			statusResponse.setMessage("Exception while purging queue, Caused By: " + e);
		}
		response = new ResponseEntity<StatusResponse>(statusResponse, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	public ResponseEntity<StatusResponse> sendMsg(@RequestParam String objName, @RequestParam String message, @RequestBody(required=false) Map<String, String> headers, HttpServletResponse resp) throws IOException{
		ResponseEntity<StatusResponse> response=null;
		StatusResponse statusResponse=new StatusResponse();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: sendMsg");
		}		
		String countStr = headers.get("JMSMessageCount");
		int count = 1;
		try{
			count = Integer.parseInt(countStr);
			if(count > 1000 || count <= 0){
				LOGGER.error(this.getClass().getName()+" :: sendMsg error :: message count is not in range (0,1000]");
				statusResponse.setFlag(false);
				statusResponse.setMessage("Exception while sending message, Caused By: invalid count value = "+count);
			}else{
				long start = System.currentTimeMillis();
				amqService.sendMsg(objName, headers, message, count);
				LOGGER.info("Service - amqService: sendMsg: time to process = "+(System.currentTimeMillis() - start));
				statusResponse.setFlag(true);
				String msg ="Topic";
				if(objName.contains("destinationType=Queue")){
					msg ="Queue";
				}
				statusResponse.setMessage(count+" message(s) sent to "+msg+" "+headers.get("JMSDestination")+" successfully!!!");
			}
		}catch(NumberFormatException ex){
			LOGGER.error(this.getClass().getName()+" :: sendMsg error", ex);
			statusResponse.setFlag(false);
			statusResponse.setMessage("Exception while sending message, Caused By: invalid count value = "+countStr);
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: sendMsg error", e);
			statusResponse.setFlag(false);
			statusResponse.setMessage("Exception while sending message, Caused By: " + e);
		}
		response = new ResponseEntity<StatusResponse>(statusResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public ResponseEntity<AmqMessageResponse> browse(@RequestParam String objName, HttpServletResponse resp) throws IOException{
		ResponseEntity<AmqMessageResponse> response = null;
		AmqMessageResponse amqMessageResponse = new AmqMessageResponse();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: browse");
		}
		try{
			long start = System.currentTimeMillis();
			AmqMessageResult amqMessageResult = amqService.browse(objName);
			LOGGER.info("Service - amqService: browse: time to process = "+(System.currentTimeMillis() - start));
			if(amqMessageResult != null && amqMessageResult.getValue() != null && amqMessageResult.getValue().size() > 0){
				List<AmqMessage> messageList = new ArrayList<AmqMessage>(amqMessageResult.getValue().values());
				amqMessageResponse.setMessageList(messageList);
				amqMessageResponse.setFlag(true);
			}else{
				amqMessageResponse.setFlag(false);
				amqMessageResponse.setMessage("No Message Found!!!");
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: browse error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<AmqMessageResponse>(amqMessageResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/browseMsg", method = RequestMethod.GET)
	public ResponseEntity<JmsMessageResponse> browseMsg(@RequestParam String objName, @RequestParam String msgId, HttpServletResponse resp) throws IOException{
		ResponseEntity<JmsMessageResponse> response = null;
		JmsMessageResponse jmsMessageResponse = new JmsMessageResponse();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(this.getClass().getName()+" :: browse");
		}
		try{
			long start = System.currentTimeMillis();
			JmsMessageResult jmsMessageResult = amqService.browseMsg(objName, msgId);
			LOGGER.info("Service - amqService: browseMsg: time to process = "+(System.currentTimeMillis() - start));
			if(jmsMessageResult != null && jmsMessageResult.getValue() != null){
				jmsMessageResponse.setMessageDetail(jmsMessageResult.getValue());
				jmsMessageResponse.setFlag(true);
			}else{
				jmsMessageResponse.setFlag(false);
				jmsMessageResponse.setMessage("No Message Found!!!");
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+" :: browse error", e);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<JmsMessageResponse>(jmsMessageResponse, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/removeMsg", method = RequestMethod.POST)
    public ResponseEntity<StatusResponse> removeMsg(@RequestParam String objName, @RequestParam String msgId) throws IOException{
            ResponseEntity<StatusResponse> response = null;
            StatusResponse statusResponse=new StatusResponse();
            if(LOGGER.isDebugEnabled()){
                    LOGGER.debug(this.getClass().getName()+" :: sendMsg");
            }
            try{
            	long start = System.currentTimeMillis();
                    amqService.removeMsg(objName, msgId);
                    LOGGER.info("Service - amqService: removeMsg: time to process = "+(System.currentTimeMillis() - start));
                    statusResponse.setFlag(true);
                    statusResponse.setMessage("Message removed successfully!!!");
            }catch(Exception e){
                    LOGGER.error(this.getClass().getName()+" :: sendMsg error", e);
                    statusResponse.setFlag(false);
                    statusResponse.setMessage("Exception while removing message, Caused By: " + e);
            }
            response = new ResponseEntity<StatusResponse>(statusResponse, HttpStatus.OK);
            return response;
    }

	@RequestMapping(value = "/subscriber/add", method = RequestMethod.POST)
    public ResponseEntity<SubscriberResponse> addSubscriber(@RequestParam String amqObjName, @RequestParam String client,  @RequestParam String subscriberName,
    		 @RequestParam String topicName, @RequestParam String selector) throws IOException{
			ResponseEntity<SubscriberResponse> response = null;
			SubscriberResponse subscriberResponse=new SubscriberResponse();
            if(LOGGER.isDebugEnabled()){
                    LOGGER.debug(this.getClass().getName()+" :: addSubscriber :::: params :: subscriberName = {0}", subscriberName);
            }
            try{
            	long start = System.currentTimeMillis();
            	amqService.createDurableSubscriber(amqObjName, client, subscriberName, topicName, selector);
            	LOGGER.info("Service - amqService: createDurableSubscriber: time to process = "+(System.currentTimeMillis() - start));
            	
        		String stringToSearch = amqObjName+",destinationName="+topicName+",destinationType=Topic,endpoint=Consumer,*"; 
            	start = System.currentTimeMillis();
            	List<Subscriber> subscriberList = amqService.getSubscribersForTopic(stringToSearch);
            	LOGGER.info("Service - amqService: getSubscribersForTopic: time to process = "+(System.currentTimeMillis() - start));
            	
            	subscriberResponse.setSubscriber(subscriberList);
            	subscriberResponse.setFlag(true);
                subscriberResponse.setMessage("Subscriber added successfully!!");
                
            }catch(Exception e){
                    LOGGER.error(this.getClass().getName()+" :: addSubscriber error", e);
                    subscriberResponse.setFlag(false);
                    subscriberResponse.setMessage("Exception while adding subscriber, Caused By: " + e);
            }
            response = new ResponseEntity<SubscriberResponse>(subscriberResponse, HttpStatus.OK);
            return response;
    }
	
	@RequestMapping(value = "/subscriber/remove", method = RequestMethod.POST)
    public ResponseEntity<SubscriberResponse> removeSubscriber(@RequestParam String subscriberName, @RequestParam String amqObjName, @RequestParam String client, @RequestParam String topicName) throws IOException{
		ResponseEntity<SubscriberResponse> response = null;
		SubscriberResponse subscriberResponse=new SubscriberResponse();
            if(LOGGER.isDebugEnabled()){
                    LOGGER.debug(this.getClass().getName()+" :: removeSubscriber :::: params :: subscriberName = {0} , topicName = {1}", subscriberName,topicName);
            }
            try{
            	long start = System.currentTimeMillis();
            	amqService.destroyDurableSubscriber(amqObjName, client, subscriberName);
            	LOGGER.info("Service - amqService: destroyDurableSubscriber: time to process = "+(System.currentTimeMillis() - start));
            	
            	String stringToSearch = amqObjName+",destinationName="+topicName+",destinationType=Topic,endpoint=Consumer,*";
            	start = System.currentTimeMillis();
            	List<Subscriber> subscriberList = amqService.getSubscribersForTopic(stringToSearch);
            	LOGGER.info("Service - amqService: getSubscribersForTopic: time to process = "+(System.currentTimeMillis() - start));
            	
            	subscriberResponse.setSubscriber(subscriberList);
            	subscriberResponse.setFlag(true);
                subscriberResponse.setMessage("Subscriber removed successfully!!");
            		
            }catch(Exception e){
                    LOGGER.error(this.getClass().getName()+" :: addSubscriber error", e);
                    subscriberResponse.setFlag(false);
                    subscriberResponse.setMessage("Exception while removing subscriber, Caused By: " + e);
            }
            response = new ResponseEntity<SubscriberResponse>(subscriberResponse, HttpStatus.OK);
            return response;
    }
	
	//getSubscriberDetails
	
	@RequestMapping(value = "/subscribers", method = RequestMethod.POST)
    public ResponseEntity<SubscriberResponse> getSubscriberDetails(@RequestParam String topicObjName) throws IOException{
            ResponseEntity<SubscriberResponse> response = null;
            SubscriberResponse subscriberResponse=new SubscriberResponse();
            if(LOGGER.isDebugEnabled()){
                    LOGGER.debug(this.getClass().getName()+" :: getSubscriberDetails");
            }
            
            String stringToSearch = topicObjName+",endpoint=Consumer,*"; 
            try{
            	long start = System.currentTimeMillis();
            	List<Subscriber> subscriberList = amqService.getSubscribersForTopic(stringToSearch);
            	LOGGER.info("Service - amqService: getSubscribersForTopic: time to process = "+(System.currentTimeMillis() - start));
            	subscriberResponse.setSubscriber(subscriberList);
            	subscriberResponse.setFlag(true);
            	
            }catch(Exception e){
                    LOGGER.error(this.getClass().getName()+" :: getSubscriberDetails error", e);
                    subscriberResponse.setFlag(false);
                    subscriberResponse.setMessage("Exception while getting subscriber details, Caused By: " + e);
            }
            response = new ResponseEntity<SubscriberResponse>(subscriberResponse, HttpStatus.OK);
            return response;
    }
	
	@RequestMapping(value = "/consumers", method = RequestMethod.POST)
    public ResponseEntity<ConsumerResponse> getConsumersList(@RequestBody List<ObjectNames> objNameList) throws IOException{
		ResponseEntity<ConsumerResponse> response = null;
		ConsumerResponse consumerResponse=new ConsumerResponse();
            if(LOGGER.isDebugEnabled()){
                    LOGGER.debug(this.getClass().getName()+" :: getConsumersList");
            }
            try{
            	long start = System.currentTimeMillis();
            	List<Consumer> consumerList = amqService.getConsumerDetails(objNameList);
            	LOGGER.info("Service - amqService: getConsumerDetails: time to process = "+(System.currentTimeMillis() - start));
            	consumerResponse.setConsumers(consumerList);
            	consumerResponse.setFlag(true);
            		
            }catch(Exception e){
                    LOGGER.error(this.getClass().getName()+" :: getConsumersList error", e);
                    consumerResponse.setFlag(false);
                    consumerResponse.setMessage("Exception while getting active consumer details, Caused By: " + e);
            }
            response = new ResponseEntity<ConsumerResponse>(consumerResponse, HttpStatus.OK);
            return response;
    }
	
}
