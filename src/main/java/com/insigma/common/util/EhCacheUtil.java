package com.insigma.common.util;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * EhCacheUtil
 *
 * @author admin
 * @version 1.0
 * @date 2016-07-23
 */
public class EhCacheUtil {

    private static CacheManager manager;

    static {
        if (manager == null) {
            InputStream in = EhCacheUtil.class.getClassLoader().getResourceAsStream("ehcache-shiro.xml");
            manager = CacheManager.create(in);
        }
    }

    public static CacheManager getManager() {
        return manager;
    }


    public static void setManager(CacheManager manager) {
        EhCacheUtil.manager = manager;
    }
    
    public static Object getParamFromCache(String key){
    	Element element=EhCacheUtil.getManager().getCache("webcache").get(key);
    	if(element!=null){
    		return (Object)element.getValue();
    	}else{
    		return null;
    	}
    }

    /**
     * 将对象暂时加入缓存(很鸡肋的方法,只为适配前端select插件)
    * @author: liangy  
    * @date 2018年11月30日
    * @param @param key
    * @param @param object
    * @param @return    
    * @return boolean   
    * @throws
     */
    public static boolean setParamFromCache(String key, Object object){
    	Cache cache = EhCacheUtil.getManager().getCache("webcache"); 
    	if (StringUtils.isNotBlank(key) && object != null && cache != null) {
    		Element element = new Element(key, object); 
        	cache.put(element);
        	if (getParamFromCache(key) != null) {
        		return true;
        	}
        	return false;
    	}
    	return false;
    }
}