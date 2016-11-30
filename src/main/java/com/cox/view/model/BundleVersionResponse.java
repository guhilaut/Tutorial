package com.cox.view.model;

import java.util.List;

public class BundleVersionResponse {
	
	private List<BundleVersion> bundleVersionList;
	private boolean flag;
	private String message;
	
	public List<BundleVersion> getBundleVersionList() {
		return bundleVersionList;
	}
	public void setBundleVersionList(List<BundleVersion> bundleVersionList) {
		this.bundleVersionList = bundleVersionList;
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
