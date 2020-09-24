package com.insigma.mvc.service.pub;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.insigma.dto.R;


/**
 * ¹«¹²²éÑ¯service
 * @author admin
 *
 */
public interface PubService  {
	
	 R call(String interface_id,HashMap map,HttpServletRequest request)  throws Exception ;
	 
}
