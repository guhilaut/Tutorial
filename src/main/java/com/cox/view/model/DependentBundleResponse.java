package com.cox.view.model;

import java.util.Set;

public class DependentBundleResponse {
	
	private Set<DependentBundle> dependentBundleList;
	private boolean flag;
	private String message;
	
	public Set<DependentBundle> getDependentBundleList() {
		return dependentBundleList;
	}
	public void setDependentBundleList(Set<DependentBundle> dependentBundleList) {
		this.dependentBundleList = dependentBundleList;
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
