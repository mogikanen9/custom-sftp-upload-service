package com.mogikanensoftware.web.sftp.service;

import com.mogikanensoftware.web.sftp.bean.SFTPServerConnectionSettings;

/**
 * 
 * @author vladislav_voinov
 *
 */
public interface SFTPService {

	int FTP_PORT_UNDEFINED = -1;
	int PROXY_PORT_UNDEFINED = -1;

	String PROXY_TYPE_HTTP = "HTTP";
	String PROXY_TYPE_SOCKS4 = "SOCKS4";
	String PROXY_TYPE_SOCKS5 = "SOCKS5";

	void connect(SFTPServerConnectionSettings settings) throws SFTPServiceException;
	
	void reconnect() throws SFTPServiceException;
	
	boolean isConnected() throws SFTPServiceException;

	void uploadFile(String sftpDestFolder,String sourceFileName, String nameOfFileToStore) throws SFTPServiceException;
	
	void disconnect() throws SFTPServiceException;
	
	
}
