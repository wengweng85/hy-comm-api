package com.insigma.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.insigma.dto.validator.annonation.Idcard;
import com.insigma.dto.validgroup.Add;
import com.insigma.dto.validgroup.Update;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the A01 database table.
 * 
 */

/**
 *  @Null	限制只能为null
	@NotNull	限制必须不为null
	@AssertFalse	限制必须为false
	@AssertTrue	限制必须为true
	@DecimalMax(value)	限制必须为一个不大于指定值的数字
	@DecimalMin(value)	限制必须为一个不小于指定值的数字
	@Digits(integer,fraction)	限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction
	@Future	限制必须是一个将来的日期
	@Max(value)	限制必须为一个不大于指定值的数字
	@Min(value)	限制必须为一个不小于指定值的数字
	@Past	限制必须是一个过去的日期
	@Pattern(value)	限制必须符合指定的正则表达式
	  1 匹配首尾空格的正则表达式：(^\s*)|(\s*$)
	  2 整数或者小数：^[0-9]+\.{0,1}[0-9]{0,2}$
	  3 只能输入数字："^[0-9]*$"。
	  4 只能输入n位的数字："^\d{n}$"。
	  5 只能输入至少n位的数字："^\d{n,}$"。
	  6 只能输入m~n位的数字：。"^\d{m,n}$"
	  7 只能输入零和非零开头的数字："^(0|[1-9][0-9]*)$"。
	  8 只能输入有两位小数的正实数："^[0-9]+(.[0-9]{2})?$"。
	  9 只能输入有1~3位小数的正实数："^[0-9]+(.[0-9]{1,3})?$"。
	 10 只能输入非零的正整数："^\+?[1-9][0-9]*$"。
	 11 只能输入非零的负整数："^\-[1-9][]0-9"*$。
	 12 只能输入长度为3的字符："^.{3}$"。
	 13 只能输入由26个英文字母组成的字符串："^[A-Za-z]+$"。
	 14 只能输入由26个大写英文字母组成的字符串："^[A-Z]+$"。
	 15 只能输入由26个小写英文字母组成的字符串："^[a-z]+$"。
	 16 只能输入由数字和26个英文字母组成的字符串："^[A-Za-z0-9]+$"。
	 17 只能输入由数字、26个英文字母或者下划线组成的字符串："^\w+$"。
	 18 验证用户密码："^[a-zA-Z]\w{5,17}$"正确格式为：以字母开头，长度在6~18之间，只能包含字符、数字和下划线。
	 19 验证是否含有^%&',;=?$\"等字符："[^%&',;=?$\x22]+"。
	 20 只能输入汉字："^[\u4e00-\u9fa5]{0,}$"
	 21 验证Email地址："^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$"。
	 22 验证InternetURL："^http://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$"。
	 23 验证电话号码："^(\(\d{3,4}-)|\d{3.4}-)?\d{7,8}$"正确格式为："XXX-XXXXXXX"、"XXXX-XXXXXXXX"、"XXX-XXXXXXX"、"XXX-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX"。
	 24 验证身份证号（15位或18位数字）："^\d{15}|\d{18}$"。
	 25 验证一年的12个月："^(0?[1-9]|1[0-2])$"正确格式为："01"～"09"和"1"～"12"。
	 26 验证一个月的31天："^((0?[1-9])|((1|2)[0-9])|30|31)$"正确格式为；"01"～"09"和"1"～"31"。
	 27 匹配中文字符的正则表达式： [\u4e00-\u9fa5]
	 28 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
	 29 应用：计算字符串的长度（一个双字节字符长度计2，ASCII字符计1）
	 30 String.prototype.len=function(){return this.replace(/[^\x00-\xff]/g,"aa").length;}
	 31 匹配空行的正则表达式：\n[\s| ]*\r
	 32 匹配html标签的正则表达式：<(.*)>(.*)<\/(.*)>|<(.*)\/>
	
	@Size(max,min)	限制字符长度必须在min到max之间
	@Past	验证注解的元素值（日期类型）比当前时间早
	@NotEmpty	验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0）
	@NotBlank	验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格
	@Email	验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式
 * @author wengsh
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class A01DTO  extends BaseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "人员统一标识符不能为空",groups = Update.class)
	private String a0000;

	@NotBlank(message = "姓名不能为空",groups = Add.class)
	private String a0101;

	@NotBlank(message = "姓名拼音缩写不能为空",groups = Add.class)
	private String a0102;

	@NotBlank(message = "性别代码不能为空",groups = Add.class)
	@Size(min = 1,max = 1,message = "性别代码长度必须为1",groups = Add.class)
	private String a0104;
    
	private String a0104a;

	private String a0107;
	
	private String a0111a;
	
	private String a0114a;
	private String a0115a;
	private String a0117;
	private String a0128;
	private String a0134;
	private String a0140;
	private String a0141;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date test;
	@Idcard(message="test2必须是身份证格式",groups =  Add.class)
	private String test2;

}