package com.insigma.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��ֹ�ظ��ύ��ע��
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AvoidRepeatSubmit {
 
	/**
     * ʧЧʱ�䣬�����Եڶ����ύ���ʱ��
     * @return
     */
    long expireTime() default 3 * 1000L;
 
}
