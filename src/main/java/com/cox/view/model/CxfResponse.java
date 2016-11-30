package com.cox.view.model;

import java.util.ArrayList;
import java.util.List;

public class CxfResponse {
	
	private String message;
	private boolean flag;
	private List<CxfDetialsResponse> cxfDetialsResponse;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public List<CxfDetialsResponse> getCxfDetialsResponse() {
		if(cxfDetialsResponse == null){
			cxfDetialsResponse = new ArrayList<>();
		}
		return cxfDetialsResponse;
	}
	/*public void setCxfDetialsResponse(List<CxfDetialsResponse> cxfDetialsResponse) {
		this.cxfDetialsResponse = cxfDetialsResponse;
	}*/
	
}
