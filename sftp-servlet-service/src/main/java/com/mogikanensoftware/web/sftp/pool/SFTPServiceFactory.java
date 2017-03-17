package com.mogikanensoftware.web.sftp.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogikanensoftware.web.sftp.bean.SFTPServerConnectionSettings;
import com.mogikanensoftware.web.sftp.service.SFTPService;
import com.mogikanensoftware.web.sftp.service.SFTPServiceException;
import com.mogikanensoftware.web.sftp.service.impl.SFTPServiceImpl;

/**
 * 
 * @author vladislav_voinov
 *
 */
public class SFTPServiceFactory extends BasePooledObjectFactory<SFTPService>{

	private static final Logger logger = LogManager.getLogger(SFTPServiceFactory.class);
	
	private SFTPServerConnectionSettings settings;
	
	public SFTPServiceFactory(SFTPServerConnectionSettings setings) {
		super();
		this.settings = setings;
		if(logger.isDebugEnabled()){
			logger.debug("setings->"+setings);
		}
	}


	@Override
	public void destroyObject(PooledObject<SFTPService> p) throws Exception {
		logger.debug("destroyObject...");
		p.getObject().disconnect();
	}

	@Override
	public boolean validateObject(PooledObject<SFTPService> p) {
		
		logger.debug("validateObject...");
		
		try {
			boolean isValid = p.getObject().isConnected();
			
			logger.debug("isValid->"+isValid);			
			return isValid;
		} catch (SFTPServiceException e) {
			return false;
		}
	}

	@Override
	public void activateObject(PooledObject<SFTPService> p) throws Exception {
		logger.debug("activateObject...");
		super.activateObject(p);		
	}

	@Override
	public SFTPService create() throws Exception {
		
		logger.debug("create...");
		
		SFTPService instance = new SFTPServiceImpl();
		instance.connect(this.settings);
		return instance;
	}

	@Override
	public PooledObject<SFTPService> wrap(SFTPService obj) {
		
		logger.debug("wrap...");
		
		return new DefaultPooledObject<SFTPService>(obj);
	}

}
