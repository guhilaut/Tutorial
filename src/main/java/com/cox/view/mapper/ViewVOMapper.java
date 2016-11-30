package com.cox.view.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cox.service.impl.Configuration;
import com.cox.service.model.BundleDetails;
import com.cox.service.model.ConfigListBean;
import com.cox.service.model.LogDetail;
import com.cox.service.model.LogValue;
import com.cox.view.model.BundleDetailVO;
import com.cox.view.model.ConfigListResponse;
import com.cox.view.model.ConfigProp;
import com.cox.view.model.LogDetailResponse;

@Component
public class ViewVOMapper {
	
	@Autowired
	private Configuration configuration;
	
	public BundleDetailVO convertBundleDetails(BundleDetails bundleDetails){
		BundleDetailVO bundleDetailVO = new BundleDetailVO();
		Map<String, Map<String, String>> headers = bundleDetails.getBundleHeader();
		List<Map<String, String>> newHeaders = new ArrayList<Map<String, String>>();
		for(String key : headers.keySet()){
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.putAll(headers.get(key));
			newHeaders.add(headerMap);
		}
		bundleDetailVO.setBundleHeader(newHeaders);
		bundleDetailVO.setExportedPackages(new HashSet<String>(bundleDetails.getExportedPackages()));
		bundleDetailVO.setBundleId(bundleDetails.getIdentifier());
		bundleDetailVO.setImportedPackages(new HashSet<String>(bundleDetails.getImportedPackages()));
		Date modifiedDate = bundleDetails.getLastModified();
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd YYYY hh:mm");
		String strDate = formatter.format(modifiedDate);
		bundleDetailVO.setLastModified(strDate);
		Set<String> registeredServices = new HashSet<String>();
		for(Integer bundleId : bundleDetails.getRegisteredServices()){
			String bundleName = configuration.getBundleMap().get(bundleId);
			if(bundleName != null){
				registeredServices.add(bundleName);
			}
		}
		bundleDetailVO.setRegisteredServices(registeredServices);
		Set<String> requiredBundles = new HashSet<String>();
		for(Integer bundleId : bundleDetails.getRequiredBundles()){
			String bundleName = configuration.getBundleMap().get(bundleId);
			if(bundleName != null){
				requiredBundles.add(bundleName);
			}
		}
		bundleDetailVO.setRequiredBundles(requiredBundles);
		Set<String> usedServices = new HashSet<String>();
		for(Integer bundleId : bundleDetails.getServicesInUse()){
			String bundleName = configuration.getBundleMap().get(bundleId);
			if(bundleName != null){
				usedServices.add(bundleName);
			}
		}
		bundleDetailVO.setServicesInUse(usedServices);
		bundleDetailVO.setState(bundleDetails.getState());
		bundleDetailVO.setSymbolicName(bundleDetails.getSymbolicName());
		bundleDetailVO.setVersion(bundleDetails.getVersion());
		
		return bundleDetailVO;
	}

	public List<ConfigProp> createConfigPropList(Map<String, String> propList) {
		List<ConfigProp> configPropList = new ArrayList<ConfigProp>();
		for(Entry<String, String> entry : propList.entrySet()){
			ConfigProp config = new ConfigProp();
			config.setName(entry.getKey());
			config.setValue(entry.getValue());
			configPropList.add(config);
		}
		
		return configPropList;
	}

	public ConfigListResponse createConfigList(ConfigListBean configList, String bundleName) {
		ConfigListResponse configListResponse = new ConfigListResponse();
		
		if(configList.getConfigList() != null){
			List<String> configNameList = new ArrayList<String>();
			for(String configName : configList.getConfigList()){
				if(configName.contains(bundleName)){
					configNameList.add(configName);
				}
			}
			configListResponse.setConfigList(configNameList);
			configListResponse.setFlag(true);
			configListResponse.setMessage(null);
		}else{
			configListResponse.setConfigList(null);
			configListResponse.setFlag(false);
			configListResponse.setMessage("Configuration Not Found!!!");
		}
		
		return configListResponse;
	}

	public LogDetailResponse mapToLogDetailResponse(LogDetail logDetail) {
		LogDetailResponse logDetailResponse = new LogDetailResponse();
		if(logDetail != null && logDetail.getLogValue() != null 
				&& logDetail.getLogValue().getLogEventsList() != null && !logDetail.getLogValue().getLogEventsList().isEmpty()){
			logDetailResponse.setLogEvents(logDetail.getLogValue().getLogEventsList());
			logDetailResponse.setFlag(true);
			logDetailResponse.setMessage(null);
		}else{
			logDetailResponse.setLogEvents(null);
			logDetailResponse.setFlag(false);
			logDetailResponse.setMessage("Log not found!!!");
		}
		return logDetailResponse;
	}
	
	public LogDetailResponse mapToLogDetailResponse(LogValue logValue) {
		LogDetailResponse logDetailResponse = new LogDetailResponse();
		if(logValue != null && logValue.getLogEventsList()!= null && !logValue.getLogEventsList().isEmpty()){
			logDetailResponse.setLogEvents(logValue.getLogEventsList());
			logDetailResponse.setFlag(true);
			logDetailResponse.setMessage(null);
		}else{
			logDetailResponse.setLogEvents(null);
			logDetailResponse.setFlag(false);
			logDetailResponse.setMessage("Log not found!!!");
		}
		return logDetailResponse;
	}
}
