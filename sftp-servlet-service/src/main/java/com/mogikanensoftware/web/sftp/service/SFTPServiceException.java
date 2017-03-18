package com.mogikanensoftware.web.sftp.service;

/**
 * 
 * @author vladislav_voinov
 *
 */
public class SFTPServiceException extends Exception {

	public static final String module = SFTPServiceException.class.getName();
	public static final long serialVersionUID = module.hashCode();

	public SFTPServiceException() {
		super();
	}

	public SFTPServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public SFTPServiceException(String message) {
		super(message);
	}

	public SFTPServiceException(Throwable cause) {
		super(cause);
	}

}
