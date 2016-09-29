package com.prcsteel.ec.persist.dao.ec;


import com.prcsteel.ec.model.domain.ec.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    User queryActiveUserById(Integer id);

    User queryUserById(Integer id);

    List<User> query();

    /**
     * 添加超市用户
     *
     * @param user 用户对象
     * @return 影响行数
     * @author peanut
     * @date 2016/04/28
     * @see User
     */
    int insert(User user);

    /**
     * 通过手机号查询超市用户
     *
     * @param phone 手机号
     * @return 用户对象
     * @author peanut
     * @date 2016/04/28
     */
    User selectByPhone(String phone);


    /**
     * 通过Guid查询超市用户
     *
     * @param guid
     * @return 用户对象
     * @author rabbit
     * @date 2016-8-17
     */
    User selectByGuid(String guid);


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
     * 通过旧手机号修改用户姓名
     *
     * @param name     用户姓名
     * @param oldPhone 旧手机号
     * @return 影响行数
     * @author Tiny
     * @date 2016/06/01
     */
    int updateNameByMobile(@Param("name") String name, @Param("oldPhone") String oldPhone);

    /**
     * 通过旧手机号更新手机和姓名
     *
     * @param newPhone 新手机号
     * @param oldPhone 旧手机号
     * @param name     名字
     * @return 影响行数
     * @author Tiny
     * @date 2016/06/01
     */
    int updateMobileAndNameByMobile(@Param("newPhone") String newPhone, @Param("oldPhone") String oldPhone, @Param("name") String name);

    /**
     * 查询新手机号是否存在（排除自身外）
     *
     * @param user
     * @author Tiny
     * @date 2016/06/13
     */
    User selectNewMobile(User user);
}
