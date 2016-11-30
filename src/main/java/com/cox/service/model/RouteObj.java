package com.cox.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteObj {
	
	@JsonProperty("StatisticsEnabled")
	private boolean statsEnabled;
	@JsonProperty("EndpointUri")
	private String endpointUri;
	@JsonProperty("CamelManagementName")
	private String camelManagementName;
	@JsonProperty("ExchangesCompleted")
	private int exchangeCompleted;
	@JsonProperty("LastProcessingTime")
	private long lastProcessingTime;
	@JsonProperty("ExchangesFailed")
	private int exchangeFailed;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("FirstExchangeCompletedExchangeId")
	private String firstExCompletedExId;
	@JsonProperty("FirstExchangeCompletedTimestamp")
	private Date firstExCompletedTs;
	@JsonProperty("LastExchangeFailureTimestamp")
	private Date lastExFailureTs;
	@JsonProperty("LastExchangeCompletedTimestamp")
	private Date lastExCompletedTs;
	@JsonProperty("MaxProcessingTime")
	private long maxProcessingTime;
	@JsonProperty("Load15")
	private String load15;
	@JsonProperty("DeltaProcessingTime")
	private long deltaProcessingTime;
	@JsonProperty("OldestInflightDuration")
	private String oldestInflightDuration;
	@JsonProperty("ExternalRedeliveries")
	private int externalRedeliveries;
	@JsonProperty("ExchangesTotal")
	private int exchangesTotal;
	@JsonProperty("ResetTimestamp")
	private Date resetTimestamp;
	@JsonProperty("ExchangesInflight")
	private int exchangesInflight;
	@JsonProperty("MeanProcessingTime")
	private int meanProcessingTime;
	@JsonProperty("LastExchangeFailureExchangeId")
	private String lastExFailureExId;
	@JsonProperty("FirstExchangeFailureExchangeId")
	private String firstExFailureExId;
	@JsonProperty("CamelId")
	private String camelId;
	@JsonProperty("TotalProcessingTime")
	private long totalProcessingTime;
	@JsonProperty("FirstExchangeFailureTimestamp")
	private Date firstExFailureTS;
	@JsonProperty("RouteId")
	private String routeId;
	@JsonProperty("RoutePolicyList")
	private String routePolicyList;
	@JsonProperty("FailuresHandled")
	private int failureHandled;
	@JsonProperty("Load05")
	private String load5;
	@JsonProperty("MessageHistory")
	private boolean msgHistory;
	@JsonProperty("OldestInflightExchangeId")
	private String oldestInflightExId;
	@JsonProperty("State")
	private String state;
	@JsonProperty("InflightExchanges")
	private int inflightExchanges;
	@JsonProperty("Redeliveries")
	private int redeliveries;
	@JsonProperty("MinProcessingTime")
	private long minProcessingTime;
	@JsonProperty("LastExchangeCompletedExchangeId")
	private String lastExCompletedExId;
	@JsonProperty("Tracing")
	private boolean tracing;
	@JsonProperty("Load01")
	private String load01;

	public boolean isStatsEnabled() {
		return statsEnabled;
	}
	public void setStatsEnabled(boolean statsEnabled) {
		this.statsEnabled = statsEnabled;
	}
	public String getEndpointUri() {
		return endpointUri;
	}
	public void setEndpointUri(String endpointUri) {
		this.endpointUri = endpointUri;
	}
	public String getCamelManagementName() {
		return camelManagementName;
	}
	public void setCamelManagementName(String camelManagementName) {
		this.camelManagementName = camelManagementName;
	}
	public int getExchangeCompleted() {
		return exchangeCompleted;
	}
	public void setExchangeCompleted(int exchangeCompleted) {
		this.exchangeCompleted = exchangeCompleted;
	}
	public long getLastProcessingTime() {
		return lastProcessingTime;
	}
	public void setLastProcessingTime(long lastProcessingTime) {
		this.lastProcessingTime = lastProcessingTime;
	}
	public int getExchangeFailed() {
		return exchangeFailed;
	}
	public void setExchangeFailed(int exchangeFailed) {
		this.exchangeFailed = exchangeFailed;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFirstExCompletedExId() {
		return firstExCompletedExId;
	}
	public void setFirstExCompletedExId(String firstExCompletedExId) {
		this.firstExCompletedExId = firstExCompletedExId;
	}
	public Date getFirstExCompletedTs() {
		return firstExCompletedTs;
	}
	public void setFirstExCompletedTs(Date firstExCompletedTs) {
		this.firstExCompletedTs = firstExCompletedTs;
	}
	public Date getLastExFailureTs() {
		return lastExFailureTs;
	}
	public void setLastExFailureTs(Date lastExFailureTs) {
		this.lastExFailureTs = lastExFailureTs;
	}
	public Date getLastExCompletedTs() {
		return lastExCompletedTs;
	}
	public void setLastExCompletedTs(Date lastExCompletedTs) {
		this.lastExCompletedTs = lastExCompletedTs;
	}
	public long getMaxProcessingTime() {
		return maxProcessingTime;
	}
	public void setMaxProcessingTime(long maxProcessingTime) {
		this.maxProcessingTime = maxProcessingTime;
	}
	public String getLoad15() {
		return load15;
	}
	public void setLoad15(String load15) {
		this.load15 = load15;
	}
	public long getDeltaProcessingTime() {
		return deltaProcessingTime;
	}
	public void setDeltaProcessingTime(long deltaProcessingTime) {
		this.deltaProcessingTime = deltaProcessingTime;
	}
	public String getOldestInflightDuration() {
		return oldestInflightDuration;
	}
	public void setOldestInflightDuration(String oldestInflightDuration) {
		this.oldestInflightDuration = oldestInflightDuration;
	}
	public int getExternalRedeliveries() {
		return externalRedeliveries;
	}
	public void setExternalRedeliveries(int externalRedeliveries) {
		this.externalRedeliveries = externalRedeliveries;
	}
	public int getExchangesTotal() {
		return exchangesTotal;
	}
	public void setExchangesTotal(int exchangesTotal) {
		this.exchangesTotal = exchangesTotal;
	}
	public Date getResetTimestamp() {
		return resetTimestamp;
	}
	public void setResetTimestamp(Date resetTimestamp) {
		this.resetTimestamp = resetTimestamp;
	}
	public int getExchangesInflight() {
		return exchangesInflight;
	}
	public void setExchangesInflight(int exchangesInflight) {
		this.exchangesInflight = exchangesInflight;
	}
	public int getMeanProcessingTime() {
		return meanProcessingTime;
	}
	public void setMeanProcessingTime(int meanProcessingTime) {
		this.meanProcessingTime = meanProcessingTime;
	}
	public String getLastExFailureExId() {
		return lastExFailureExId;
	}
	public void setLastExFailureExId(String lastExFailureExId) {
		this.lastExFailureExId = lastExFailureExId;
	}
	public String getFirstExFailureExId() {
		return firstExFailureExId;
	}
	public void setFirstExFailureExId(String firstExFailureExId) {
		this.firstExFailureExId = firstExFailureExId;
	}
	public String getCamelId() {
		return camelId;
	}
	public void setCamelId(String camelId) {
		this.camelId = camelId;
	}
	public long getTotalProcessingTime() {
		return totalProcessingTime;
	}
	public void setTotalProcessingTime(long totalProcessingTime) {
		this.totalProcessingTime = totalProcessingTime;
	}
	public Date getFirstExFailureTS() {
		return firstExFailureTS;
	}
	public void setFirstExFailureTS(Date firstExFailureTS) {
		this.firstExFailureTS = firstExFailureTS;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getRoutePolicyList() {
		return routePolicyList;
	}
	public void setRoutePolicyList(String routePolicyList) {
		this.routePolicyList = routePolicyList;
	}
	public int getFailureHandled() {
		return failureHandled;
	}
	public void setFailureHandled(int failureHandled) {
		this.failureHandled = failureHandled;
	}
	public String getLoad5() {
		return load5;
	}
	public void setLoad5(String load5) {
		this.load5 = load5;
	}
	public boolean isMsgHistory() {
		return msgHistory;
	}
	public void setMsgHistory(boolean msgHistory) {
		this.msgHistory = msgHistory;
	}
	public String getOldestInflightExId() {
		return oldestInflightExId;
	}
	public void setOldestInflightExId(String oldestInflightExId) {
		this.oldestInflightExId = oldestInflightExId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getInflightExchanges() {
		return inflightExchanges;
	}
	public void setInflightExchanges(int inflightExchanges) {
		this.inflightExchanges = inflightExchanges;
	}
	public int getRedeliveries() {
		return redeliveries;
	}
	public void setRedeliveries(int redeliveries) {
		this.redeliveries = redeliveries;
	}
	public long getMinProcessingTime() {
		return minProcessingTime;
	}
	public void setMinProcessingTime(long minProcessingTime) {
		this.minProcessingTime = minProcessingTime;
	}
	public String getLastExCompletedExId() {
		return lastExCompletedExId;
	}
	public void setLastExCompletedExId(String lastExCompletedExId) {
		this.lastExCompletedExId = lastExCompletedExId;
	}
	public boolean isTracing() {
		return tracing;
	}
	public void setTracing(boolean tracing) {
		this.tracing = tracing;
	}
	public String getLoad01() {
		return load01;
	}
	public void setLoad01(String load01) {
		this.load01 = load01;
	}
	
}
