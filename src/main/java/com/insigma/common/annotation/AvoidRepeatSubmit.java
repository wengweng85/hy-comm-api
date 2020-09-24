package com.insigma.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AvoidRepeatSubmit {
 
	/**
     * 失效时间，即可以第二次提交间隔时长
     * @return
     */
    long expireTime() default 3 * 1000L;
 
}
