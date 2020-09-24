package com.insigma.common.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class ServletContextUtil implements ServletContextAware{

	private ServletContext servletContext;   
	  
	@Override
    public void setServletContext(ServletContext sc) {
	    this.servletContext = sc;   
	}
	
	protected ServletContext getServletContextContext(){
		return servletContext;
	}
}
