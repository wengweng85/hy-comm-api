package com.insigma.mvc.service.log.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.insigma.common.util.IDGenertor;
import com.insigma.common.util.IPUtil;
import com.insigma.mvc.repository.main.dao.log.SLogMapper;
import com.insigma.mvc.repository.main.model.SErrorLog;
import com.insigma.mvc.repository.main.model.SLog;
import com.insigma.mvc.service.log.SLogService;

/**
 * 
 * 框架系统日志管理
 * @author admin
 *
 */

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SLogServiceImpl implements SLogService {

	private Log log= LogFactory.getLog(SLogServiceImpl.class);
	
	@Autowired
	private SLogMapper logMapper;

	@Override
	@Async
	public String saveSLog(SLog slog){
		log.debug("开始记录日志");
        long start = System.currentTimeMillis();
        //Thread.sleep(2000);
        slog.setLogid("S"+IDGenertor.generate());
		logMapper.insert(slog);
		long end = System.currentTimeMillis();
		log.debug("完成记录日志，耗时：" + (end - start) + "毫秒");
		return slog.getLogid();
	}
	
	@Override
	//@Async
	public void updateSLog(SLog slog) {
		System.out.println("开始更新日志");
        long start = System.currentTimeMillis();
        //Thread.sleep(2000);
		logMapper.updateById(slog);
		long end = System.currentTimeMillis();
		System.out.println("完成更新日志，耗时：" + (end - start) + "毫秒");
	}
	

	@Override
	//@Async
	public String saveErrorLog(Exception e, HttpServletRequest request) {
		// TODO Auto-generated method stub
		SErrorLog sErrorLog=new SErrorLog();
		sErrorLog.setLogid("E"+IDGenertor.generate());
		if(e.getMessage()!=null){
        	 sErrorLog.setMessage(e.getMessage().length()>500?e.getMessage().substring(0,499):e.getMessage()); 
        }
        sErrorLog.setStackmsg(getStackMsg(e));
        sErrorLog.setExceptiontype(e.getClass().getName());
        String ip=IPUtil.getClientIpAddr(request);
        /*IPSeekerUtil util=new IPSeekerUtil();*/
        sErrorLog.setIpaddr(ip);
        /*String country=util.getIpCountry(ip);
        sErrorLog.setIpaddr(country+"("+ip+")");*/
        sErrorLog.setUsergent(request.getHeader("user-agent"));
        sErrorLog.setReferer(request.getHeader("Referer"));
        sErrorLog.setLogtime(new Date());
        StringBuffer url=request.getRequestURL();
        if(request.getQueryString()!=null&&!("").equals(request.getQueryString())){
            url.append("?"+request.getQueryString());
        }
        sErrorLog.setUrl(url.toString());
        String cookie="";
        if(request.getCookies()!=null){
            Cookie[] cookies=request.getCookies();
            for(int i=0;i<cookies.length;i++){
                Cookie tempcookie=cookies[i];
                cookie+=tempcookie.getName()+":"+tempcookie.getValue();
            }
            sErrorLog.setCookie(cookie.length() >500?cookie.substring(0,499):cookie);
        }
        logMapper.saveSErrorLog(sErrorLog);
		return sErrorLog.getLogid();
	}
	
	
	  /**
     * 将异常打印出来
     * @param e
     * @return
     */
    private static String getStackMsg(Exception e) {
    	if(e!=null){
    		StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        return sw.toString();
    	}else{
    		return "";
    	}
    }
}