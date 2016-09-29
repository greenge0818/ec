package com.prcsteel.ec.service.impl;

import com.prcsteel.ec.persist.dao.ec.SecurityUserDao;
import com.prcsteel.ec.model.domain.ec.SecurityUser;
import com.prcsteel.ec.service.SecurityUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by rolyer on 16-4-19.
 */
@Service("securityUserService")
public class SecurityUserServiceImpl implements SecurityUserService {
    @Resource
    private SecurityUserDao securityUserDao;

    public SecurityUser queryByUserName(String username) {
        return securityUserDao.queryByUserName(username);
    }
}
