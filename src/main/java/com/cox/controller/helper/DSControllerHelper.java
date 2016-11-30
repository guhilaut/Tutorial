package com.cox.controller.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.exception.ServiceException;
import com.cox.service.DataSourceService;
import com.cox.service.impl.Configuration;
import com.cox.service.model.ConnectionProperty;
import com.cox.service.model.DBSearchModel;
import com.cox.service.model.DbPropsInfo;
import com.cox.util.ContextListener;
import com.cox.view.model.Category;

@Service
public class DSControllerHelper {
	
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	Configuration configuration;
	
	public Map<String, Integer> getDSCount() throws ServiceException{
		
		Map<String, Integer> dsCountMap = new HashMap<String, Integer>(); 
		int sqlCount = 0;
		int oracleCount = 0;
		int unknownCount = 0;
		
		DBSearchModel dbModel = dataSourceService.searchDataSource();
		List<String> objNameList = dbModel.getObjectNames();
		for(String objName : objNameList){
			ConnectionProperty connectionProperty = dataSourceService.getConnectionProperty(objName);
			if(connectionProperty != null && connectionProperty.getDbProperty() != null 
					&& connectionProperty.getDbProperty().getUrl() != null){
				String url = connectionProperty.getDbProperty().getUrl();
				if(url.contains("oracle")){
					dsCountMap.put("ORACLE", ++oracleCount);
					//add to config list
					if(!configuration.getOracleObjNames().contains(connectionProperty.getRequest().getMbean())){
						configuration.getOracleObjNames().add(connectionProperty.getRequest().getMbean());
					}
				}else if(url.contains("sqlserver")){
					dsCountMap.put("SQL_SERVER", ++sqlCount);
					//add for sql
					if(!configuration.getSqlServerObjNames().contains(connectionProperty.getRequest().getMbean())){
						configuration.getSqlServerObjNames().add(connectionProperty.getRequest().getMbean());
					}
				}else{
					dsCountMap.put("UNKNOWN", ++unknownCount);
					//add for unknown
					if(!configuration.getUnknownDSObjNames().contains(connectionProperty.getRequest().getMbean())){
						configuration.getUnknownDSObjNames().add(connectionProperty.getRequest().getMbean());
					}
				}
			}
		}
		return dsCountMap;
	}
	
	public List<Category> getDSObjectNames() throws ServiceException{
		
		List<Category> dataSources = new ArrayList<Category>();
		
		if(configuration.getOracleObjNames() != null && !configuration.getOracleObjNames().isEmpty()){
			Category oracleCategory = new Category();
			oracleCategory.setNames(configuration.getOracleObjNames());
			oracleCategory.setType("ORACLE");
			dataSources.add(oracleCategory);
		}
		if(configuration.getSqlServerObjNames() != null && !configuration.getSqlServerObjNames().isEmpty()){
			Category sqlCategory = new Category();
			sqlCategory.setNames(configuration.getSqlServerObjNames());
			sqlCategory.setType("SQL_SERVER");
			dataSources.add(sqlCategory);
		}
		if(configuration.getUnknownDSObjNames() != null && !configuration.getUnknownDSObjNames().isEmpty()){
			Category unknownCategory = new Category();
			unknownCategory.setNames(configuration.getUnknownDSObjNames());
			unknownCategory.setType("UNKNOWN");
			dataSources.add(unknownCategory);
		}
		
		return dataSources;
	}
	
	public DbPropsInfo getDBConfiguration(String name) {
		Map<String, String> dbConfigs = ContextListener.getDbPropMap();
		DbPropsInfo dbPropsInfo = null;
		switch(name.toUpperCase()){
		case "DATA1":
			dbPropsInfo = new DbPropsInfo();
			dbPropsInfo.setConnectionUrl(dbConfigs.get("as400.data1.url"));
			dbPropsInfo.setDriverClassName(dbConfigs.get("as400.data1.driverClassName"));
			dbPropsInfo.setPassword(dbConfigs.get("as400.data1.password"));
			dbPropsInfo.setUsername(dbConfigs.get("as400.data1.username"));
			
			break;
		case "DATA2":
			dbPropsInfo = new DbPropsInfo();
			dbPropsInfo.setConnectionUrl(dbConfigs.get("as400.data2.url"));
			dbPropsInfo.setDriverClassName(dbConfigs.get("as400.data2.driverClassName"));
			dbPropsInfo.setPassword(dbConfigs.get("as400.data2.password"));
			dbPropsInfo.setUsername(dbConfigs.get("as400.data2.username"));
			
			break;
		case "CENTRAL":
			dbPropsInfo = new DbPropsInfo();
			dbPropsInfo.setConnectionUrl(dbConfigs.get("as400.central.url"));
			dbPropsInfo.setDriverClassName(dbConfigs.get("as400.central.driverClassName"));
			dbPropsInfo.setPassword(dbConfigs.get("as400.central.password"));
			dbPropsInfo.setUsername(dbConfigs.get("as400.central.username"));
			
			break;
		case "ICOMS":
			dbPropsInfo = new DbPropsInfo();
			dbPropsInfo.setConnectionUrl(dbConfigs.get("icoms.url"));
			dbPropsInfo.setPassword(dbConfigs.get("icoms.password"));
			dbPropsInfo.setUsername(dbConfigs.get("icoms.username"));
			
			break;
		
		}
		
		return dbPropsInfo;
	}

}
