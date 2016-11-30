package com.cox.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
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

import com.cox.controller.helper.HomeControllerHelper;
import com.cox.service.model.User;
import com.cox.view.model.ServerStatusResponse;

@Controller
@RequestMapping(value="/home")
public class HomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private HomeControllerHelper helper;
	
	@RequestMapping(value="/loadServers/{environment}", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<ServerStatusResponse> loadServers(@PathVariable String environment, @RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(getClass().getName()+"::loadServers is called with environment={0}", environment);
		}
		request.getSession().setAttribute("user", user);
		ServerStatusResponse serverStatus = null;
		if(user != null){
			long start = System.currentTimeMillis();
			serverStatus = helper.getServerStatus(environment, user);
			LOGGER.info("Helper - HomeControllerHelper: getServerStatus: time to process = "+(System.currentTimeMillis() - start));
		}else{
			response.reset();
	        response.setHeader("Content-Type", "application/json;charset=UTF-8");
	        response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
		return new ResponseEntity<ServerStatusResponse>(serverStatus, HttpStatus.OK);
	}
	
}
