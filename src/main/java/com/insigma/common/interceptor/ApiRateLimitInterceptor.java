package com.insigma.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.util.concurrent.RateLimiter;
import com.insigma.dto.R;
import com.insigma.dto.enums.SysCode;

import net.sf.json.JSONObject;

/**
 * ApiRateLimitInterceptor
 * 限流
 *
 * @author wengsh
 */
public class ApiRateLimitInterceptor extends HandlerInterceptorAdapter {

    private Log log = LogFactory.getLog(ApiRateLimitInterceptor.class);

    private static final RateLimiter rateLimiter=RateLimiter.create(10);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	log.debug("判断接口是否限流....");
    	if(!rateLimiter.tryAcquire()){
    	   log.info("限流中...");
    	   R<String> dto = new R<>();
    	   PrintWriter writer = response.getWriter();
           dto.setSuccess(false);
           dto.setErrorcode(SysCode.API_RATELIMIT.getCode());
           dto.setErrormsg(SysCode.API_RATELIMIT.getCodemsg());
           String encodedata= JSONObject.fromObject(dto).toString();
           log.info(encodedata);
           writer.write(encodedata);
           writer.flush();
    	   return false;
       }
        return super.preHandle(request, response, handler);
    }

 
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
      
    }


}
