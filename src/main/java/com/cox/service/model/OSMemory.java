package com.cox.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OSMemory {
	@JsonProperty("SystemCpuLoad")
	private Double systemCPULoad;
	@JsonProperty("TotalPhysicalMemorySize")
	private Long totalPhysicalMemorySize;
	@JsonProperty("FreePhysicalMemorySize")
	private Long freePhysicalMemorySize;

	public Double getSystemCPULoad() {
		return systemCPULoad;
	}
	public void setSystemCPULoad(Double systemCPULoad) {
		this.systemCPULoad = systemCPULoad;
	}
	public Long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}
	public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize) {
		this.totalPhysicalMemorySize = totalPhysicalMemorySize;
	}
	public Long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}
	public void setFreePhysicalMemorySize(Long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}
}
