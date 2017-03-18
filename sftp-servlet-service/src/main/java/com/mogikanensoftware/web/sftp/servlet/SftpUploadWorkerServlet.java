package com.mogikanensoftware.web.sftp.servlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogikanensoftware.web.sftp.bean.SFTPServerConnectionSettings;
import com.mogikanensoftware.web.sftp.bean.SftpUploadInfoBean;
import com.mogikanensoftware.web.sftp.pool.SFTPServiceFactory;
import com.mogikanensoftware.web.sftp.service.SFTPService;
import com.mogikanensoftware.web.sftp.service.SftpUploadServiceWorker;
/**
 * 
 * @author vladislav_voinov
 *
 */
@WebServlet(
		 name = "SftpUploadWorkerServlet",
		 urlPatterns = "/SftpUploadWorkerServlet",
		 initParams = {
				 	@WebInitParam(name="sftpServerName",value="127.0.0.1"),
				 	@WebInitParam(name="sftpUsername",value="sftpuser"),
				 	@WebInitParam(name="sftpPassword",value="Welcome1"),
				 	@WebInitParam(name="sftpPathToKey",value=""),
				 	@WebInitParam(name="sftpPort",value=""),
				 	@WebInitParam(name="uploadLocalTempDir",value="/users/sftpuser/sftp_upload_temp")
		 		}
		 )
public class SftpUploadWorkerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger= LogManager.getLogger(SftpUploadWorkerServlet.class);
       
	private  ExecutorService sftpUploadExcutor;
	private GenericObjectPool<SFTPService> sftpServicePool;
	
	@Override
	public void destroy() {		
		super.destroy();
		if(logger.isDebugEnabled()){
			logger.debug("Destorying...");
			logger.debug("sftpUploadExcutor->"+sftpUploadExcutor);
		}		
		if(this.sftpUploadExcutor!=null){
			this.sftpUploadExcutor.shutdown();
		}
		
		if(this.sftpServicePool!=null){
			this.sftpServicePool.clear();
			this.sftpServicePool.close();			
		}
	}

	protected String getParamValueNotEmpty(String paramValue){
		if(paramValue!=null && paramValue.trim().length()>0){
			return paramValue.trim();
		}else{
			return null;
		}
	}
	
	protected String generateLocalTmpFileName(){
		return UUID.randomUUID().toString()+System.currentTimeMillis();
	}
	
	protected String copyFile(String fullFileName) throws IOException{
		Path source = Paths.get(fullFileName);
		String targetFullFileName = this.getInitParameter("uploadLocalTempDir")+"/"+generateLocalTmpFileName();
		Path target = Paths.get(targetFullFileName);		
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		return targetFullFileName;	
		
	}
	
	@Override
	public void init() throws ServletException {		
		super.init();
		
		int maxNum = 10;
		
		sftpUploadExcutor = Executors.newFixedThreadPool(maxNum);
		

		if(logger.isInfoEnabled()){
			logger.info("sftpUploadExcutor created->"+sftpUploadExcutor.toString());
		}
		
		SFTPServerConnectionSettings settings = new SFTPServerConnectionSettings();
		settings.setServer(this.getInitParameter("sftpServerName"));
		settings.setUsername(this.getInitParameter("sftpUsername"));		
		settings.setPassword(getParamValueNotEmpty(this.getInitParameter("sftpPassword")));
		settings.setPathToKey(getParamValueNotEmpty(this.getInitParameter("sftpPathToKey")));
		String sftpPortValue = getParamValueNotEmpty(this.getInitParameter("sftpPort"));
		if(sftpPortValue!=null){
			settings.setPort(Integer.parseInt(sftpPortValue));
		}else{
			settings.setPort(22);	
		}
		
		if(logger.isInfoEnabled()){
			logger.info("settings->"+settings.toString());
		}
		
		sftpServicePool = new GenericObjectPool<SFTPService>(new SFTPServiceFactory(settings));
		sftpServicePool.setTestOnBorrow(true);
		sftpServicePool.setTestOnCreate(true);	
		sftpServicePool.setMaxTotal(maxNum);	
		
		if(logger.isInfoEnabled()){
			logger.info("sftpServicePool->"+sftpServicePool.toString());
		}
	}
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SftpUploadWorkerServlet() {
        super();       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {					
		if(logger.isDebugEnabled()){
			logger.debug("Starting doPost...");
		}
		
		try {
			
			 SftpUploadInfoBean sftpUploadInfoBean = this.from(request);
			 			 			 
			 if(logger.isDebugEnabled()){
					logger.debug("initial sftpUploadInfoBean->"+sftpUploadInfoBean);					
				}
			 
			 //copy file to local folder to release client from holding it
			 String tmpFullFileName = this.copyFile(sftpUploadInfoBean.getSourceFileName());
			 sftpUploadInfoBean.setSourceFileName(tmpFullFileName);
			 
			 if(logger.isDebugEnabled()){
					logger.debug("tmpFullFileName->"+sftpUploadInfoBean.getSourceFileName());					
				}
			 
			 Runnable worker = new SftpUploadServiceWorker(sftpUploadInfoBean,this.sftpServicePool);
			 sftpUploadExcutor.execute(worker);     	    			 
			 
			 
			 logger.debug("Finished all threads");
		     response.getWriter().print("SUCCESS");
		} catch (Exception e) {
			
			logger.error(e.getMessage(),e);
			response.getWriter().print("FAIL");
		}
         
	        		
	}

	protected SftpUploadInfoBean from(HttpServletRequest request){
		SftpUploadInfoBean bean = new SftpUploadInfoBean();
			
		bean.setSftpDestFolder(request.getParameter("sftpDestFolder"));
		bean.setSourceFileName(request.getParameter("sourceFileName"));
		bean.setNameOfFileToStore(request.getParameter("nameOfFileToStore"));		
		
		return bean;
	}	 
}
