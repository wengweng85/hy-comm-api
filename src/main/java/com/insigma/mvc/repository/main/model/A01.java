package com.insigma.mvc.repository.main.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the A01 database table.
 * 
 */
@TableName("a01_test")//指定表名
@Data
@NoArgsConstructor
public class A01 implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "a0000")
	private String a0000;

	private String a0101;

	private String a0102;

	private String a0104;
    
	private String a0107;
	
	private String a0111a;
	
	private String a0114a;
	private String a0115a;
	private String a0117;
	private String a0128;
	private String a0134;
	private String a0140;
	private String a0141;
	private Date test;
	private String test2;
}