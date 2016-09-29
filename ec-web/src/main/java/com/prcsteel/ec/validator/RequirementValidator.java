package com.prcsteel.ec.validator;

import com.prcsteel.ec.model.domain.ec.Requirement;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @ClassName: RequirementValidator
 * @Description: 采购需求验证
 * @Author Tiny
 * @Date 2016年4月29日
 */
public class RequirementValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Requirement.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    }
}
