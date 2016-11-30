package com.cox.controller.helper;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cox.service.BundleService;
import com.cox.service.impl.Configuration;
import com.cox.service.model.BundleInfo;
import com.cox.service.model.MBeanListResult;
import com.cox.service.model.MBeanSearchResult;
import com.cox.view.model.ServerConfigBean;

@Component
public class CommonHelper {
	
	@Autowired
	private Configuration configService;
	@Autowired
	private BundleService bundleService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonHelper.class);

	public void loadConfigService(String url) {
		try{
			String baseUrl = configService.getIpJolokiaPathMap().get(url);
			configService.setBaseUrl(baseUrl);
			//get the current server configuration.
			ServerConfigBean serverConfigBean = null;
			for(ServerConfigBean serverConfig : configService.getActiveServersConfig()){
				if(url.equals(serverConfig.getIp())){
					serverConfigBean = serverConfig;
					break;
				}
			}
			// load all MBeans and bundleMap when active tab is FUSE.
			if("FUSE".equals(serverConfigBean.getType())){
				/*MBeanSearchResult result = bundleService.getAllMBeans();
				if(result.getValue() != null){
					configService.setObjectNamesList(result.getValue());
				}*/
				long start = System.currentTimeMillis();
				MBeanListResult listResult = bundleService.listBundles();
				LOGGER.info("Service - bundleService: listBundles: time to process = "+(System.currentTimeMillis() - start));
				if(listResult.getValue() != null){
					Map<Integer, BundleInfo> map = listResult.getValue();
					for(Entry<Integer, BundleInfo> entry : map.entrySet()){
						configService.getBundleMap().put(entry.getKey(), entry.getValue().getName());
					}
				}
			}
		}catch(Exception e){
			LOGGER.error(getClass().getName()+"::loadConfigService() Exception while loading configuration", e);
		}
	}
}
