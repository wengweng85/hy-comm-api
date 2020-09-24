package com.insigma.dto.validator.annonation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

import com.insigma.dto.validator.IdcardValidator;

@Documented
@Constraint(validatedBy = IdcardValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@Pattern(regexp = "")
public @interface Idcard {

    String message() default "身份证号不符合格式";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @OverridesAttribute(constraint = Pattern.class, name = "regexp") String regexp() default ".*";

    @OverridesAttribute(constraint = Pattern.class, name = "flags") Pattern.Flag[] flags() default { };

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Idcard[] value();
    }
}
