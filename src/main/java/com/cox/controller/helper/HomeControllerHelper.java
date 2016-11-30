package com.cox.controller.helper;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.MalformedObjectNameException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cox.exception.ServiceException;
import com.cox.service.impl.BundleServiceImpl;
import com.cox.service.impl.Configuration;
import com.cox.service.impl.DataSourceServiceImpl;
import com.cox.service.model.User;
import com.cox.view.model.ServerConfigBean;
import com.cox.view.model.ServerInfo;
import com.cox.view.model.ServerStatusResponse;

@Service
public class HomeControllerHelper {

	@Autowired
	private Configuration configService;
	@Autowired
	private BundleServiceImpl bundleService;
	@Autowired
	private DataSourceServiceImpl dbService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HomeControllerHelper.class);

	public ServerStatusResponse getServerStatus(String env, User user) {

		ServerStatusResponse serverStatus = new ServerStatusResponse();

		List<ServerInfo> serverInfoList = new CopyOnWriteArrayList<ServerInfo>();
		// List<ServerConfigBean> serverConfigList =
		// ContextListener.getServerIpConfigMap().get(env);
		List<ServerConfigBean> serverConfigList = configService
				.getServerIpConfigMap().get(env);
		List<ServerConfigBean> activeServerConfigList = new CopyOnWriteArrayList<ServerConfigBean>();
		ExecutorService executorService = Executors
				.newFixedThreadPool(serverConfigList.size());
		CountDownLatch latch = new CountDownLatch(serverConfigList.size());

		for (ServerConfigBean serverConfigBean : serverConfigList) {
			// to store ip and jolokiaPath in the configuration.
			String jolokiaPath = serverConfigBean.getScheme() + "://"
					+ serverConfigBean.getIp() + ":"
					+ serverConfigBean.getPort() + "/"
					+ serverConfigBean.getJolokiaUrl();
			configService.getIpJolokiaPathMap().put(serverConfigBean.getIp(),
					jolokiaPath);
			// submit task to executor service to get the server status.
			executorService.submit(new ServerTracker(serverConfigBean, user,
					latch, serverInfoList, activeServerConfigList));
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			LOGGER.error(
					getClass().getName()
							+ "::getServerStatus() thread interrupted while waiting on latch",
					e);
		} finally {
			executorService.shutdownNow();
		}

		List<String> activeServerList = new ArrayList<String>();
		for (ServerInfo serverInfo : serverInfoList) {
			if (serverInfo.isPingStatus()) {
				activeServerList.add(serverInfo.getName());
			}
		}
		// set user in the configuration.
		configService.setUser(user);
		// set only active servers configurations
		configService.setActiveServersConfig(activeServerConfigList);
		serverStatus.setServerInfoList(serverInfoList);
		serverStatus.setFlag(true);
		serverStatus.setMessage(null);
		return serverStatus;
	}

	private boolean getPingStatus(ServerConfigBean serverConfigBean) {

		boolean reachable = false;
		Socket socket = null;
		try {
			int p = Integer.parseInt(serverConfigBean.getPort().trim());
			socket = new Socket(serverConfigBean.getIp(), p);
			reachable = true;
		} catch (UnknownHostException e) {
			// ignore
		} catch (IOException e) {
			// ignore
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
				}
		}
		return reachable;
	}

	class ServerTracker implements Runnable {

		String ip;
		User user;
		CountDownLatch latch;
		List<ServerInfo> serverInfoList;
		ServerConfigBean serverConfigBean;
		List<ServerConfigBean> activeServerConfigList;

		ServerTracker(ServerConfigBean serverConfigBean, User user,
				CountDownLatch latch, List<ServerInfo> serverInfoList,
				List<ServerConfigBean> activeServerConfigList) {
			this.user = user;
			this.latch = latch;
			this.serverInfoList = serverInfoList;
			this.serverConfigBean = serverConfigBean;
			this.activeServerConfigList = activeServerConfigList;
		}

		@Override
		public void run() {
			ServerInfo serverInfo = new ServerInfo();
			try {
				boolean pingStatus = getPingStatus(serverConfigBean);
				serverInfo.setName(serverConfigBean.getIp());
				serverInfo.setPingStatus(pingStatus);
				String jolokiaPath = serverConfigBean.getScheme() + "://"
						+ serverConfigBean.getIp() + ":"
						+ serverConfigBean.getPort() + "/"
						+ serverConfigBean.getJolokiaUrl();
				serverInfo.setJolokiaPath(jolokiaPath);
				if (pingStatus) {
					// set the active servers.
					activeServerConfigList.add(serverConfigBean);
					if ("FUSE".equals(serverConfigBean.getType())) {
						serverInfo.setBundleCount(bundleService.getBundleCount(
								user, jolokiaPath));
						serverInfo.setDbCount(dbService.getDBCount(user,
								jolokiaPath));
					} else if ("ACTIVEMQ".equals(serverConfigBean.getType())) {
						checkAMQServer(user, jolokiaPath);
					}
				} else {
					serverInfo.setPingStatus(false);
					serverInfo.setMessage("Server is down");
				}
			} catch (Exception e) {
				// Either server got down at this point or invalid credentials
				// applied, so set server status false here.
				serverInfo.setPingStatus(false);
				serverInfo.setMessage("Invalid Credentials");
				LOGGER.error(getClass().getName()
						+ "::run() Exception while getting servers status", e);
			} finally {
				serverInfoList.add(serverInfo);
				latch.countDown();
			}
		}
	}

	private void checkAMQServer(User user, String jolokiaPath)
			throws ServiceException {
		try {
			J4pClient client = J4pClient.url(jolokiaPath).user(user.getName())
					.password(user.getPassword())
					.authenticator(new BasicAuthenticator().preemptive())
					.build();
			J4pSearchRequest request = new J4pSearchRequest(
					"org.apache.activemq:type=Broker,brokerName=*");
			client.execute(request);
			((CloseableHttpClient) client.getHttpClient()).close();

		} catch (J4pException e) {
			LOGGER.error(
					getClass().getName() + " :: checkAMQServer :::: error", e);
			throw new ServiceException("Server Connection Error", e);
		} catch (MalformedObjectNameException e) {
			LOGGER.error(
					getClass().getName() + " :: checkAMQServer :::: error", e);
			throw new ServiceException("Malformed Url Error", e);
		} catch (IOException e) {
			LOGGER.error(
					getClass().getName() + " :: checkAMQServer :::: error", e);
			throw new ServiceException("IO Connection Error", e);
		}
	}

}
