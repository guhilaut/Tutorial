package com.cox.exception;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ServiceException(){
		super();
	}
	
	public ServiceException(Throwable ex){
		super(ex);
	}
	
	public ServiceException(String msg){
		super(msg);
	}
	
	public ServiceException(String message, Throwable ex){
		super(message, ex);
	}

}
