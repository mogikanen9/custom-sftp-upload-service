package com.mogikanensoftware.web.sftp.service;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogikanensoftware.web.sftp.bean.SftpUploadInfoBean;

/**
 * 
 * @author vladislav_voinov
 *
 */
public class SftpUploadServiceWorker implements Runnable{

	private static final Logger logger = LogManager.getFormatterLogger(SftpUploadServiceWorker.class);
	
	private SftpUploadInfoBean sftpUploadInfoBean;
	private GenericObjectPool<SFTPService> sftpServicePool;
			
	
	public SftpUploadInfoBean getSftpUploadInfoBean() {
		return sftpUploadInfoBean;
	}

	public void setSftpUploadInfoBean(SftpUploadInfoBean sftpUploadInfoBean) {
		this.sftpUploadInfoBean = sftpUploadInfoBean;
	}

	public SftpUploadServiceWorker(SftpUploadInfoBean sftpUploadInfoBean, GenericObjectPool<SFTPService> sftpServicePool) {
		super();
		this.sftpUploadInfoBean = sftpUploadInfoBean;
		this.sftpServicePool = sftpServicePool;
	}

	

	public GenericObjectPool<SFTPService> getSftpServicePool() {
		return sftpServicePool;
	}

	public void setSftpServicePool(GenericObjectPool<SFTPService> sftpServicePool) {
		this.sftpServicePool = sftpServicePool;
	}

	@Override
	public void run() {
		
		SFTPService service = null;
		
     	try {
     		
     		 service = this.sftpServicePool.borrowObject();
			 if(logger.isDebugEnabled()){
					logger.debug("service borrowed from pool->"+service);
				}
     		
			service.uploadFile(sftpUploadInfoBean.getSftpDestFolder(), sftpUploadInfoBean.getSourceFileName(), sftpUploadInfoBean.getNameOfFileToStore());
			
			Files.deleteIfExists(Paths.get(sftpUploadInfoBean.getSourceFileName()));
			
		} catch (SFTPServiceException e) {
			logger.error("Could not upload file->"+sftpUploadInfoBean.getSourceFileName()+" due to ->"+e.getMessage(),e);
		} catch (Exception e) {
			logger.error("Unable to borrow SFTPService object from the pool ->"+e.getMessage(),e);
		}finally {
			if(service!=null){
				this.sftpServicePool.returnObject(service);
			}
		}
		
	}

}
