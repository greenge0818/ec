package com.prcsteel.ec.validator;

import com.prcsteel.ec.model.domain.ec.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Rolyer on 2016/4/22.
 */
public class DemoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "email", "user.email.required", "密码不能为空");
    }
}
