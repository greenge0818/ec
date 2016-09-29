package com.prcsteel.ec.validator;

import com.prcsteel.ec.model.domain.ec.Cart;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Rolyer on 2016/4/22.
 */
public class CartValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Cart.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    }
}
