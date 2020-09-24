package com.insigma.mvc.component.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.insigma.common.util.IPUtil;
import com.insigma.mvc.repository.main.model.SLog;

public class LogUtil {

    /**
     * 文件记录
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    public static SLog parseRequestToLog(HttpServletRequest request, HttpServletResponse response, Exception e) {
        SLog slog = new SLog();
        //请求信息
        slog.setUrl(request.getRequestURI());
        slog.setMethod(request.getMethod());
        slog.setQueryparam(getQueryParam(request));
        String ip = IPUtil.getClientIpAddr(request);
        slog.setIpaddr(ip);
        slog.setAppkey(request.getHeader("appkey"));
        slog.setUsergent(request.getHeader("user-agent"));
        slog.setReferer(request.getHeader("Referer")==null?"":request.getHeader("Referer"));
        StringBuffer url = new StringBuffer(request.getRequestURI().toString());
        slog.setUrl(url.toString());
        Cookie[] cookies = request.getCookies();
        StringBuffer sb = new StringBuffer("");
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                sb.append(cookie.getName());
                sb.append(":");
                sb.append(cookie.getValue());
            }
            slog.setCookie(sb.length() > 500 ? sb.substring(0, 499) : sb.toString());
        }else{
        	slog.setCookie("");
        }
        //response信息
        try {
            //slog.setResponsemsg(parseResponse(response));
            if(e!=null){
                slog.setExceptiontype(e.getClass().getName());
            }else{
                slog.setExceptiontype("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return slog;
    }

    /**
     * 将异常打印出来
     *
     * @param e
     * @return
     */
    private static String getStackMsg(Exception e) {
        if (e == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }


    /**
     * 得到请求参数
     *
     * @param request
     * @return
     */
    public static String getQueryParam(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        Map map = request.getParameterMap();
        Set keSet = map.entrySet();
        for (Iterator itr = keSet.iterator(); itr.hasNext(); ) {
            Map.Entry me = (Map.Entry) itr.next();
            Object ok = me.getKey();
            Object ov = me.getValue();
            String[] value = new String[1];
            if (ov instanceof String[]) {
                value = (String[]) ov;
            } else {
                value[0] = ov.toString();
            }
            for (String k: value) {
                sb.append(ok);
                sb.append("=");
                sb.append(k);
                sb.append("&");
            }
        }
        return sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : "";
    }

}
