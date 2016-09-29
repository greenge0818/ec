package com.prcsteel.ec.service.cas.impl;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.model.domain.cas.CasUser;
import com.prcsteel.ec.persist.dao.cas.CasUserDao;
import com.prcsteel.ec.service.cas.CasUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Rolyer on 2016/5/5.
 */
@Service("casUserService")
public class CasUserServiceImpl implements CasUserService {
    @Resource
    private CasUserDao casUserDao;

    @Override
    public void add(CasUser user) {
        casUserDao.insert(user);
    }

    @Override
    public int SelectByMobileAndValidCode(String mobile, String code) {
        if(StringUtils.isBlank(mobile)){
            throw new BusinessException(MessageTemplate.USER_MOBILE_EMPTY.getCode(),MessageTemplate.USER_MOBILE_EMPTY.getMsg());
        }
        if(!StringUtil.isPhoneNumberCheck(mobile)){
            throw new BusinessException(MessageTemplate.PHONE_ERROR.getCode(),MessageTemplate.PHONE_ERROR.getMsg());
        }
        if(StringUtils.isBlank(code)){
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(),MessageTemplate.USER_CODE_EMPTY.getMsg());
        }
        return casUserDao.SelectByMobileAndValidCode(mobile,code);
    }
}
