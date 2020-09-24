package com.insigma.mvc.repository.main.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the SYS_LOG database table.
 * 
 */
@Entity
@Table(name="SYS_LOG")
@NamedQuery(name="SysLog.findAll", query="SELECT s FROM SysLog s")
public class SysLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String logid;

	private String appkey;

	private String cookie;

	private String cost;

	private String exceptiontype;

	private String interfacetype;

	private String ipaddr;

	@Temporal(TemporalType.DATE)
	private Date logtime;

	private String logtype;

	@Column(name="\"MESSAGE\"")
	private String message;

	private String method;

	@Lob
	private String queryparam;

	private String referer;

	@Lob
	private String responsemsg;

	@Lob
	private String stackmsg;

	private String success;

	private String token;

	private String url;

	private String usergent;

	private String userid;

	public SysLog() {
	}

	public String getLogid() {
		return this.logid;
	}

	public void setLogid(String logid) {
		this.logid = logid;
	}

	public String getAppkey() {
		return this.appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getCookie() {
		return this.cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getCost() {
		return this.cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getExceptiontype() {
		return this.exceptiontype;
	}

	public void setExceptiontype(String exceptiontype) {
		this.exceptiontype = exceptiontype;
	}

	public String getInterfacetype() {
		return this.interfacetype;
	}

	public void setInterfacetype(String interfacetype) {
		this.interfacetype = interfacetype;
	}

	public String getIpaddr() {
		return this.ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public Date getLogtime() {
		return this.logtime;
	}

	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}

	public String getLogtype() {
		return this.logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getQueryparam() {
		return this.queryparam;
	}

	public void setQueryparam(String queryparam) {
		this.queryparam = queryparam;
	}

	public String getReferer() {
		return this.referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getResponsemsg() {
		return this.responsemsg;
	}

	public void setResponsemsg(String responsemsg) {
		this.responsemsg = responsemsg;
	}

	public String getStackmsg() {
		return this.stackmsg;
	}

	public void setStackmsg(String stackmsg) {
		this.stackmsg = stackmsg;
	}

	public String getSuccess() {
		return this.success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsergent() {
		return this.usergent;
	}

	public void setUsergent(String usergent) {
		this.usergent = usergent;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}