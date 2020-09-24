package com.insigma.mvc.component.aspect;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.insigma.common.annotation.AvoidRepeatSubmit;
import com.insigma.common.util.MD5Util;

/**
 * 防止重复提交的切面
 */
@Aspect
@Component
public class RepeatSubmitAspect {
 
	private Log log=LogFactory.getLog(RepeatSubmitAspect.class);
	@Resource
    private RedisTemplate redisTemplate;
	
	@Autowired(required = false)
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(stringSerializer);
		this.redisTemplate = redisTemplate;
	}

    /**
     * 定义匹配规则，以便于后续拦截直接拦截submit方法，不用重复表达式
     */
    @Pointcut(value = "@annotation(com.insigma.common.annotation.AvoidRepeatSubmit)")
    public void submit() {
    }

    @Before("submit()&&@annotation(avoidRepeatSubmit)")
    public void doBefore(JoinPoint joinPoint, AvoidRepeatSubmit avoidRepeatSubmit) {
        // 拼装参数
        StringBuffer sb = new StringBuffer();
        for(Object object : joinPoint.getArgs()){
            sb.append(object);
        }

        System.out.println(sb.toString());
        String key = MD5Util.MD5Encode(sb.toString());
        long expireTime = avoidRepeatSubmit.expireTime();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object object = valueOperations.get(key);
        if(null != object){
            throw new RuntimeException("您已经提交了请求，请不要重复提交！");
        }
        valueOperations.set(key, "1", expireTime, TimeUnit.MILLISECONDS);
    }

    @Around("submit()&&@annotation(avoidRepeatSubmit)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, AvoidRepeatSubmit avoidRepeatSubmit) throws Throwable {
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
