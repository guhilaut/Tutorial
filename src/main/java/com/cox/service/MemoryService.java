package com.cox.service;

import com.cox.exception.ServiceException;
import com.cox.service.model.MemoryInfoBean;
import com.cox.service.model.OperatingSystemInfoBean;

public interface MemoryService {

	public MemoryInfoBean getAllMemory() throws ServiceException;
	
	public OperatingSystemInfoBean getOperatingSystemInfo() throws ServiceException;
	
}
