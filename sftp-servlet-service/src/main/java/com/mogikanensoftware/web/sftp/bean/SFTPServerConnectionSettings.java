package com.mogikanensoftware.web.sftp.bean;

import java.io.Serializable;
/**
 * 
 * @author vladislav_voinov
 *
 */
public class SFTPServerConnectionSettings implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String server;
	private String username;
	private String password;
	private String pathToKey;
	private int port;
	private String proxyHost;
	private int proxyPort;
	private String proxyType;
	

	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPathToKey() {
		return pathToKey;
	}
	public void setPathToKey(String pathToKey) {
		this.pathToKey = pathToKey;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getProxyType() {
		return proxyType;
	}
	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((pathToKey == null) ? 0 : pathToKey.hashCode());
		result = prime * result + port;
		result = prime * result + ((proxyHost == null) ? 0 : proxyHost.hashCode());
		result = prime * result + proxyPort;
		result = prime * result + ((proxyType == null) ? 0 : proxyType.hashCode());
		result = prime * result + ((server == null) ? 0 : server.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SFTPServerConnectionSettings other = (SFTPServerConnectionSettings) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (pathToKey == null) {
			if (other.pathToKey != null)
				return false;
		} else if (!pathToKey.equals(other.pathToKey))
			return false;
		if (port != other.port)
			return false;
		if (proxyHost == null) {
			if (other.proxyHost != null)
				return false;
		} else if (!proxyHost.equals(other.proxyHost))
			return false;
		if (proxyPort != other.proxyPort)
			return false;
		if (proxyType == null) {
			if (other.proxyType != null)
				return false;
		} else if (!proxyType.equals(other.proxyType))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SFTPServerConnectionSettings [server=" + server + ", username=" + username + ", password=" + password
				+ ", pathToKey=" + pathToKey + ", port=" + port + ", proxyHost=" + proxyHost + ", proxyPort="
				+ proxyPort + ", proxyType=" + proxyType + "]";
	}
}
