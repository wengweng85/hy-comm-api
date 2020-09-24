package com.insigma.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * �û���־ע��
 * ��ʹ�÷����ϼ��ϴ�ע���ͳһ��aop�����ز���¼��־
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLog {
	String value() default "";
}
