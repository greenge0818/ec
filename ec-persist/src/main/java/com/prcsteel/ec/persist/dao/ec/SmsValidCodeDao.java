package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.domain.ec.SmsValidCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author peanut
 * @description 短信验证码Dao
 * @date 2016/4/28 15:46
 */
@Repository
public interface SmsValidCodeDao {

    int insert(SmsValidCode smsValidCode);

    /**
     * 根据手机号和模块查询出不可以再次发送验证码的记录
     *
     * @param mobile 手机号
     * @param module 模块
     * @return
     * @author peanut
     * @date 2016/04/28
     */
    SmsValidCode selectUnresendByMobileAndModule(@Param("mobile") String mobile, @Param("module") String module);

    /**
     * 根据手机号，模块查询出最新的一条验证码数据
     *
     * @param mobile 手机号
     * @param module 模块
     * @return
     * @author peanut
     * @date 2016/04/28
     */
    SmsValidCode selectByMobileAndModule(@Param("mobile") String mobile, @Param("module") String module);

    /**
     * 根据手机号、验证码、模块使得验证记录失效
     *
     * @param mobile 手机号
     * @param module 模块
     * @param code   验证码
     * @return
     * @author peanut
     * @date 2016/05/05
     */
    int invalidCodeByMobileAndCodeAndModule(@Param("mobile") String mobile, @Param("module") String module, @Param("code") String code);

    /**
     * 根据手机号查询出最新的三条注册的验证码数据
     *
     * @param mobile 手机号
     * @return
     * @author Tiny
     * @date 2016/08/26
     */
    List<SmsValidCode> selectRegisterRecordByMobile(@Param("mobile") String mobile, @Param("times") Integer times);
}
