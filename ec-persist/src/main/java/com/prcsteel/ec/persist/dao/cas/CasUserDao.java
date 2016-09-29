package com.prcsteel.ec.persist.dao.cas;

import com.prcsteel.ec.model.domain.cas.CasUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CasUser record);

    int insertSelective(CasUser record);

    CasUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CasUser record);

    int updateByPrimaryKey(CasUser record);

    /**
     * 通过手机号查询CAS用户
     *
     * @param phone 手机号
     * @return CAS用户对象
     * @author peanut
     * @date 2016/05/11
     */
    CasUser selectByPhone(String phone);

    /**
     * 通过手机号修改用户密码
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return 影响行数
     * @author peanut
     * @date 2016/05/11
     */
    int updatePasswordByPhone(@Param("phone") String phone, @Param("pwd") String pwd);

    /**
     * 通过旧手机号修改为新手机号
     *
     * @param newPhone 新手机号
     * @param oldPhone 旧手机号
     * @return 影响行数
     * @author peanut
     * @date 2016/05/05
     */
    int updateMobileByMobile(@Param("newPhone") String newPhone, @Param("oldPhone") String oldPhone);

    /**
     * 更新cas库用户动态验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @author peanut
     * @date 2016/05/13
     */
    int updateCasDynamicPassword(@Param("phone") String phone, @Param("code") String code);

    /**
     * 根据手机号码和验证码检验是否可以登陆
     *
     * @Author:Green.Ge
     * @Date:2016-06-03
     */
    int SelectByMobileAndValidCode(@Param("mobile") String mobile, @Param("code") String code);

    /**
     * 锁定/解锁用户
     * @param mobile
     * @param isLocked
     * @author Tiny
     * @date 2016/07/04
     */
    int updateIsLockedByMobile(@Param("mobile") String mobile, @Param("isLocked") String isLocked);
}