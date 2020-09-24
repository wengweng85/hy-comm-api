package com.insigma.common.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * Created by admin on 2014-12-19.
 */
public class SpringUtil {

    /**
     * ªÒ»°spring bean
     * @param servletContext
     * @param beanname
     * @return
     */
    public static Object getBean(ServletContext servletContext,String beanname){
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        Object bean =  wac.getBean(beanname);
        return bean;
    }

}
