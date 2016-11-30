package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemoryInfo {
	
	@JsonProperty("HeapMemoryUsage")
	private Memory heapMemory;
	@JsonProperty("NonHeapMemoryUsage")
	private Memory nonHeapMemory;
	
	public Memory getHeapMemory() {
		return heapMemory;
	}
	public void setHeapMemory(Memory heapMemory) {
		this.heapMemory = heapMemory;
	}
	public Memory getNonHeapMemory() {
		return nonHeapMemory;
	}
	public void setNonHeapMemory(Memory nonHeapMemory) {
		this.nonHeapMemory = nonHeapMemory;
	}

}
