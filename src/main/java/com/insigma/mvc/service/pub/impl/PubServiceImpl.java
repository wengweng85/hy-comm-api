package com.insigma.mvc.service.pub.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insigma.common.util.IDGenertor;
import com.insigma.common.util.IPUtil;
import com.insigma.common.util.JsonUtils;
import com.insigma.common.util.MD5Util;
import com.insigma.dto.R;
import com.insigma.dto.enums.SysCode;
import com.insigma.mvc.repository.main.dao.pub.PubMapper;
import com.insigma.mvc.repository.main.model.SLog;
import com.insigma.mvc.service.log.SLogService;
import com.insigma.mvc.service.pub.PubService;
import com.insigma.mvc.service.rediscached.RedisCachedService;

/**
 * 
 * 公共接口服务
 * 根据接口名转换成表名动态获取数据
 * @author admin
 *
 */

@Service
public class PubServiceImpl implements PubService {

	/**
	 */
	Log log=LogFactory.getLog(PubServiceImpl.class);
	
	@Resource
	private PubMapper pubmapper;
	
	@Resource
	private SLogService slogservice;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private RedisCachedService redisCachedService;
	
	
	@Autowired(required = false)
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(stringSerializer);
		this.redisTemplate = redisTemplate;
	}

	@Override
	@Transactional
	public R call(String interface_id, HashMap map,HttpServletRequest request) throws Exception {
		//1.第一步 默认根据接口名+参数动态md5 查询redis中是否有数据
		//2.第二步 如果缓存没有数据 根据接口名动态查询 接口编号对应表名（接口编号与表名一致) 查询成功后写入缓存
		//3.第三 步 如果查询不到调用webservice接口 查询成功后写入缓存
		String redis_key="interface_"+interface_id;
		if(!map.isEmpty()) {
			redis_key+="_"+MD5Util.MD5Encode(JsonUtils.objectToJson(map));
		}
		saveLog(request,interface_id,map);
		log.info("1.redis key"+redis_key);
		log.info("第一步 默认根据接口名+参数动态md5 查询redis中是否有数据");
		String redis_key_value = (String) redisTemplate.boundValueOps(redis_key.toLowerCase()).get();
		if(redis_key_value==null) {
			log.info("第二步如果缓存没有数据 根据接口名动态查询 接口编号对应表名（接口编号与表名一致)");
			List<HashMap<String,Object>> list_result=new ArrayList<HashMap<String,Object>>();
			try {
				HashMap reqmap=(HashMap)map.clone();
				reqmap.put("interface_id", interface_id);
				list_result=pubmapper.commonQuery(reqmap);
			}catch(Exception ex) {
				log.error("接口"+interface_id+"还未配置表数据");
				return R.fail(SysCode.NOINTERFACE,"接口编号为"+interface_id+"的接口不存在,请联系管理员");
				//ex.printStackTrace();
			}
			if(list_result.size()>0) {
				if(list_result.size()==1) {
					redisCachedService.setRedisValue(redis_key,JsonUtils.objectToJson(list_result.get(0)));
					return R.success(list_result.get(0));
				}else {
					redisCachedService.setRedisValue(redis_key,JsonUtils.objectToJson(list_result));
					return R.success(list_result);
				}
			}else {
	    		return R.fail();
			}
        }else {
        	log.info("redis key"+redis_key+"取值成功");
        	return R.success(redis_key_value);
        }
	}
	
	/**
	 * 
	 * @param request
	 * @param interface_id
	 */
	public void saveLog(HttpServletRequest request,String interface_id,HashMap reqbody) {
		SLog slog = new SLog();
		slog.setLogid(IDGenertor.generate());
		slog.setLogtype("2");
		slog.setInterfacecode(interface_id);
		slog.setQueryparam(JsonUtils.objectToJson(reqbody));
		slog.setUserid("appuser");
		StringBuffer url=request.getRequestURL();
        if(request.getQueryString()!=null&&!("").equals(request.getQueryString())){
            url.append("?"+request.getQueryString());
        }
        slog.setUrl(url.toString());
        // 设置IP地址
        slog.setIpaddr(IPUtil.getInetAddress());
        slog.setUsergent(request.getHeader("user-agent"));
        // 系统当前时间
        Date date = new Date();
        slog.setLogtime(date);
        slogservice.saveSLog(slog);
	}
}