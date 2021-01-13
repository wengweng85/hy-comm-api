package com.insigma.mvc.repository.main.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * SmtUser
 */
@TableName("SMT_USER")//指定表名
@Data
@NoArgsConstructor
public class SmtUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "userid")
	private String userid;

	private String checkip;

	private Date createdate;

	private String dept;

	private String description;

	private String empid;

	private String iplist;

	private String isleader;

	private String loginname;

	private String macaddr;

	private String otherinfo;

	private String owner;

	private String passwd;

	private String rate;

	private String regionid;

	private String useful;

	private String username;

	private String usertype;

	
}