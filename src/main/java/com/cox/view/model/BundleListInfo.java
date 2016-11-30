package com.cox.view.model;

import java.util.List;

import com.cox.service.model.BundleInfo;

public class BundleListInfo {
	
	private List<BundleInfo> bundleList;
	private boolean flag;
	private String message;
	
	public List<BundleInfo> getBundleList() {
		return bundleList;
	}
	public void setBundleList(List<BundleInfo> bundleList) {
		this.bundleList = bundleList;
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
