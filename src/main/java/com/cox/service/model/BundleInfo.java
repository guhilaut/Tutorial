package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BundleInfo {
	
	@JsonProperty("Version")
	private String version;
	@JsonProperty("Blueprint")
	private String blueprint;
	@JsonProperty("State")
	private String state;
	@JsonProperty("ID")
	private String id;
	@JsonProperty("Start Level")
	private String startLevel;
	@JsonProperty("Name")
	private String name;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBlueprint() {
		return blueprint;
	}
	public void setBlueprint(String blueprint) {
		this.blueprint = blueprint;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartLevel() {
		return startLevel;
	}
	public void setStartLevel(String startLevel) {
		this.startLevel = startLevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
