package com.insigma.resolver;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.insigma.dto.R;
import com.insigma.dto.enums.SysCode;
import com.insigma.mvc.service.log.SLogService;

import net.sf.json.JSONObject;


public class MyCustomSimpleMappingExceptionResolver  extends  SimpleMappingExceptionResolver {
	Log log=LogFactory.getLog(MyCustomSimpleMappingExceptionResolver.class);

	
	@Resource
    private SLogService slogservice;
	
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        e.printStackTrace();
	    String errorlogid=slogservice.saveErrorLog(e, request);
    	try {
             PrintWriter writer = response.getWriter();
             R<String> dto = new R<String>();
             dto.setSuccess(false);
             dto.setErrorcode(SysCode.ERROR.getCode());
             dto.setErrormsg(SysCode.ERROR.getCodemsg()+"请联系管理员。异常信息编号:"+errorlogid);
             String encodedata= JSONObject.fromObject(dto).toString();
             writer.write(encodedata);
             writer.flush();
             writer.close();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return null;
     }
}
