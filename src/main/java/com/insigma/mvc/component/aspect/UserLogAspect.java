package com.insigma.mvc.component.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.insigma.common.annotation.UserLog;

/**
 * 用户页面日志记录
 */
@Aspect
@Component
public class UserLogAspect {
 
	private Log log=LogFactory.getLog(RepeatSubmitAspect.class);

    /**
     * 定义匹配规则，以便于后续拦截直接拦截submit方法，不用重复表达式
     */
    @Pointcut(value = "@annotation(com.insigma.common.annotation.UserLog)")
    public void submit() {
    }

    @Before("submit()&&@annotation(userlog)")
    public void doBefore(JoinPoint joinPoint, UserLog userlog) {
        // 拼装参数
        StringBuffer sb = new StringBuffer();
        for(Object object : joinPoint.getArgs()){
            sb.append(object);
        }

        System.out.println(sb.toString());
      
    }

    @Around("submit()&&@annotation(userlog)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, UserLog userlog) throws Throwable {
    	log.debug("环绕通知：");
        Object result = null;
        result = proceedingJoinPoint.proceed();
        return result;
    }

    @After("submit()")
    public void doAfter() {
    	log.debug("******拦截后的逻辑******");
    }
   
}
