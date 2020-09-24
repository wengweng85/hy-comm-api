package com.insigma.mvc.repository.main.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the SYS_ERRORLOG database table.
 * 
 */
@Entity
@Table(name="SYS_ERRORLOG")
@NamedQuery(name="SysErrorlog.findAll", query="SELECT s FROM SysErrorlog s")
public class SysErrorlog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String logid;

	private String cookie;

	private String exceptiontype;

	private String ipaddr;

	@Temporal(TemporalType.DATE)
	private Date logtime;

	@Column(name="\"MESSAGE\"")
	private String message;

	private String referer;

	@Lob
	private String stackmsg;

	private String url;

	private String usergent;

	private String userid;

	public SysErrorlog() {
	}

	public String getLogid() {
		return this.logid;
	}

	public void setLogid(String logid) {
		this.logid = logid;
	}

	public String getCookie() {
		return this.cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getExceptiontype() {
		return this.exceptiontype;
	}

	public void setExceptiontype(String exceptiontype) {
		this.exceptiontype = exceptiontype;
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

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReferer() {
		return this.referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getStackmsg() {
		return this.stackmsg;
	}

	public void setStackmsg(String stackmsg) {
		this.stackmsg = stackmsg;
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