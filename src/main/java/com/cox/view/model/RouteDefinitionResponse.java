package com.cox.view.model;

import java.util.List;


public class RouteDefinitionResponse {
	
	private List<RouteDefinition> routeDefinitionResponse;
	private boolean flag;
	private String message;
	
	public List<RouteDefinition> getRouteDefinitionResponse() {
		return routeDefinitionResponse;
	}
	public void setRouteDefinitionResponse(
			List<RouteDefinition> routeDefinitionResponse) {
		this.routeDefinitionResponse = routeDefinitionResponse;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
