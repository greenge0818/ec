package com.prcsteel.ec.service;


import com.prcsteel.ec.core.enums.OpType;
import com.prcsteel.ec.core.enums.SMSTemplate;
import com.prcsteel.ec.core.enums.UserSource;
import com.prcsteel.ec.model.domain.ec.AppUser;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.model.dto.ChangeContactStatusDto;

import java.util.List;

public interface UserService {
    void add(User user);

    void delete(User id);

    void update(User user);

    User queryActiveUserById(Integer id);

    List<User> query();

    /**
     * 用户注册(密码必填)
     *
     * @param user 用户对象
     * @return AddCbmsContact对象
     * @author peanut
     * @date 2016/04/28
     * @see User
     */
    void userRegisterPwdRequired(User user);

    /**
     * 发送手机号验证码
     *
     * @param phone  手机号码
     * @param module 发送手机号模块
     * @return
     * @author peanut
     * @date 2016/04/28
     */
    boolean sendPhoneCode(String phone, String module);

    /**
     * 发送手机号验证码
     *
     * @param phone 手机号码（模块不确定）
     * @return
     * @author Tiny
     * @date 2016/05/03
     */
    boolean sendPhoneCodeWithoutModel(String phone);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return
     */
    boolean isPhoneExists(String phone);

    /**
     * 用户登陆
     *
     * @param phone    手机号
     * @param code     根据type类型，可能是密码和手机验证码
     * @param type     登陆类型：手机密码登陆 或手机验证码登陆
     * @param cookieId cookieId用于购物车合并
     * @return
     * @author peanut
     * @date 2016/04/29
     * @see com.prcsteel.ec.core.enums.LoginType
     boolean userLogin(String phone, String code, String type, String cookieId);
    */

    /**
     * @Auther:Green.Ge
     * @Description:APP用户登陆
     * @param deviceNo 设备号
     * @param deviceType 设备类型
     * @param mobile 手机
     * @param code 动态验证码
     */
    AppUser APPUserLogin(String deviceNo,String deviceType, String mobile, String code);

    /**
     * @param authToken
     * @Auther:Green.Ge
     * @Description:APP用户登陆
     */
    void APPUserLogout(String authToken);

    /**
     * @Auther:Green.Ge
     * @Description:根据token获取User对象
     * @param authToken token
     */
    User checkUser(String authToken);

    /**
     * 检查验证码可用性
     *
     * @param phone  手机号
     * @param code   验证码
     * @param module 发送验证码场景(模块)
     * @return
     * @author peanut
     * @date 2016/05/04
     */
    boolean isCodeAvailable(String phone, String code, String module);

    /**
     * 确定重置密码
     *
     * @param phone  手机号
     * @param pwd    密码
     * @param code   验证码
     * @param module 模块
     * @return
     * @author peanut
     * @date 2016/05/04
     */
    void resetPassword(String phone, String pwd, String code, String module);

    /**
     * 确定修改手机号
     *
     * @param newPhone 新手机号
     * @param newCode  新验证码
     * @return
     * @author peanut
     * @date 2016/05/05
     */
    void resetPhone(String newPhone, String newCode);


    /**
     * 检验被操作的手机号是否与当前登陆用户的手机号一致
     *
     * @param oprtPhone 操作的手机号
     * @return 检查后的手机号
     * @author peanut
     * @date 2016/05/06
     */
    String checkPhoneMatch(String oprtPhone);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone 手机号
     * @return 用户实体
     * @author peanut
     * @date 2016/05/09
     */
    User getUserInfo(String phone);

    /**
     * 获取用户公司名称---cbms系统提供
     *
     * @param phone 手机号
     * @return 公司名称
     * @author peanut
     * @date 2016/05/09
     */
    String getUserCompany(String phone);

    /**
     * 取得用户上次登陆时间
     *
     * @param phone 手机号
     * @return 上次登陆时间
     * @author peanut
     * @date 2016/05/09
     */
    String getUserLastLoginTime(String phone);

    /**
     * 获取用户积分---积分系统接口提供
     *
     * @param mobile 手机号
     * @return 用户积分
     * @author peanut
     * @date 2016/05/09
     */
    String getUserScore(String mobile);

    /**
     * 注册模板: 参数user不为null时，进行注册用户校验及注册（密码可不填）
     *
     * @param user 超市用对象
     * @param
     * @author peanut
     * @date 2016/05/09
     */
    void registrationModule(User user);

    /**
     * 检查重置手机号验证码正确性_旧手机号
     *
     * @param code 验证码
     * @return
     * @author peanut
     * @date 2016/05/12
     */
    void checkCodeForResetPhone(String code);

    /**
     * 检查重置密码验证码正确性__未登陆
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @author peanut
     * @date 2016/05/12
     */
    void checkCodeForResetPassword(String phone, String code);

    /**
     * 检查重置密码验证码正确性__登陆后
     *
     * @param code 验证码
     * @return
     * @author peanut
     * @date 2016/05/12
     */
    void checkCodeForResetPassword(String code);

    /**
     * 用户注册(密码不需要填)
     *
     * @param user 用户对象
     * @author Tiny
     * @date 2016/05/12
     * @see User
     */
    String userRegisterPwdNotRequired(User user);

    /**
     * 更新cas库用户动态验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @author peanut
     * @date 2016/05/13
     */
    void updateCasDynamicPassword(String phone, String code);

    /**
     * 用户登陆后的处理
     *
     * @param cookieId
     * @return
     * @author peanut
     * @date 2016/05/13
     */
    int afterLogin(String cookieId);

    /**
     * 新增CBMS联系人信息到超市
     *
     * @param mobile
     * @param name
     * @author Tiny
     * @date 2016/05/31
     */
    void addCBMSUser(String mobile, String name);

    /**
     * 更新CBMS联系人信息到超市
     *
     * @param user
     * @author Tiny
     * @date 2016/05/31
     */
    void updateCBMSUser(User user);

    /**
     * 新增外部系统联系人信息到超市
     *
     * @param mobile
     * @param name
     * @param source
     * @param template
     * @param opType
     * @author Tiny
     * @date 2016/06/06
     */
    User addRemoteUser(String mobile, String name, UserSource source, SMSTemplate template, OpType opType);

    /**
     * CBMS禁用/启用联系人
     * @param changeContactStatusDto
     * @author Tiny
     * @date 2016/07/01
     */
    void changeContactStatus(ChangeContactStatusDto changeContactStatusDto);

    /**
     * cas用户是否可用
     *
     * @param phone
     * @author peanut
     * @date 2016/07/14
     */
    void checkPhoneValid(String phone);
}
