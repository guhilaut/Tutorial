package com.cox.view.model;

import java.util.List;

public class DataSourceResponse {
	
	private List<Category> dataSouceNames;
	private boolean flag;
	private String message;
	
	public List<Category> getDataSouceNames() {
		return dataSouceNames;
	}
	public void setDataSouceNames(List<Category> dataSouceNames) {
		this.dataSouceNames = dataSouceNames;
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
