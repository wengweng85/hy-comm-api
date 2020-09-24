package com.insigma.mvc.component.log;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.insigma.common.util.HttpContextUtils;
import com.insigma.common.util.IDGenertor;
import com.insigma.common.util.IPUtil;
import com.insigma.common.util.JsonUtils;
import com.insigma.mvc.repository.main.model.SLog;
import com.insigma.mvc.service.log.SLogService;


/**
 * 日志记录器
 * 
 * @author wengsh
 *
 */
@Aspect
@Component
public class SystemLogAspect {
	
	@Autowired
    private SLogService sLogService;
	
	Log log = LogFactory.getLog(SystemLogAspect.class);

	private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
	
	private NamedThreadLocal<String> logidThreadLocal = new NamedThreadLocal<>("logid");

    @Pointcut("execution( * com.insigma.mvc.controller.*.*(..))")//两个..代表所有子目录，最后括号里的两个..代表所有参数
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        System.out.println("请求地址 : " +request.getRequestURL().toString());
        logger.info("请求地址 : " +request.getRequestURL().toString());
        logger.info("HTTP METHOD : " + request.getMethod());
        // 获取真实的ip地址
        logger.info("IP : " + IPUtil.getInetAddress());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
    }
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容(返回值太复杂时，打印的是物理存储空间的地址)
        String params = JsonUtils.objectToJson(ret);
        logger.info("返回值 : " + params);
        //String logid=logidThreadLocal.get();
        //updateSLog(logid,params);
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();// ob 为方法的返回值
        logger.info("耗时 : " + (System.currentTimeMillis() - startTime));
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - startTime;
        //异步保存日志
        saveLog(pjp, time);
        return ob;
    }
	 /**
     * 保存日志
     * @param joinPoint
     * @param time
     * @throws InterruptedException
     */
    void saveLog(ProceedingJoinPoint joinPoint, long time) throws InterruptedException {
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SLog slog = new SLog();
        slog.setLogtype("1");//1.请求日志 2.接口日志
        String logid=IDGenertor.generate();
        logidThreadLocal.set(logid);
        slog.setLogid(logid);
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        slog.setMessage(className + "." + methodName + "()");
        slog.setMethod(className + "." + methodName + "()");
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JsonUtils.objectToJson(args[0]).substring(0, 4999);
            slog.setQueryparam(params);
        } catch (Exception e) {
        	slog.setQueryparam(Arrays.toString(joinPoint.getArgs()));
        }
        slog.setUserid("0000000");
        slog.setUrl(request.getRequestURL().toString());
        // 设置IP地址
        slog.setIpaddr(IPUtil.getInetAddress());
        slog.setCost(new Long(time).toString());
        slog.setUsergent(request.getHeader("user-agent"));
        // 系统当前时间
        Date date = new Date();
        slog.setLogtime(date);
        log.debug("保存接口日志");
        // 保存日志
        sLogService.saveSLog(slog);
        log.debug("保存接口日志完成!");
    }
    
    /**
     * 
     * @param logid
     * @param responsemsg
     */
    public void updateSLog(String logid,String responsemsg) throws InterruptedException {
    	SLog slog = new SLog();
		slog.setLogid(logid);
		slog.setResponsemsg(responsemsg);
		log.debug("更新日志");
		sLogService.updateSLog(slog);
		log.debug("更新日志完成!");
    }
}
