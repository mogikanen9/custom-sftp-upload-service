package com.mogikanensoftware.web.sftp.bean;

import java.io.Serializable;

/**
 * 
 * @author vladislav_voinov
 *
 */
public class SftpUploadInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String sftpDestFolder;
	private String sourceFileName;
	private String nameOfFileToStore;	

	public SftpUploadInfoBean() {
		super();
	}

	public SftpUploadInfoBean(String sftpDestFolder,
			String sourceFileName, String nameOfFileToStore) {
		super();	
		this.sftpDestFolder = sftpDestFolder;
		this.sourceFileName = sourceFileName;
		this.nameOfFileToStore = nameOfFileToStore;
	}

	public String getSftpDestFolder() {
		return sftpDestFolder;
	}

	public void setSftpDestFolder(String sftpDestFolder) {
		this.sftpDestFolder = sftpDestFolder;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public String getNameOfFileToStore() {
		return nameOfFileToStore;
	}

	public void setNameOfFileToStore(String nameOfFileToStore) {
		this.nameOfFileToStore = nameOfFileToStore;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nameOfFileToStore == null) ? 0 : nameOfFileToStore.hashCode());
		result = prime * result + ((sftpDestFolder == null) ? 0 : sftpDestFolder.hashCode());
		result = prime * result + ((sourceFileName == null) ? 0 : sourceFileName.hashCode());
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
		SftpUploadInfoBean other = (SftpUploadInfoBean) obj;
		if (nameOfFileToStore == null) {
			if (other.nameOfFileToStore != null)
				return false;
		} else if (!nameOfFileToStore.equals(other.nameOfFileToStore))
			return false;
		if (sftpDestFolder == null) {
			if (other.sftpDestFolder != null)
				return false;
		} else if (!sftpDestFolder.equals(other.sftpDestFolder))
			return false;
		if (sourceFileName == null) {
			if (other.sourceFileName != null)
				return false;
		} else if (!sourceFileName.equals(other.sourceFileName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SftpUploadInfoBean [sftpDestFolder=" + sftpDestFolder + ", sourceFileName=" + sourceFileName
				+ ", nameOfFileToStore=" + nameOfFileToStore + "]";
	}
}
