package com.prcsteel.ec.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.prcsteel.ec.core.enums.*;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.model.RestResult;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.DateUtil;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.model.domain.cas.CasUser;
import com.prcsteel.ec.model.domain.ec.*;
import com.prcsteel.ec.model.dto.ChangeContactStatusDto;
import com.prcsteel.ec.model.model.AddCbmsContact;
import com.prcsteel.ec.model.model.PointMemberInfo;
import com.prcsteel.ec.model.model.UpdateCbmsContact;
import com.prcsteel.ec.persist.dao.cas.CasUserDao;
import com.prcsteel.ec.persist.dao.ec.SearchHistoryDao;
import com.prcsteel.ec.persist.dao.ec.SmsValidCodeDao;
import com.prcsteel.ec.persist.dao.ec.UserDao;
import com.prcsteel.ec.service.*;
import com.prcsteel.ec.service.api.RestCbmsService;
import com.prcsteel.ec.service.api.RestScoreService;
import com.prcsteel.ec.service.cas.CasUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private CasUserDao casUserDao;

    @Resource
    private GlobalIdService globalIdService;

    @Resource
    private GenericDaoService genericDaoService;

    @Resource
    private CommonService commonService;

    @Resource
    private SmsValidCodeDao smsValidCodeDao;

    @Resource
    private CartService cartService;

    @Resource
    private SystemOperationLogService systemOperationLogService;

    @Resource
    private SearchHistoryDao searchHistoryDao;

    @Resource
    private CacheService cacheService;

    @Resource
    private CasUserService casUserService;

    @Resource
    private RestScoreService restScoreService;

    @Resource
    private RestCbmsService restCbmsService;

    @Resource
    private ActiveMQService activeMQService;

    @Resource
    private AppPushService appPushService;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value("${amq.addCBMSContact}")
    private String addDse;

    @Value("${amq.updateCBMSContact}")
    private String updateDse;

    @Value("${amq.addMarketUserId}")
    private String userIdDse;

    @Override
    public void add(User user) {
        user.preInsert(globalIdService.getId());
        genericDaoService.insert(user);
    }

    @Override
    public void delete(User user) {
        genericDaoService.deleteByKey(user);
    }

    @Override
    public void update(User user) {
        genericDaoService.updateByKey(user);
    }

    @Override
    public User queryActiveUserById(Integer id) {
        return userDao.queryActiveUserById(id);
    }

    @Override
    public List<User> query() {
        return userDao.query();
    }

    /**
     * 用户注册
     *
     * @param user 用户对象
     * @return AddCbmsContact对象
     * @author peanut
     * @date 2016/04/28
     * @see User
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userRegisterPwdRequired(User user) {
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_EMPTY.getCode(), MessageTemplate.USER_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(user.getPassword())) {
            throw new BusinessException(MessageTemplate.USER_PWD_EMPTY.getCode(), MessageTemplate.USER_PWD_EMPTY.getMsg());
        }

        registrationModule(user);
        //新增的超市用户推送到cbms
        AddCbmsContact addCbmsContact = new AddCbmsContact(user.getId(), user.getMobile(), null, null, user.getSource());
        MqLog mqLog = new MqLog(MqLogModual.ADD_CBMS_CONTACT.getModule(), RemoteDataSource.CBMS.getCode(), new Gson().toJson(addCbmsContact), "Y", null);
        commonService.sendMqAndInsertLog(addDse, addCbmsContact, mqLog);

    }

    /**
     * 根据手机号，模块查询出最新的一条验证码数据
     *
     * @param mobile 手机号
     * @param module 模块
     * @return
     */
    private SmsValidCode selectByMobileAndModule(String mobile, String module) {
        return smsValidCodeDao.selectByMobileAndModule(mobile, module);
    }

    /**
     * 发送手机号验证码
     *
     * @param phone  手机号码
     * @param module 发送手机号模块
     * @return
     * @author peanut
     * @date 2016/04/28
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendPhoneCode(String phone, String module) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(module)) {
            throw new BusinessException(MessageTemplate.SMS_MODULE_EMPTY.getCode(), MessageTemplate.SMS_MODULE_EMPTY.getMsg());
        }
        if (!StringUtil.isPhoneNumberCheck(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_ERROR.getCode(), MessageTemplate.PHONE_ERROR.getMsg());
        }
        return commonService.doSendSMS(phone, module);
    }

    /**
     * 发送手机号验证码（模块不确定）
     *
     * @param phone 手机号码
     * @return
     * @author Tiny
     * @date 2016/05/03
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendPhoneCodeWithoutModel(String phone) {
        try {
            checkPhoneValid(phone);
            return sendPhoneCode(phone, SMSTemplate.LOGIN.toString());
        } catch (BusinessException e) {
            if (MessageTemplate.PHONE_UNREGISTER.getCode().equals(e.getCode())) {
                return sendPhoneCode(phone, SMSTemplate.REGISTER.toString());
            } else {
                throw e;
            }
        }
    }

    /**
     * 检查CAS手机号是否存在
     *
     * @param phone 手机号
     * @return
     */

    @Override
    public boolean isPhoneExists(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (!StringUtil.isPhoneNumberCheck(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_ERROR.getCode(), MessageTemplate.PHONE_ERROR.getMsg());
        }
        return casUserDao.selectByPhone(phone) == null ? false : true;
    }

    /**
     * @Author: Tiny
     * @Description: 注册模板: 参数user不为null时，进行注册用户校验及注册（密码可不填）
     * @Date: 2016年4月29日
     */
    @Override
    @Transactional()
    public void registrationModule(User user) {
        if (StringUtils.isBlank(user.getMobile())) {
            throw new BusinessException(MessageTemplate.USER_MOBILE_EMPTY.getCode(), MessageTemplate.USER_MOBILE_EMPTY.getMsg());
        }
        if (!StringUtil.isPhoneNumberCheck(user.getMobile())) {
            throw new BusinessException(MessageTemplate.PHONE_ERROR.getCode(), MessageTemplate.PHONE_ERROR.getMsg());
        }
        if (StringUtils.isBlank(user.getCode())) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }

        CasUser cas = casUserDao.selectByPhone(user.getMobile());
        //用户已存在
        if (cas != null && ("N".equals(cas.getIsDeleted()) || "N".equals(cas.getIsLocked()) || "Y".equals(cas.getIsActivated()))) {
            throw new BusinessException(MessageTemplate.USER_EXISTS.getCode(), MessageTemplate.USER_EXISTS.getMsg());
        }
        //用户已锁定
        if (cas != null && (!"N".equals(cas.getIsDeleted()) || !"N".equals(cas.getIsLocked()) || !"Y".equals(cas.getIsActivated()))) {
            throw new BusinessException(MessageTemplate.USER_ACCOUNT_LOCKED.getCode(), MessageTemplate.USER_ACCOUNT_LOCKED.getMsg());
        }

        //检查验证码可用性
        if (isCodeAvailable(user.getMobile(), user.getCode(), SMSTemplate.REGISTER.getModule())) {
            if (userDao.selectByPhone(user.getMobile()) == null) {
                //设置基本属性
                user.setStatus("1");
                if (StringUtils.isBlank(user.getName())) {
                    user.setName(user.getMobile());
                }
                if (StringUtils.isBlank(user.getSource())) {
                    user.setSource(UserSource.MARKET.toString());
                }
                user.preInsert(globalIdService.getId());
                user.setCreatedBy(Constant.SYS_USER);
                user.setLastUpdatedBy(Constant.SYS_USER);
                if (userDao.insert(user) != 1) {
                    throw new BusinessException(MessageTemplate.USER_REGISTER_ERROR.getCode(), MessageTemplate.USER_REGISTER_ERROR.getMsg());
                }
            }
            //cas用户添加
            CasUser casUser = new CasUser();
            casUser.setAccount(user.getMobile());
            casUser.setPassword(user.getPassword());
            casUser.setIsActivated("Y");
            if (casUserDao.insertSelective(casUser) != 1) {
                throw new BusinessException(MessageTemplate.USER_REGISTER_ERROR.getCode(), MessageTemplate.USER_REGISTER_ERROR.getMsg());
            }
            updateCasDynamicPassword(user.getMobile(), user.getCode());
            //验证码失效!
            smsValidCodeDao.invalidCodeByMobileAndCodeAndModule(user.getMobile(), SMSTemplate.REGISTER.getModule(), user.getCode());
        }
    }

    /**
     * 检查重置手机号验证码正确性_旧手机号
     *
     * @param code 验证码
     * @return
     * @author peanut
     * @date 2016/05/12
     */
    @Override
    public void checkCodeForResetPhone(String code) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }
        //判断登陆用户
        String userPhone = UserUtils.getPrincipal();
        if (StringUtils.isNotBlank(userPhone)) {
            //检测code可用性
            isCodeAvailable(userPhone, code, SMSTemplate.RESET_PHONE_OLD.getModule());
        } else {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
    }

    /**
     * 检查忘记密码验证码正确性__未登陆
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @author peanut
     * @date 2016/05/12
     */
    @Override
    public void checkCodeForResetPassword(String phone, String code) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }
        phone = checkPhoneMatch(phone);
        isCodeAvailable(phone, code, SMSTemplate.FORGOT_PASSWORD.getModule());
    }

    /**
     * 检查重置密码验证码正确性__登陆后
     *
     * @param code 验证码
     * @return
     * @author peanut
     * @date 2016/05/12
     */
    @Override
    public void checkCodeForResetPassword(String code) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }
        String userPhone = UserUtils.getPrincipal();
        if (StringUtils.isNotBlank(userPhone)) {
            isCodeAvailable(userPhone, code, SMSTemplate.RESET_PASSWORD.getModule());
        } else {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
    }

    /**
     * 检查验证码可用性
     *
     * @param phone  手机号
     * @param code   验证码
     * @param module 发送验证码场景(模块)
     * @return
     */
    @Override
    public boolean isCodeAvailable(String phone, String code, String module) {

        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(module)) {
            throw new BusinessException(MessageTemplate.SMS_MODULE_EMPTY.getCode(), MessageTemplate.SMS_MODULE_EMPTY.getMsg());
        }

        //根据手机号，模块查询出验证码数据
        SmsValidCode svc = selectByMobileAndModule(phone, module);

        if (svc == null) {
            throw new BusinessException(MessageTemplate.SMS_CODE_UNAVAILABLE.getCode(), MessageTemplate.SMS_CODE_UNAVAILABLE.getMsg());
        }
        //验证码校验
        if (!code.equals(svc.getValidCode())) {
            throw new BusinessException(MessageTemplate.SMS_CHECK_ERROR.getCode(), MessageTemplate.SMS_CHECK_ERROR.getMsg());
        }
        //检查验证码是否失效
        if (svc.getExpireTime().getTime() < new Date().getTime()) {
            throw new BusinessException(MessageTemplate.SMS_SEND_TIMEOUT.getCode(), MessageTemplate.SMS_SEND_TIMEOUT.getMsg());
        }
        return true;
    }

    /**
     * 确定重置（忘记）密码
     *
     * @param phone  手机号
     * @param pwd    密码
     * @param code   验证码
     * @param module 模块：重置或忘记密码
     * @return
     * @author peanut
     * @date 2016/05/04
     */
    @Override
    public void resetPassword(String phone, String pwd, String code, String module) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(pwd)) {
            throw new BusinessException(MessageTemplate.USER_PWD_EMPTY.getCode(), MessageTemplate.USER_PWD_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }

        //检验重置密码的手机号是否与当前登陆用户的手机号一致
        phone = checkPhoneMatch(phone);

        if (isCodeAvailable(phone, code, module)) {
            //更新
            if (casUserDao.updatePasswordByPhone(phone, pwd) != 1) {
                throw new BusinessException(MessageTemplate.USER_PWD_RESET_ERROR.getCode(), MessageTemplate.USER_PWD_RESET_ERROR.getMsg());
            }
            //code失效
            smsValidCodeDao.invalidCodeByMobileAndCodeAndModule(phone, module, code);

        } else {
            throw new BusinessException(MessageTemplate.USER_PWD_RESET_EXCEPTION.getCode(), MessageTemplate.USER_PWD_RESET_EXCEPTION.getMsg());
        }
    }

    /**
     * 检验被操作的手机号是否与当前登陆用户的手机号一致
     *
     * @param oprtPhone 操作的手机号
     * @author peanut
     * @date 2016/05/06
     */
    @Override
    public String checkPhoneMatch(String oprtPhone) {
        if (StringUtils.isBlank(oprtPhone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        String userPhone = UserUtils.getPrincipal();
        if (StringUtils.isNotBlank(userPhone)) {
            if (StringUtil.isPhoneNumberCheck(oprtPhone)) {
                if (!userPhone.equals(oprtPhone)) {
                    throw new BusinessException(MessageTemplate.USER_PHONE_NOT_MATCH.getCode(), MessageTemplate.USER_PHONE_NOT_MATCH.getMsg());
                } else {
                    oprtPhone = userPhone;
                }
            } else {
                throw new BusinessException(MessageTemplate.PHONE_ERROR.getCode(), MessageTemplate.PHONE_ERROR.getMsg());
            }
        }
        return oprtPhone;
    }

    /**
     * 根据手机号获取超市用户信息
     *
     * @param phone 手机号
     * @return 用户实体
     */
    @Override
    public User getUserInfo(String phone) {

        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }

        return userDao.selectByPhone(phone);
    }

    /**
     * 获取用户公司名称 ---cbms系统提供
     *
     * @param phone 手机号
     * @return 公司名称
     * @author peanut
     * @date 2016/05/09
     */
    @Override
    public String getUserCompany(String phone) {

        if (StringUtils.isBlank(phone)) return null;

        User user = userDao.selectByPhone(phone);
        String result = null;
        if (user != null) {
            try {
                result = restCbmsService.getCompanyNameByEcUserId(user.getId());
                if (StringUtils.isNotBlank(result)) {
                    RestResult restResult = new Gson().fromJson(result, RestResult.class);
                    return restResult == null ? null : restResult.getData() != null ? String.valueOf(restResult.getData()) : null;
                }
            } catch (JsonSyntaxException e) {
                logger.error("ESB 返回内容：{} 转换为对象失败!", result);
            } catch (Exception e1) {
                logger.error("获取用户:{}公司名称失败!", phone);
            }
        } else {
            logger.error("当前手机号:{} 找不到用户!", phone);
        }
        return null;
    }

    /**
     * 取得用户上次登陆时间
     *
     * @param phone 手机号
     * @return 上次登陆时间
     * @author peanut
     * @date 2016/05/09
     */
    @Override
    public String getUserLastLoginTime(String phone) {

        if (StringUtils.isBlank(phone)) return null;

        User user = userDao.selectByPhone(phone);
        if (user == null) return null;

        //登陆日志，时间倒序
        List<SystemOperationLog> logList = systemOperationLogService.getLogByOprtGuidAndOprtKey(user.getGuid(), OpType.USER_LOGIN.toString());

        if (logList == null || logList.isEmpty()) return null;

        Date loginTime;
        //初次登陆
        if (logList.size() == 1) {
            //上次登陆日志记录的创建时间
            loginTime = logList.get(0).getCreated();
        } else {
            //多次登陆，取第二条记录
            loginTime = logList.get(1).getCreated();
        }

        return new SimpleDateFormat("yyyy年MM月dd日 HH : mm").format(loginTime);
    }

    /**
     * 获取用户积分---积分系统接口提供
     *
     * @param mobile 手机号
     * @return 用户积分
     * @author peanut
     * @date 2016/05/09
     */
    @Override
    public String getUserScore(String mobile) {

        if (StringUtils.isBlank(mobile)) return null;
        String result = null;
        /***
         * ESB 调用积分接口
         */
        try {
            HashMap<String, String> params = new HashMap<>();
            String timestamp = DateUtil.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
            params.put("timestamp", timestamp);
            params.put("code", mobile);

            //生成token
            String token = StringUtil.getSignature(params, Constant.SECRET);
            result = restScoreService.getScoreByMobile(token, timestamp, mobile);
            RestResult restResult;
            if (StringUtils.isNotBlank(result)) {
                restResult = new Gson().fromJson(result, RestResult.class);
                if (restResult.getData() != null) {
                    PointMemberInfo pointMemberInfo = new Gson().fromJson(String.valueOf(restResult.getData()), PointMemberInfo.class);
                    return pointMemberInfo != null ? String.valueOf(pointMemberInfo.getPoint()) : null;
                } else {
                    logger.error("用户:{} 无积分数据!", mobile);
                }
            } else {
                logger.error("ESB 返回数据为空!");
            }
        } catch (IOException e1) {
            logger.error("获取积分生成token失败!");
        } catch (JsonSyntaxException e2) {
            logger.error("rest json 数据内容:{} 转换失败!", result);
        } catch (Exception e3) {
            logger.error("ESB调用积分系统失败!");
        }
        return null;
    }

    /**
     * 确定修改手机号
     *
     * @param newPhone 新手机号
     * @param newCode  新验证码
     * @return
     * @author peanut
     * @date 2016/05/05
     */
    @Override
    @Transactional
    public void resetPhone(String newPhone, String newCode) {
        if (StringUtils.isBlank(newPhone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(newCode)) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        String oldPhone = user.getMobile();
        //新手机号已经注册
        if (isPhoneExists(newPhone)) {
            throw new BusinessException(MessageTemplate.PHONE_EXISTS.getCode(), MessageTemplate.PHONE_EXISTS.getMsg());
        }
        if (isCodeAvailable(newPhone, newCode, SMSTemplate.RESET_PHONE_NEW.getModule())) {
            //更新手机号
            //cas更新
            int casRes = casUserDao.updateMobileByMobile(newPhone, oldPhone);
            //超市更新
            int mktRes = userDao.updateMobileByMobile(newPhone, oldPhone);
            if (casRes != 1 && mktRes != 1) {
                throw new BusinessException(MessageTemplate.PHONE_RESET_ERROR.getCode(), MessageTemplate.PHONE_RESET_ERROR.getMsg());
            }
        }
        //验证码失效
        smsValidCodeDao.invalidCodeByMobileAndCodeAndModule(newPhone, SMSTemplate.RESET_PHONE_NEW.getModule(), newCode);
        smsValidCodeDao.invalidCodeByMobileAndCodeAndModule(oldPhone, SMSTemplate.RESET_PHONE_OLD.getModule(), null);

        //超市更新用户手机同步到CBMS
        UpdateCbmsContact updateCbmsContact = new UpdateCbmsContact(user.getId(), newPhone);
        MqLog mqLog = new MqLog(MqLogModual.UPDATE_CBMS_CONTACT.getModule(), RemoteDataSource.CBMS.getCode(), new Gson().toJson(updateCbmsContact), "Y", null);
        commonService.sendMqAndInsertLog(updateDse, updateCbmsContact, mqLog);
    }

    /**
     * 用户注册(密码不需要填)
     *
     * @param user 用户对象
     * @author Tiny
     * @date 2016/04/28
     * @see User
     */
    @Override
    @Transactional
    public String userRegisterPwdNotRequired(User user) {
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_EMPTY.getCode(), MessageTemplate.USER_EMPTY.getMsg());
        }
        //注册
        registrationModule(user);
        //生成初始密码短信记录
        String code = StringUtil.genRandomCode();
//        String content = commonService.smsRecord(user.getMobile(), SMSTemplate.DEFAULTPWD.getModule(), code);
//        if (content == null) {
//            throw new BusinessException(MessageTemplate.SMS_RECORD_INSERT_ERROR.getCode(), MessageTemplate.SMS_RECORD_INSERT_ERROR.getMsg());
//        }
        //将初始密码更新到cas
        if (casUserDao.updatePasswordByPhone(user.getMobile(), code) != 1) {
            throw new BusinessException(MessageTemplate.USER_REGISTER_ERROR.getCode(), MessageTemplate.USER_REGISTER_ERROR.getMsg());
        }
        //发送短信
//        if (!commonService.send(user.getMobile(), content)) {
//            throw new BusinessException(MessageTemplate.PWD_SEND_ERROR.getCode(), MessageTemplate.PWD_SEND_ERROR.getMsg());
//        }
        //新增的超市用户推送到cbms
        AddCbmsContact addCbmsContact = new AddCbmsContact(user.getId(), user.getMobile(), null, null, user.getSource());
        MqLog mqLog = new MqLog(MqLogModual.ADD_CBMS_CONTACT.getModule(), RemoteDataSource.CBMS.getCode(), new Gson().toJson(addCbmsContact), "Y", null);
        commonService.sendMqAndInsertLog(addDse, addCbmsContact, mqLog);

        return code;
    }

    /**
     * 更新cas库用户动态验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @author peanut
     * @date 2016/05/13
     */
    @Override
    public void updateCasDynamicPassword(String phone, String code) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(MessageTemplate.USER_CODE_EMPTY.getCode(), MessageTemplate.USER_CODE_EMPTY.getMsg());
        }
        casUserDao.updateCasDynamicPassword(phone, code);
    }

    /**
     * 用户登陆
     *
     * @param code 根据type类型，可能是密码和手机验证码
     * @return
     * @author peanut
     * @date 2016/04/29
     * @Override
     * @Deprecated public boolean userLogin(String phone, String code, String type, String cookieId) {
     * if (StringUtils.isBlank(phone)) {
     * throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
     * }
     * User user = userDao.selectByPhone(phone);
     * //验证手机号是否注册
     * if (user == null) {
     * throw new BusinessException(MessageTemplate.PHONE_UNREGISTER.getCode(), MessageTemplate.PHONE_UNREGISTER.getMsg());
     * }
     * if (StringUtils.isBlank(type)) {
     * throw new BusinessException(MessageTemplate.LOGIN_TYPE_EMPTY.getCode(), MessageTemplate.LOGIN_TYPE_EMPTY.getMsg());
     * }
     * boolean isPwd = true;
     * //手机验证码登陆校验
     * if (type.equals(LoginType.PHONE_CODE.toString())) {
     * if (StringUtils.isBlank(code)) {
     * throw new BusinessException(MessageTemplate.SMS_CHECK_ERROR.getCode(), MessageTemplate.SMS_CHECK_ERROR.getMsg());
     * }
     * isPwd = false;
     * isCodeAvailable(phone, code, SMSTemplate.LOGIN.getModule());
     * }
     * //手机密码登陆校验
     * else if (type.equals(LoginType.PHONE_PASSWORD.toString())) {
     * if (StringUtils.isBlank(code)) {
     * throw new BusinessException(MessageTemplate.LOGIN_PWD_EMPTY.getCode(), MessageTemplate.LOGIN_PWD_EMPTY.getMsg());
     * }
     * if (!code.equals(user.getPassword())) {
     * throw new BusinessException(MessageTemplate.LOGIN_PWD_ERROR.getCode(), MessageTemplate.LOGIN_PWD_ERROR.getMsg());
     * }
     * }
     * <p>
     * // 登陆后续的操作
     * doSeriesLogicAfterLogin(phone, cookieId, isPwd ? null : code);
     * <p>
     * return true;
     * }
     * @see com.prcsteel.ec.core.enums.LoginType
     */

    @Override
    @Transactional
    public AppUser APPUserLogin(String deviceNo,String deviceType, String mobile, String code) {
        //设备号和设备类型必须给
        if(StringUtils.isEmpty(deviceNo)){
           throw new BusinessException(MessageTemplate.DEVICE_NO_NULL_ERROR.getCode(), MessageTemplate.DEVICE_NO_NULL_ERROR.getMsg());
        }else if (StringUtils.isEmpty(deviceType)) {
            throw new BusinessException(MessageTemplate.DEVICE_TYPE_NULL_ERROR.getCode(), MessageTemplate.DEVICE_TYPE_NULL_ERROR.getMsg());
        }
        //判断是否存在
        if (!isPhoneExists(mobile) && !Constant.APP_SUPER_USER.equals(mobile)) {   //app特权用户不走注册流程
            User user = new User();
            user.setMobile(mobile);
            user.setCode(code);
            user.setSource(UserSource.APP.toString());
            userRegisterPwdNotRequired(user);
        }
        //登陆
        AppUser appUser = APPUserLogin_step1(mobile, code, deviceNo, deviceType);
        //超市验证码失效
        smsValidCodeDao.invalidCodeByMobileAndCodeAndModule(mobile, SMSTemplate.LOGIN.getModule(), null);
        //cas动态验证码失效
        casUserDao.updateCasDynamicPassword(mobile, null);
        return appUser;
    }

    @Override
    public void APPUserLogout(String authToken) {
        checkUser(authToken);
        cacheService.delete(authToken);
    }

    private AppUser APPUserLogin_step1(String mobile, String code, String deviceNo,String deviceType) {
        //做本库登陆
        int i = 0;
        if (!Constant.APP_SUPER_USER.equals(mobile)) {
            try {
                checkPhoneValid(mobile);
                i = casUserService.SelectByMobileAndValidCode(mobile, code);
            } catch (BusinessException e) {
                throw e;
            }
            if (i == 0) {
                throw new BusinessException(MessageTemplate.LOGIN_ERROR.getCode(), MessageTemplate.LOGIN_ERROR.getMsg());
            }
        }

        User user = userDao.selectByPhone(mobile);
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_EMPTY.getCode(), MessageTemplate.USER_EMPTY.getMsg());
        }
        //去缓存找该用户是否登录
        AppUser appUser = (AppUser)cacheService.get(mobile);
        if(appUser!=null){
            //如果登录的设备不是原来缓存中的设备，让原来的设备下线
            if(!appUser.getDeviceNo().equals(deviceNo)){
                logger.info("sending message to kick user off line.");
                appPushService.sendPushMessage(appUser.getDeviceNo(), "logout");
                logger.info("sending message to kick user off line done");
            }
            //把原来的登录设备信息从缓存中抹去
            logger.info("deleting user info from cache.");
            cacheService.delete(appUser.getAuthToken());
            cacheService.delete(mobile);
            logger.info("deleting user info from cache.. done");
        }
        //生成token
        String token = StringUtil.build(mobile + System.currentTimeMillis(), "utf-8");
        logger.debug("app login,mobile:{},token:{}", mobile, token);
        //保存token缓存
        appUser = new AppUser();
        appUser.setUser(user);
        appUser.setAuthToken(token);
        appUser.setDeviceNo(deviceNo);
        appUser.setDeviceType(deviceType);
        //永久有效
        cacheService.set(token, 0, appUser);
        cacheService.set(mobile, 0, appUser);
        //判断该用户在busi_app_login_log表中是否存在；存在做update；不存在做insert
        AppLoginLog appLoginLog = genericDaoService.findOne(new AppLoginLog(user.getId()));
        if (appLoginLog == null) {
            appLoginLog = new AppLoginLog(user.getId(), token);
            try {
                appLoginLog.preInsert();
                genericDaoService.insert(appLoginLog);
            } catch (Exception e) {
                logger.error("app用户登录日志表插入失败：" + e.getMessage());
            }
        } else {
            appLoginLog.setToken(token);
            appLoginLog.setLastUpdated(new Date());
            appLoginLog.setModificationNumber(appLoginLog.getModificationNumber() + 1);
            try {
                genericDaoService.updateByKey(appLoginLog);
            } catch (Exception e) {
                logger.error("app用户登录日志表更新失败：" + e.getMessage());
            }
        }
        return appUser;
    }

    @Override
    public User checkUser(String authToken) {
        if (StringUtils.isBlank(authToken)) {
            throw new BusinessException(MessageTemplate.LOGIN_TOKEN_BLANK.getCode(), MessageTemplate.LOGIN_TOKEN_BLANK.getMsg());
        }
        AppUser appUser = findByToken(authToken);
        if (appUser == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        User user = appUser.getUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        return user;
    }

    /**
     * 根据token获取用户；缓存中找不到，则去日志表中找
     *
     * @param token
     * @return
     */
    private AppUser findByToken(String token) {
        try {
            Object o = cacheService.get(token);
            if (o == null || !(o instanceof AppUser)) {
                return selectByTokenFromLog(token);
            }
            return (AppUser) o;
        } catch (Exception e) {
            return selectByTokenFromLog(token);
        }
    }

    /**
     * 根据token去日志表中查询
     *
     * @param token
     * @return
     */
    private AppUser selectByTokenFromLog(String token) {
        try {
            AppLoginLog appLoginLog = genericDaoService.findOne(new AppLoginLog(token));
            if (appLoginLog == null) {
                logger.error("token:{} 未查询到用户信息!", token);
                return null;
            }
            User user = genericDaoService.findOne(new User(appLoginLog.getUserId()));
            AppUser appUser = new AppUser();
            appUser.setUser(user);
            appUser.setAuthToken(token);
            //保存token缓存,永久有效
            cacheService.set(token, 0, appUser);
            return appUser;
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.USER_GET_APPUSER_ERROR.getCode(), MessageTemplate.USER_GET_APPUSER_ERROR.getMsg());
        }
    }

    /**
     * 用户登陆后的处理
     *
     * @return
     * @author peanut
     * @date 2016/05/13
     */
    @Override
    public int afterLogin(String cookieId) {
        String userPhone = UserUtils.getPrincipal();
        User user;
        if (StringUtils.isNotBlank(userPhone) && (user = userDao.selectByPhone(userPhone)) != null) {

            //1、添加登陆日志
            systemOperationLogService.insertLoginLog(userPhone, null);

            //2、角色信息存储


            //3、调用并更新偏好与探索信息(接口)


            //4、调用并更新购物车
            cartService.updateCartUserGuidByCookieId(cookieId);

            //5、更新搜索历史
            searchHistoryDao.setCookieToLoginId(user.getGuid(), cookieId);

            //6、页面中转

            //7、登陆验证码失效

            //超市验证码失效
            smsValidCodeDao.invalidCodeByMobileAndCodeAndModule(userPhone, SMSTemplate.LOGIN.getModule(), null);
            //cas动态验证码失效
            casUserDao.updateCasDynamicPassword(userPhone, null);

            return 1;
        }
        return 0;
    }

    /**
     * 处理登陆后的一系列逻辑

     @Deprecated public void doSeriesLogicAfterLogin(String phone, String cookieId, String code) {
     //1、调用cas接口，用户上线
     //commonService.login(phone);

     //2、角色信息存储


     //3、调用并更新偏好与探索信息(接口)


     //4、调用并更新购物车
     cartService.updateCartUserGuidByCookieId(cookieId);

     //5、页面中转

     //6、登陆验证码失效
     if (StringUtils.isNotBlank(code)) {
     //超市验证码失效
     smsValidCodeDao.invalidCodeByMobileAndCodeAndModule(phone, SMSTemplate.LOGIN.getModule(), code);
     //cas动态验证码失效
     casUserDao.updateCasDynamicPassword(phone, null);

     }
     }
     */

    /**
     * 新增CBMS联系人信息到超市
     *
     * @param mobile
     * @param name
     * @author Tiny
     * @date 2016/05/31
     */
    @Override
    @Transactional
    public void addCBMSUser(String mobile, String name) {
        //0.判断格式是否正确，长度是否超标
        //1.根据手机号去cas数据库查一把，判断用户是否已经存在，存在则不插cas表，不存在则生成一个动态密码 插cas表，发短信给用户告知动态密码（文案需定义，因为入口是CBMS，而不是超市）
        //2.在超市user表查一把，判断用户是否存在，存在则不插user表，不存在则插user表
        //3.记录操作日志
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_MOBILE_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_MOBILE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_NAME_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_NAME_EMPTY.getMsg());
        }
        if (mobile.length() != Constant.MOBILE_SIZE || !StringUtil.isPhoneNumberCheck(mobile)) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_MOBILE_FORMAT_ERROR.getCode(),
                    MessageTemplate.REMOTE_USER_MOBILE_FORMAT_ERROR.getMsg());
        }

        User user = addRemoteUser(mobile, name, UserSource.CBMS, SMSTemplate.CBMS_USER_DYNAMIC_PWD, OpType.CBMS_USER_ADD);

        //超市推送新增联系人的超市userId给CBMS
        UpdateCbmsContact updateCbmsContact = new UpdateCbmsContact(user.getId(), user.getMobile());
        MqLog mqLog = new MqLog(MqLogModual.ADD_MARKET_USER_ID.getModule(), RemoteDataSource.CBMS.getCode(), new Gson().toJson(updateCbmsContact), "Y", null);
        commonService.sendMqAndInsertLog(userIdDse, updateCbmsContact, mqLog);
    }

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
    @Override
    public User addRemoteUser(String mobile, String name, UserSource source, SMSTemplate template, OpType opType) {
        User user = userDao.selectByPhone(mobile);
        //根据手机号去user表查
        if (user == null) {
            user = new User(name, mobile, null,
                    source.toString(), Constant.USER_STATUS_ENABLE, Constant.SYS_USER, Constant.SYS_USER);
            user.preInsert(globalIdService.getId());
            userDao.insert(user);
        }

        //根据手机号去cas数据库查
        if (casUserDao.selectByPhone(mobile) == null) {
            //cas用户添加
            //生成初始密码短信记录
            String code = StringUtil.genRandomCode();
//            String content = commonService.smsRecord(mobile, template.getModule(), code);

            CasUser casUser = new CasUser();
            casUser.setAccount(mobile);
            casUser.setPassword(code);
            casUser.setIsActivated("Y");
            if (casUserDao.insertSelective(casUser) != 1) {
                throw new BusinessException(MessageTemplate.REMOTE_USER_INSERT_FAIL.getCode(),
                        MessageTemplate.REMOTE_USER_INSERT_FAIL.getMsg());
            }

            //添加日志（新增联系人信息）
            systemOperationLogService.insertModel(opType, new Gson().toJson(Arrays.asList(new String[]{mobile, name})));

            //发送短信
//            if (!commonService.send(mobile, content)) {
//                throw new BusinessException(MessageTemplate.REMOTE_PWD_SEND_ERROR.getCode(), MessageTemplate.REMOTE_PWD_SEND_ERROR.getMsg());
//            }
        }

        return user;
    }

    /**
     * 更新CBMS联系人信息到超市
     *
     * @param user
     * @author Tiny
     * @date 2016/05/31
     */
    @Override
    @Transactional
    public void updateCBMSUser(User user) {
        //0.判断格式是否正确，长度是否超标
        //1.拿id去user表查；用户不存在走新增流程；
        //1.1用户存在：则判断新手机号在user表中是否已经存在；新手机号存在且cas表中不存在，cas表新增；
        //1.2新手机号不存在：user表更新；旧手机号cas表中存在做更新，不存在走新增新手机号用户流程
        //2.记录操作日志
        if (user.getId() == null) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_ID_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_ID_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(user.getMobile())) {
            throw new BusinessException(MessageTemplate.REMOTE_NEW_MOBILE_EMPTY.getCode(),
                    MessageTemplate.REMOTE_NEW_MOBILE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(user.getName())) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_NAME_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_NAME_EMPTY.getMsg());
        }
        if (user.getMobile().length() != Constant.MOBILE_SIZE || !StringUtil.isPhoneNumberCheck(user.getMobile())) {
            throw new BusinessException(MessageTemplate.REMOTE_NEW_MOBILE_FORMAT_ERROR.getCode(),
                    MessageTemplate.REMOTE_NEW_MOBILE_FORMAT_ERROR.getMsg());
        }

        //更新CBMS联系人信息
        updateRemoteUser(user);
    }

    /**
     * 更新联系人信息（外部系统）
     *
     * @param user
     * @author Tiny
     * @date 2016/06/01
     */
    private void updateRemoteUser(User user) {
        User oldUser = userDao.queryActiveUserById(user.getId());
        if (oldUser != null) {
            //user表新手机号不存在
            if (userDao.selectNewMobile(user) == null) {
                //超市user表更新
                if (genericDaoService.updateByKey(user) != 1) {
                    throw new BusinessException(MessageTemplate.REMOTE_USER_UPDATE_FAIL.getCode(),
                            MessageTemplate.REMOTE_USER_UPDATE_FAIL.getMsg());
                }
                //cas更新
                updateCas(oldUser.getMobile(), user);
            } else if (!isPhoneExists(user.getMobile())) {//user表新手机号存在；则判断cas表中新手机号是否存在，不存在做新增
                addCBMSUser(user.getMobile(), user.getName());
            }
        } else { //在user表中没有对应的用户
            addCBMSUser(user.getMobile(), user.getName());
        }
    }

    /**
     * cas更新
     *
     * @param oldMobile
     * @param newUser
     * @author Tiny
     * @date 2016/06/15
     */
    private void updateCas(String oldMobile, User newUser) {
        //旧手机号cas表中存在做更新；不存在:若新手机号在cas表中也不存在；走新增新手机号用户流程
        if (casUserDao.selectByPhone(oldMobile) != null) {
            //新手机号跟旧手机号不相等；cas中新手机号不存在；cas更新
            if (!oldMobile.equals(newUser.getMobile()) && !isPhoneExists(newUser.getMobile()) && casUserDao.updateMobileByMobile(newUser.getMobile(), oldMobile) != 1) {
                throw new BusinessException(MessageTemplate.REMOTE_USER_UPDATE_FAIL.getCode(),
                        MessageTemplate.REMOTE_USER_UPDATE_FAIL.getMsg());
            }
            //添加日志（修改CBMS联系人手机号）
            if (!oldMobile.equals(newUser.getMobile())) {
                systemOperationLogService.insertModel(OpType.CBMS_USER_MODIFY_MOBILE, new Gson().toJson(Arrays.asList(new String[]{newUser.getMobile(), newUser.getName()})));
            } else {  //添加日志（修改CBMS联系人姓名）
                systemOperationLogService.insertModel(OpType.CBMS_USER_MODIFY_NAME, new Gson().toJson(Arrays.asList(new String[]{newUser.getMobile(), newUser.getName()})));
            }

        } else if (!isPhoneExists(newUser.getMobile())) { //新手机号在cas表中也不存在；走新增新手机号用户流程
            addCBMSUser(newUser.getMobile(), newUser.getName());
        }
    }

    /**
     * CBMS禁用/启用联系人
     *
     * @param changeContactStatusDto
     * @author Tiny
     * @date 2016/07/01
     */
    @Override
    public void changeContactStatus(ChangeContactStatusDto changeContactStatusDto) {
        if (changeContactStatusDto.getId() == null) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_ID_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_ID_EMPTY.getMsg());
        }
        if (changeContactStatusDto.getStatus() == null) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_STATUS_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_STATUS_EMPTY.getMsg());
        }
        User user = userDao.queryUserById(changeContactStatusDto.getId());
        if (user == null) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_NOT_EXIST.getCode(),
                    MessageTemplate.REMOTE_USER_NOT_EXIST.getMsg());
        }
        //user表更新
        user.setIsDeleted(changeContactStatusDto.getStatus().equals(1) ? "N" : "Y");
        if (genericDaoService.updateByKey(user) != 1) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_UPDATE_FAIL.getCode(),
                    MessageTemplate.REMOTE_USER_UPDATE_FAIL.getMsg());
        }
        //cas表更新
        casUserDao.updateIsLockedByMobile(user.getMobile(), changeContactStatusDto.getStatus().equals(1) ? "N" : "Y");
    }

    /**
     * cas用户是否可用
     *
     * @param phone
     * @author peanut
     * @date 2016/07/14
     */
    @Override
    public void checkPhoneValid(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (!StringUtil.isPhoneNumberCheck(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_ERROR.getCode(), MessageTemplate.PHONE_ERROR.getMsg());
        }
        CasUser casUser = casUserDao.selectByPhone(phone);
        if (casUser == null) {
            throw new BusinessException(MessageTemplate.PHONE_UNREGISTER.getCode(), MessageTemplate.PHONE_UNREGISTER.getMsg());
        }
        if (!"N".equals(casUser.getIsDeleted()) || !"N".equals(casUser.getIsLocked()) || !"Y".equals(casUser.getIsActivated())) {
            throw new BusinessException(MessageTemplate.USER_ACCOUNT_LOCKED.getCode(), MessageTemplate.USER_ACCOUNT_LOCKED.getMsg());
        }
    }
}
