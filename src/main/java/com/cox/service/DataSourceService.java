package com.cox.service;

import java.util.List;

import com.cox.exception.ServiceException;
import com.cox.service.model.ConnectionModel;
import com.cox.service.model.ConnectionProperty;
import com.cox.service.model.DBSearchModel;
import com.cox.service.model.DbPropsInfo;
import com.cox.service.model.MConfigDetail;
import com.cox.service.model.User;
import com.cox.view.model.CheckConnectionResponse;

public interface DataSourceService {

	public ConnectionModel checkDBConnection(String objName) throws ServiceException;
	
	public DBSearchModel searchDataSource() throws ServiceException;
	
	public CheckConnectionResponse testConnection(String objName, String type) throws ServiceException;
	
	public List<String> getDSDependent(String objName) throws ServiceException;
	
	public List<MConfigDetail> getPropertiesList(List<String> bundleNameList) throws ServiceException;
	
	public CheckConnectionResponse testAs400Connection(DbPropsInfo dbPropsInfo);
	
	public CheckConnectionResponse testICOMConnection(DbPropsInfo dbPropsInfo);
	
	public int getDBCount(User user, String ip) throws ServiceException;
	
	public ConnectionProperty getConnectionProperty(String objName) throws ServiceException;
	
}
