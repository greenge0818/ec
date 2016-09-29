package com.prcsteel.ec.service.cas;

import com.prcsteel.ec.model.domain.cas.CasUser;

/**
 * Created by Rolyer on 2016/5/5.
 */
public interface CasUserService {
    void add(CasUser user);

    /**
     * 根据手机号和动态验证码登陆（判断验证码是否有效)
     * @Author:Green.Ge
     * @param mobile
     * @param code
     */
    int SelectByMobileAndValidCode(String mobile,String code);
}
