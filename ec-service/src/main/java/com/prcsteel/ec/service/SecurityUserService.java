package com.prcsteel.ec.service;

import com.prcsteel.ec.model.domain.ec.SecurityUser;

/**
 * Created by rolyer on 16-4-19.
 */
public interface SecurityUserService {
    SecurityUser queryByUserName(String username);
}
