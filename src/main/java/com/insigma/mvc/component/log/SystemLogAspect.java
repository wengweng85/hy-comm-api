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
 * ��־��¼��
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

    @Pointcut("execution( * com.insigma.mvc.controller.*.*(..))")//����..����������Ŀ¼����������������..�������в���
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        System.out.println("�����ַ : " +request.getRequestURL().toString());
        logger.info("�����ַ : " +request.getRequestURL().toString());
        logger.info("HTTP METHOD : " + request.getMethod());
        // ��ȡ��ʵ��ip��ַ
        logger.info("IP : " + IPUtil.getInetAddress());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("���� : " + Arrays.toString(joinPoint.getArgs()));
    }
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")// returning��ֵ��doAfterReturning�Ĳ�����һ��
    public void doAfterReturning(Object ret) throws Throwable {
        // ���������󣬷�������(����ֵ̫����ʱ����ӡ��������洢�ռ�ĵ�ַ)
        String params = JsonUtils.objectToJson(ret);
        logger.info("����ֵ : " + params);
        //String logid=logidThreadLocal.get();
        //updateSLog(logid,params);
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();// ob Ϊ�����ķ���ֵ
        logger.info("��ʱ : " + (System.currentTimeMillis() - startTime));
        // ִ��ʱ��(����)
        long time = System.currentTimeMillis() - startTime;
        //�첽������־
        saveLog(pjp, time);
        return ob;
    }
	 /**
     * ������־
     * @param joinPoint
     * @param time
     * @throws InterruptedException
     */
    void saveLog(ProceedingJoinPoint joinPoint, long time) throws InterruptedException {
        // ��ȡrequest
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SLog slog = new SLog();
        slog.setLogtype("1");//1.������־ 2.�ӿ���־
        String logid=IDGenertor.generate();
        logidThreadLocal.set(logid);
        slog.setLogid(logid);
        // ����ķ�����
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        slog.setMessage(className + "." + methodName + "()");
        slog.setMethod(className + "." + methodName + "()");
        // ����Ĳ���
        Object[] args = joinPoint.getArgs();
        try {
            String params = JsonUtils.objectToJson(args[0]).substring(0, 4999);
            slog.setQueryparam(params);
        } catch (Exception e) {
        	slog.setQueryparam(Arrays.toString(joinPoint.getArgs()));
        }
        slog.setUserid("0000000");
        slog.setUrl(request.getRequestURL().toString());
        // ����IP��ַ
        slog.setIpaddr(IPUtil.getInetAddress());
        slog.setCost(new Long(time).toString());
        slog.setUsergent(request.getHeader("user-agent"));
        // ϵͳ��ǰʱ��
        Date date = new Date();
        slog.setLogtime(date);
        log.debug("����ӿ���־");
        // ������־
        sLogService.saveSLog(slog);
        log.debug("����ӿ���־���!");
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
		log.debug("������־");
		sLogService.updateSLog(slog);
		log.debug("������־���!");
    }
}
