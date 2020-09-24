package com.insigma.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.groups.Default;

/**
 * ������ʾ����hibernateУ���ע��
 * @author ���
 */
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableValidate {

    Class<?> [] groups() default { Default.class };//У�������Ϣ
}

