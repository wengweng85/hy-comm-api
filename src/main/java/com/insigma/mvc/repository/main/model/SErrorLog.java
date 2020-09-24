package com.insigma.mvc.repository.main.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SErrorLog
 */
@TableName("s_errorlog")//指定表名
@Data
@NoArgsConstructor
public class SErrorLog  implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logid;
    private String message;
    private String stackmsg;
    private Date logtime;
    private String exceptiontype;
    private String usergent;
    private String ipaddr;
    private String url;
    private transient String logtime_string;//发生时间
    private String referer;
    private String userid;
    private String cookie;
    
    
}
