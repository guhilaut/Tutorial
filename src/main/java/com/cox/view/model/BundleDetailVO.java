package com.cox.view.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BundleDetailVO {
	
	private Set<String> registeredServices;
	private List<Map<String, String>> bundleHeader;
	private String symbolicName;
	private Set<String> exportedPackages;
	private String state;
	private Set<String> requiredBundles;
	private Set<String> importedPackages;
	private Set<String> servicesInUse;
	private String lastModified;
	private String version;
	private int bundleId;
	private boolean flag;
	private String message;
	
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
	public Set<String> getRegisteredServices() {
		return registeredServices;
	}
	public void setRegisteredServices(Set<String> registeredServices) {
		this.registeredServices = registeredServices;
	}
	public List<Map<String, String>> getBundleHeader() {
		return bundleHeader;
	}
	public void setBundleHeader(List<Map<String, String>> bundleHeader) {
		this.bundleHeader = bundleHeader;
	}
	public String getSymbolicName() {
		return symbolicName;
	}
	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}
	public Set<String> getExportedPackages() {
		return exportedPackages;
	}
	public void setExportedPackages(Set<String> exportedPackages) {
		this.exportedPackages = exportedPackages;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Set<String> getRequiredBundles() {
		return requiredBundles;
	}
	public void setRequiredBundles(Set<String> requiredBundles) {
		this.requiredBundles = requiredBundles;
	}
	public Set<String> getImportedPackages() {
		return importedPackages;
	}
	public void setImportedPackages(Set<String> importedPackages) {
		this.importedPackages = importedPackages;
	}
	public Set<String> getServicesInUse() {
		return servicesInUse;
	}
	public void setServicesInUse(Set<String> servicesInUse) {
		this.servicesInUse = servicesInUse;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getBundleId() {
		return bundleId;
	}
	public void setBundleId(int bundleId) {
		this.bundleId = bundleId;
	}

	
}
