package com.insigma.mvc.service.rediscached.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.insigma.mvc.service.rediscached.RedisCachedService;

/**
 * 缓存服务service
 * @author admin
 *
 */

@Service
public class RedisCachedServiceImpl implements RedisCachedService {

	/**
	 */
	Log log=LogFactory.getLog(RedisCachedServiceImpl.class);
	
	@Autowired
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
	 * 设置缓存
	 * @param redis_key
	 * @param key_value
	 */
	@Override
	public void setRedisValue(String redis_key,String key_value) {
		//过期时间
		redisTemplate.boundValueOps(redis_key.toLowerCase()).set(key_value,10,TimeUnit.MINUTES);
		log.info("redis key"+redis_key+"设置成功。");
	}

}