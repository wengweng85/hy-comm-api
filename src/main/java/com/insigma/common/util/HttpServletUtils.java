package com.insigma.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class HttpServletUtils {
    public static boolean jsAjax(HttpServletRequest req){
        boolean isAjaxRequest = false;
        if(!StringUtils.isBlank(req.getHeader("x-requested-with")) && req.getHeader("x-requested-with").equals("XMLHttpRequest")){
            isAjaxRequest = true;
        }
        return isAjaxRequest;
    }
}
