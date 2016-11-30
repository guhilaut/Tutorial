package com.cox.service;

import com.cox.exception.ServiceException;
import com.cox.service.model.LogFilter;
import com.cox.service.model.LogValue;

/**
 * This interface is responsible for track the server logs.
 * @author SSharma17
 * @since 17 Apr 2016
 */
public interface LogService {

	/**
	 * get the log tail for the specified line no.
	 * @param line - line no of the log
	 * @return logDetail - details of log
	 * @throws ServiceException - in case of service failure.
	 */
	public LogValue getLogTail(int line) throws ServiceException;
	/**
	 * get the log based on log-filter parameters
	 * @param logFiler - to filter the logs
	 * @return LogDetail - details of log
	 * @throws ServiceException - in case of service failure.
	 */
	public LogValue filterLogEvents(LogFilter logFiler) throws ServiceException;
	
}
