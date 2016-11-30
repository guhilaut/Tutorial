package com.cox.service;

import java.util.List;
import java.util.Set;

import com.cox.exception.ServiceException;
import com.cox.service.model.BundleDetailResult;
import com.cox.service.model.CXFMBeanInfo;
import com.cox.service.model.CXFMBeanResult;
import com.cox.service.model.ConfigListBean;
import com.cox.service.model.MBeanListResult;
import com.cox.service.model.MBeanSearchResult;
import com.cox.service.model.MConfigDetail;
import com.cox.service.model.RouteDTO;
import com.cox.service.model.User;
import com.cox.view.model.BundleVersion;
import com.cox.view.model.ConfigProp;
import com.cox.view.model.DependentBundle;
import com.cox.view.model.RouteDefinition;
/**
 * All the bundle related methods.
 * @author SSharma17
 *
 */
public interface BundleService {

	/**
	 * List all the bundles deployed on the jboss fuse server.
	 * @return MBeanListResult - An object representation of json string returned from fuse server.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public MBeanListResult listBundles() throws ServiceException;
	/**
	 * Get all the MBeans name for further invocation.
	 * @return MBeanSearchResult - An object representation of json string returned from fuse server.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	//public MBeanSearchResult getAllMBeans() throws ServiceException;
	/**
	 * Get the details of a particular bundle.
	 * @param bundleId - Id of the bundle.
	 * @return BundleDetailResult - An object representation of json string returned from fuse server.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public BundleDetailResult getMBeanInfo(String bundleId) throws ServiceException;
	/**
	 * Get the details of the bundles in batch.
	 * @param bundleIdList - list of bundle Id.
	 * @return List<BundleDetailResult> - A list of objects representation of json string returned from fuse server.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public List<BundleDetailResult> getMBeanInfoList(List<String> bundleIdList) throws ServiceException;
	/**
	 * Get the dependent bundles on this bundle
	 * @param bundleId - Id of the bundle.
	 * @return List<BundleDetailResult> - A list of objects representation of json string returned from fuse server.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public Set<DependentBundle> getDependentBundles(String bundleId) throws ServiceException;
	/**
	 * List all the configurations available related to this bundle
	 * @param bundleName - name of the bundle
	 * @return ConfigListBean - list of properties file names available
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public ConfigListBean listAllConfigFiles() throws ServiceException;
	/**
	 * Get the properties of the bundle through the config file.
	 * @param bundleName - the name of the bundle
	 * @return MConfigDetail - An object representation of json string returned from fuse server.
	 * @throws ServiceException -  throws when a call to fuse server have problems.
	 */
	public MConfigDetail loadConfiguration(String bundleName) throws ServiceException;
	/**
	 * Get the route definition of a bundle.
	 * @param objName - name of the bundle.
	 * @return List<RouteDefinition> - list of {@link RouteDefinition}
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public List<RouteDefinition> getRouteDefinitions(String objName) throws ServiceException;
	/**
	 * Get the route information.
	 * @param objName - the bundle name
	 * @param routeId - route Id
	 * @param routeDefinitions - the route definition
	 * @param endpoint - the endpoint
	 * @return RouteDTO - A route value object.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public RouteDTO getRouteInfo(String objName, String routeId, List<RouteDefinition> routeDefinitions, String endpoint) throws ServiceException;
	/**
	 * Checks the version of a bundle on all the server in a particular environment.
	 * @param bundleName - name of the bundle.
	 * @return List<BundleVersion> - list of version and server.
	 */
	public List<BundleVersion> checkVersionMismatchOnAllServers(String bundleName);
	/**
	 * The count of the bundle deployed on the server
	 * @param user - authentic user for this server
	 * @param ip - server ip
	 * @return int - count of the bundle
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public int getBundleCount(User user, String ip) throws ServiceException;
	/**
	 * Switch the bundle state start to stop and vice-versa
	 * @param bundleId - the bundle id
	 * @param state - The state of the bundle that  
	 * @return true if state changed successfully else false.
	 * @throws ServiceException - throws when a call to fuse server have problems. 
	 */
	public boolean switchBundleState(String bundleId, String state) throws ServiceException;
	/**
	 * update the configurations.
	 * @param bundleName - bundle name.
	 * @param configPropList - list of {@link ConfigProp}
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public void updateConfiguration(String bundleName, List<ConfigProp> configPropList) throws ServiceException;
	/**
	 * get details of cxf Mbean.
	 * @param bundleName - bundle name.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public List<CXFMBeanResult> getCxfMBean(String bundleName) throws ServiceException;
	
	/**
	 * get wsdl or wadl url.
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public String getCxfEndpointURL(CXFMBeanInfo cxfMBeanInfo) throws ServiceException;
	
	/**
	 * get list of operations from wsdl.
	 * @param serviceUrl - wsdl url.
	 * @param isWSDL - true if url is of wsdl else false for wadl
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public List<String> getOperationList(String serviceUrl, boolean isWSDL) throws ServiceException;
	
	/**
	 * to test soap request.
	 * @param serviceUrl - wsdl url.
	 * @param operationName - name of the operation to be tested
	 * @param requestString - soap request string
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public String testServiceOperationForWSDL(String serviceUrl, String operationName, String requestString) throws ServiceException;
	
	/**
	 * get soap request XML.
	 * @param serviceUrl - wsdl url.
	 * @param operationName - name of the operation to be tested
	 * @throws ServiceException - throws when a call to fuse server have problems.
	 */
	public String getSOAPRequest(String serviceUrl, String operationName) throws ServiceException;
	//public String testServiceOperation(String serviceUrl, String operationName, String requestString, boolean isWSDL) throws ServiceException;

	
}
