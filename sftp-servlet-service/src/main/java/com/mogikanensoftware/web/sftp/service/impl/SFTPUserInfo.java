package com.mogikanensoftware.web.sftp.service.impl;

import java.util.logging.Logger;

import com.jcraft.jsch.UserInfo;

/**
 * 
 * @author vladislav_voinov
 *
 */
public class SFTPUserInfo implements UserInfo {

	private static final Logger log = Logger.getLogger(SFTPUserInfo.class.getName());

	private String passwd;

	public SFTPUserInfo(String pass) {
		this.passwd = pass;
	}

	public String getPassword() {
		return passwd;
	}

	public boolean promptYesNo(String str) {
		return true;
	}

	public String getPassphrase() {
		return null;
	}

	public boolean promptPassphrase(String message) {
		return true;
	}

	public boolean promptPassword(String message) {
		return true;
	}

	public void showMessage(String message) {
		log.info("message->" + message);
	}
}
