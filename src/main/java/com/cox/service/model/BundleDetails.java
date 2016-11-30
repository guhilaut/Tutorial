package com.cox.service.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BundleDetails {
	
	@JsonProperty("RegisteredServices")
	private List<Integer> registeredServices;
	@JsonProperty("Headers")
	private Map<String, Map<String, String>> bundleHeader;
	@JsonProperty("SymbolicName")
	private String symbolicName;
	@JsonProperty("Location")
	private String location;
	//Need to verify the type of activationPolicyUsed
	@JsonProperty("ActivationPolicyUsed")
	private Object activationPolicyUsed;
	@JsonProperty("Hosts")
	private List<String> hosts;
	@JsonProperty("ExportedPackages")
	private List<String> exportedPackages;
	@JsonProperty("PersistentlyStarted")
	private boolean persistentlyStarted;
	@JsonProperty("RequiringBundles")
	private List<String> requiringBundles;
	@JsonProperty("Required")
	private boolean required;
	@JsonProperty("State")
	private String state;
	@JsonProperty("RemovalPending")
	private boolean removalPending;
	@JsonProperty("RequiredBundles")
	private List<Integer> requiredBundles;
	@JsonProperty("Fragments")
	private List<String> fragments;
	@JsonProperty("StartLevel")
	private int startLevel;
	@JsonProperty("ImportedPackages")
	private List<String> importedPackages;
	@JsonProperty("ServicesInUse")
	private List<Integer> servicesInUse;
	@JsonProperty("LastModified")
	private Date lastModified;
	@JsonProperty("Version")
	private String version;
	@JsonProperty("Identifier")
	private int identifier;
	@JsonProperty("Fragment")
	private boolean fragment;

	public List<Integer> getRegisteredServices() {
		return registeredServices;
	}
	public void setRegisteredServices(List<Integer> registeredServices) {
		this.registeredServices = registeredServices;
	}
	public String getSymbolicName() {
		return symbolicName;
	}
	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Object getActivationPolicyUsed() {
		return activationPolicyUsed;
	}
	public void setActivationPolicyUsed(Object activationPolicyUsed) {
		this.activationPolicyUsed = activationPolicyUsed;
	}
	public List<String> getHosts() {
		return hosts;
	}
	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}
	public boolean isPersistentlyStarted() {
		return persistentlyStarted;
	}
	public void setPersistentlyStarted(boolean persistentlyStarted) {
		this.persistentlyStarted = persistentlyStarted;
	}
	public List<String> getRequiringBundles() {
		return requiringBundles;
	}
	public void setRequiringBundles(List<String> requiringBundles) {
		this.requiringBundles = requiringBundles;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isRemovalPending() {
		return removalPending;
	}
	public void setRemovalPending(boolean removalPending) {
		this.removalPending = removalPending;
	}
	public List<Integer> getRequiredBundles() {
		return requiredBundles;
	}
	public void setRequiredBundles(List<Integer> requiredBundles) {
		this.requiredBundles = requiredBundles;
	}
	public List<String> getFragments() {
		return fragments;
	}
	public void setFragments(List<String> fragments) {
		this.fragments = fragments;
	}
	public int getStartLevel() {
		return startLevel;
	}
	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}
	public List<Integer> getServicesInUse() {
		return servicesInUse;
	}
	public void setServicesInUse(List<Integer> servicesInUse) {
		this.servicesInUse = servicesInUse;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public boolean isFragment() {
		return fragment;
	}
	public void setFragment(boolean fragment) {
		this.fragment = fragment;
	}
	public Map<String, Map<String, String>> getBundleHeader() {
		return bundleHeader;
	}
	public void setBundleHeader(Map<String, Map<String, String>> bundleHeader) {
		this.bundleHeader = bundleHeader;
	}
	public List<String> getExportedPackages() {
		return exportedPackages;
	}
	public void setExportedPackages(List<String> exportedPackages) {
		this.exportedPackages = exportedPackages;
	}
	public List<String> getImportedPackages() {
		return importedPackages;
	}
	public void setImportedPackages(List<String> importedPackages) {
		this.importedPackages = importedPackages;
	}
	
}
