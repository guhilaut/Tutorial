package com.cox.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cox.controller.helper.CommonHelper;
import com.cox.service.impl.MemoryServiceImpl;
import com.cox.service.model.MemoryInfoBean;
import com.cox.service.model.OperatingSystemInfoBean;
import com.cox.view.model.OperatingSystemResponse;
import com.cox.view.model.UsedMemoryResponse;

@Controller
@RequestMapping(value = "/memory")
public class MemoryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryController.class);
	
	@Autowired
	private CommonHelper helper;
	
	@Autowired
	private MemoryServiceImpl memoryService;
	
	@RequestMapping(value="/allMemory/{url:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<UsedMemoryResponse> getUsedMemory(@PathVariable String url){
		UsedMemoryResponse usedMemoryResponse = new UsedMemoryResponse();
		ResponseEntity<UsedMemoryResponse> response = null;
		helper.loadConfigService(url);
		try {
			long start = System.currentTimeMillis();
			MemoryInfoBean memoryInfoBean = memoryService.getAllMemory();
			LOGGER.info("Service - memoryService: getAllMemory: time to process = "+(System.currentTimeMillis() - start));
			if(memoryInfoBean != null && memoryInfoBean.getMemoryInfo() != null){
				usedMemoryResponse.setHeapMemory(memoryInfoBean.getMemoryInfo().getHeapMemory());
				usedMemoryResponse.setNonHeapMemory(memoryInfoBean.getMemoryInfo().getNonHeapMemory());
				usedMemoryResponse.setFlag(true);
				usedMemoryResponse.setMessage(null);
				response = new ResponseEntity<UsedMemoryResponse>(usedMemoryResponse, HttpStatus.OK);
			}else{
				usedMemoryResponse.setFlag(false);
				usedMemoryResponse.setMessage("MemoryInfo not found!!!");
				response = new ResponseEntity<UsedMemoryResponse>(usedMemoryResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Error in "+getClass().getName()+"::getUsedMemory", e);
			usedMemoryResponse.setFlag(false);
			usedMemoryResponse.setMessage(e.getMessage());
			response = new ResponseEntity<UsedMemoryResponse>(usedMemoryResponse, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value="/osMemory/{url:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<OperatingSystemResponse> getOperatingSystemInfo(@PathVariable String url){
		OperatingSystemResponse operatingSystemResponse = new OperatingSystemResponse();
		ResponseEntity<OperatingSystemResponse> response = null;
		helper.loadConfigService(url);
		try {
			long start = System.currentTimeMillis();
			OperatingSystemInfoBean operatingSystemInfoBean = memoryService.getOperatingSystemInfo();
			LOGGER.info("Service - memoryService: getOperatingSystemInfo: time to process = "+(System.currentTimeMillis() - start));
			if(operatingSystemInfoBean != null && operatingSystemInfoBean.getOsMemory() != null){
				operatingSystemResponse.setOsMemory(operatingSystemInfoBean.getOsMemory());
				operatingSystemResponse.setFlag(true);
				operatingSystemResponse.setMessage(null);
				response = new ResponseEntity<OperatingSystemResponse>(operatingSystemResponse, HttpStatus.OK);
			}else{
				operatingSystemResponse.setFlag(false);
				operatingSystemResponse.setMessage("getOperatingSystemInfo not found!!!");
				response = new ResponseEntity<OperatingSystemResponse>(operatingSystemResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Error in "+getClass().getName()+"::getOperatingSystemInfo", e);
			operatingSystemResponse.setFlag(false);
			operatingSystemResponse.setMessage(e.getMessage());
			response = new ResponseEntity<OperatingSystemResponse>(operatingSystemResponse, HttpStatus.OK);
		}
		return response;
	}
}
