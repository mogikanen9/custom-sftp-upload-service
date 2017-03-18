package com.mogikanensoftware.web.sftp.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.ProxySOCKS4;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.mogikanensoftware.web.sftp.bean.SFTPServerConnectionSettings;
import com.mogikanensoftware.web.sftp.service.SFTPService;
import com.mogikanensoftware.web.sftp.service.SFTPServiceException;

/**
 * 
 * @author vladislav_voinov
 *
 */
public class SFTPServiceImpl implements SFTPService {

	private static final Logger logger = LogManager.getLogger(SFTPServiceImpl.class);

	public static final int SFTP_PORT_UNDEFINED = -1;
	public static final int PROXY_PORT_UNDEFINED = -1;

	public static final String PROXY_TYPE_HTTP = "HTTP";
	public static final String PROXY_TYPE_SOCKS4 = "SOCKS4";
	public static final String PROXY_TYPE_SOCKS5 = "SOCKS5";

	public static final int DEFAULT_PORT = 22;

	private static final String STRICT_HOST_KEY_CHEKING = "StrictHostKeyChecking";

	private static final String NO_VALUE = "no";

	private static final String CHANNEL_TYPE_SFTP = "sftp";

	private JSch jsch = new JSch();

	private Session session;

	private ChannelSftp channel;

	public SFTPServiceImpl() {
		super();
		logger.debug("SFTPServiceImpl instance created");
	}

	public void addIdentity(String pathToKey) throws JSchException {
		logger.debug("pathToKey->" + pathToKey);
		if (pathToKey != null) {
			jsch.addIdentity(pathToKey);
		}
	}

	public Proxy getProxyForHost(String proxyHost, int proxyPort, String proxyType) {

		if (proxyType == null || proxyType.equalsIgnoreCase(PROXY_TYPE_HTTP)) {
			if (proxyPort != PROXY_PORT_UNDEFINED) {
				return new ProxyHTTP(proxyHost, proxyPort);
			} else {
				return new ProxyHTTP(proxyHost);
			}
		} else if (proxyType.equalsIgnoreCase(PROXY_TYPE_SOCKS4)) {
			if (proxyPort != PROXY_PORT_UNDEFINED) {
				return new ProxySOCKS4(proxyHost, proxyPort);
			} else {
				return new ProxySOCKS4(proxyHost);
			}
		} else if (proxyType.equalsIgnoreCase(PROXY_TYPE_SOCKS5)) {
			if (proxyPort != PROXY_PORT_UNDEFINED) {
				return new ProxySOCKS5(proxyHost, proxyPort);
			} else {
				return new ProxySOCKS5(proxyHost);
			}
		} else {
			throw new IllegalArgumentException("Unknown proxyType " + proxyType);
		}
	}

	public void disconnect() {

		if (channel != null) {
			channel.quit();
		}

		if (session != null) {
			session.disconnect();
		}
	}

	public void changeDirectory(String directory) throws SFTPServiceException {
		try {
			channel.cd(directory);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SFTPServiceException(
					"Fail to change remote directory (" + directory + ") due to " + e.getMessage(), e);
		}
	}

	public void uploadFile(File fullLocalFileName, String remoteFileName) throws SFTPServiceException {
		InputStream fis = null;
		try {
			fis = new FileInputStream(fullLocalFileName);
			channel.put(fis, remoteFileName);
		} catch (Exception e) {
			throw new SFTPServiceException(
					"Fail to upload file (" + remoteFileName + ") from string buffer due to " + e.getMessage(), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
					throw new SFTPServiceException(ex);
				}
			}
		}
	}

	@Override
	public void uploadFile(String sftpDestFolder, String sourceFileName, String nameOfFileToStore)
			throws SFTPServiceException {
		this.changeDirectory(sftpDestFolder);
		this.uploadFile(new File(sourceFileName), nameOfFileToStore);
	}

	@Override
	public void connect(SFTPServerConnectionSettings settings) throws SFTPServiceException {

		try {
			// set path to key
			if (settings.getPathToKey() != null) {
				this.addIdentity(settings.getPathToKey());
			}

			// open session
			if (settings.getPort() == SFTP_PORT_UNDEFINED) {
				settings.setPort(DEFAULT_PORT);
			}
			session = jsch.getSession(settings.getUsername(), settings.getServer(), settings.getPort());
			java.util.Properties config = new java.util.Properties();
			config.put(STRICT_HOST_KEY_CHEKING, NO_VALUE);
			session.setConfig(config);
			if (settings.getPassword() != null) {
				UserInfo ui = new SFTPUserInfo(settings.getPassword());
				session.setUserInfo(ui);
			}

			if (logger.isDebugEnabled()) {

				logger.debug("proxyHost->'" + settings.getProxyHost() + "'");
				logger.debug("proxyPort->'" + settings.getProxyPort() + "'");
				logger.debug("proxyType->'" + settings.getProxyType() + "'");
			}

			// set proxy
			if (settings.getProxyHost() != null) {
				Proxy proxy = getProxyForHost(settings.getProxyHost(), settings.getProxyPort(),
						settings.getProxyType());
				session.setProxy(proxy);
			}

			session.connect();

			channel = (ChannelSftp) session.openChannel(CHANNEL_TYPE_SFTP);
			channel.connect();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new SFTPServiceException(ex);
		}

	}

	@Override
	public void reconnect() throws SFTPServiceException {
		this.disconnect();

	}

	@Override
	public boolean isConnected() throws SFTPServiceException {
		return this.session.isConnected();
	}
}
