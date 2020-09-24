package com.insigma.mvc.service.log;

import javax.servlet.http.HttpServletRequest;

import com.insigma.mvc.repository.main.model.SLog;


/**
 * 框架系统日志服务
 * @author admin
 *
 */
public interface SLogService  {
	
	 public String saveSLog(SLog slog) ;
	 public void updateSLog(SLog slog) ;
	 public String saveErrorLog(Exception e, HttpServletRequest request);
	 
}
