package com.cox.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cox.controller.helper.CommonHelper;
import com.cox.exception.ServiceException;
import com.cox.service.LogService;
import com.cox.service.model.LogFilter;
import com.cox.service.model.LogValue;
import com.cox.view.mapper.ViewVOMapper;
import com.cox.view.model.LogDetailResponse;

@Controller
@RequestMapping(value = "/log")
public class LogController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);
	
	@Autowired
	private LogService logService;
	@Autowired
	private ViewVOMapper viewMapper;
	@Autowired
	private CommonHelper helper;
	
	@RequestMapping(path="/getLogTail", produces="application/json")
	public ResponseEntity<LogDetailResponse> getLogTail(@RequestParam String url, @RequestParam int lineNo, HttpServletResponse resp) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+"::getLogTail is called with param {0}, {1}", url, lineNo);
		}
		LogDetailResponse logDetailResponse = null;
		try{
			helper.loadConfigService(url);
			/*LogDetail logDetail = logService.getLogTail(lineNo);
			logDetailResponse = viewMapper.mapToLogDetailResponse(logDetail);*/
			long start = System.currentTimeMillis();
			LogValue logValue = logService.getLogTail(lineNo);
			LOGGER.info("Service - logService: getLogTail: time to process = "+(System.currentTimeMillis() - start));
			logDetailResponse = viewMapper.mapToLogDetailResponse(logValue);
		}catch(ServiceException ex){
			LOGGER.error(getClass().getName()+" Error in getLogTail() called with params {0}, {1}", url, lineNo);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<LogDetailResponse>(logDetailResponse, HttpStatus.OK);
	}
	
	@RequestMapping(path="/filterLog", produces="application/json")
	public ResponseEntity<LogDetailResponse> filterLogEvents(@RequestParam int lineNo, @RequestParam String logLevel, @RequestParam(required=false) Long afterTime, 
			@RequestParam(required=false) Long beforeTime, @RequestParam(required=false) String matchesText, HttpServletResponse resp) throws IOException{
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+"::filterLogEvents() is called with param {0}, {1}, {2}, {3}, {4}", lineNo, logLevel, afterTime, beforeTime, matchesText);
		}
		LogDetailResponse logDetailResponse = null;
		try {
			LogFilter logFilter = new LogFilter();
			logFilter.setCount(lineNo);
			logFilter.setLevels(logLevel);
			if(afterTime != null)
				logFilter.setAfterTimestamp(afterTime);
			if(beforeTime != null)
				logFilter.setBeforeTimestamp(beforeTime);
			if(matchesText != null)
				logFilter.setMatchesText(matchesText);
			long start = System.currentTimeMillis();
			LogValue logValue = logService.filterLogEvents(logFilter);
			LOGGER.info("Service - logService: filterLogEvents: time to process = "+(System.currentTimeMillis() - start));
			logDetailResponse = viewMapper.mapToLogDetailResponse(logValue);
		} catch (ServiceException e) {
			LOGGER.error(getClass().getName()+"::filterLogEvents() is called with param {0}, {1}, {2}, {3}, {4}", lineNo, logLevel, afterTime, beforeTime, matchesText);
			resp.reset();
	        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<LogDetailResponse>(logDetailResponse, HttpStatus.OK);
	}
}
