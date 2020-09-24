package com.insigma.dto.validator.annonation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.insigma.dto.validator.DateValueValidator;

/**
 * ���ڸ�ʽ��֤,������{@code format}�������Ч�����ڸ�ʽ
 * @author Canaan
 * @date 2017��3��8��
 */
@Documented
@Constraint(validatedBy = { DateValueValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface DateValue {
	
	String message() default "���ڸ�ʽ����ȷ";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * ��Ч�����ڸ�ʽ
	 */
	String[] format() default {};  
	
	/**
	 * Defines several {@link DateValue} annotations on the same element.
	 *
	 * @see com.yhbc.validation.DateValue.DateValue
	 */
	@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		DateValue[] value();
	}
}
