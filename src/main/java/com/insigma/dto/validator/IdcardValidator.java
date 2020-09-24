package com.insigma.dto.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.insigma.dto.validator.annonation.Idcard;

public class IdcardValidator implements ConstraintValidator<Idcard, String> {

	
    private static final String REG = "^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])||(1[0-2]))((0[1-9])||(1\\d)||(2\\\\d)||(3[0-1]))\\d{3}([0-9]||X)$";
    private Pattern pattern = Pattern.compile(REG);

    @Override
    public void initialize(Idcard idcard) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (null == s || s.length() == 0) {
            return true;
        }
        boolean result= pattern.matcher(s).matches();
        System.out.println("result->"+result);
        return result;
    }
}
