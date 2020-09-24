package com.insigma.mvc.repository.main.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统日志表
 *
 * @author admin
 */
@TableName("s_log")//指定表名
@Data
@NoArgsConstructor
public class SLog implements java.io.Serializable {

    private String messagetype;//消息类型
    private String logid;//			日志id
    private String interfacetype;//			接口类型代码
    private String interfacecode;//			接口类型代码
    private String message;//		日志标题
    private Date logtime;//		发生时间
    private String logtime_string;//发生时间
    private String cost;//		请求耗费时间
    private String stackmsg;//		异常栈信息
    private String exceptiontype;//		异常类型
    private String usergent;//		user-agent
    private String ipaddr;//		客户端ip地址
    private String referer;//		refer
    private String url;//		请求的地址
    private String userid;//		当前操作人员id
    private String cookie;//		cookie
    private String appkey;//		appkey
    private String queryparam;//		请求参数信息
    private String method;//		请求方法类型
    private String success;//	请求是否成功
    private String responsemsg;//	返回信息
    private String token;//请求jwt状态码
    private String content;
    private String logtype;
}