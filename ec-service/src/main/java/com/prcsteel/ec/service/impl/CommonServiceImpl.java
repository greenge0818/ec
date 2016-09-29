package com.prcsteel.ec.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.ec.core.enums.ASSRequirementType;
import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.RequirementType;
import com.prcsteel.ec.core.enums.SMSTemplate;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.model.RestResult;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.service.FileService;
import com.prcsteel.ec.core.util.DateUtil;
import com.prcsteel.ec.core.util.FileUtil;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.model.domain.ec.*;
import com.prcsteel.ec.model.dto.*;
import com.prcsteel.ec.model.query.MarketToSmartHotResourceQuery;
import com.prcsteel.ec.persist.dao.ec.SmsValidCodeDao;
import com.prcsteel.ec.persist.dao.ec.UserDao;
import com.prcsteel.ec.service.*;
import com.prcsteel.ec.service.api.RestCbmsService;
import com.prcsteel.ec.service.api.RestCommonService;
import com.prcsteel.ec.service.api.RestMarketCenterService;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * @author peanut
 * @description: 通用Service
 * @date 2016/4/28 10:14
 */
@Service
public class CommonServiceImpl implements CommonService {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Value("${smsService}")
    private String smsServiceAddress;  // 短信服务地址
    @Value("${smsService.switch}")
    private boolean smsServiceSwitch;  // 短信发送开关
    @Value("${register.sendSMS.swich}")
    private boolean registerSendSMSSwich;  // 注册发送验证码次数控制开关

    @Resource
    private SmsValidCodeDao smsValidCodeDao;

    @Resource
    private GlobalIdService globalIdService;

    @Resource
    private UserService userService;

    @Resource
    FileService fileService;

    @Resource
    private UserDao userDao;

    @Resource
    private CacheService cacheService;

    @Resource
    private RestCbmsService restCbmsService;

    @Resource
    private RestCommonService restCommonService;

    @Resource
    private RestMarketCenterService restMarketCenterService;

    @Resource
    private ActiveMQService activeMQService;

    @Resource
    private GenericDaoService genericDaoService;

    @Resource
    private AppPushService appPushService;

    @Resource
    private MarketService marketService;

    private static final Gson gson = new Gson();

    /**
     * 向用户推送消息
     *
     * @param mobile 手机号
     */
    public void sendNotification(String mobile) {
        try {
            AppUser appUser = (AppUser) cacheService.get(mobile);
            if (appUser != null) {
                appPushService.sendPushNoticfication(appUser.getDeviceNo(), appUser.getDeviceType(), Constant.APP_NOTIFICATION_TITLE, Constant.APP_NOTIFICATION_CONTENT);
            }
        } catch (Exception e) {
            logger.error("message send error", e);
        }
    }

    /**
     * 处理发送手机验证码逻辑
     *
     * @param phone  手机号
     * @param module 发送手机号的模块
     * @return
     * @author peanut
     * @date 2016/04/28
     */
    @Override
    public boolean doSendSMS(String phone, String module) {

        String content = smsRecord(phone, module, null);

        if (content == null) {
            return false;
        }

        //具体发送短信
        return send(phone, content);

    }

    /**
     * 检查模块下的验证码再次发送时间是否到达规定合理时间
     *
     * @param phone  手机号码
     * @param module 模块
     */
    private boolean checkSMSIsLimited(String phone, String module) {
        SmsValidCode svc = smsValidCodeDao.selectUnresendByMobileAndModule(phone, module);
        if (svc == null) return false;
        return true;
    }

    /**
     * 生成短信记录
     *
     * @param phone
     * @param module
     * @return
     */
    @Override
    public String smsRecord(String phone, String module, String code) {
        //手机号验证及发送手机号
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(module) || !StringUtil.isPhoneNumberCheck(phone))
            return null;

        //检查模块下的验证码再次发送时间是否到达规定合理时间
        if (checkSMSIsLimited(phone, module)) {
            throw new BusinessException(MessageTemplate.SMS_SEND_LIMITED.getCode(), MessageTemplate.SMS_SEND_LIMITED.getMsg());
        }

        //注册；控制10分钟内重复发送验证码3次以上（不包含3次），该手机号锁定4小时不能够注册发送验证码
        if (checkRegisterSendLimited(phone, module)) {
            throw new BusinessException(MessageTemplate.SMS_REGISTER_SEND_LIMITED.getCode(), MessageTemplate.SMS_REGISTER_SEND_LIMITED.getMsg());
        }

        //根据模块获取发送短信内容

        //1、获取短信验证码
        if (code == null) {
            code = StringUtil.genSMSValidCode();
        }
        //登陆模块 动态验证码更新至cas库
        if (module.equals(SMSTemplate.LOGIN.getModule())) {
            userService.updateCasDynamicPassword(phone, code);
        }

        //2、获取短信模块内容
        String content = SMSTemplate.getContentByModule(module);

        content = content.replace("#code", code);

        //保存发送记录
        SmsValidCode svc = new SmsValidCode();
        svc.preInsert(globalIdService.getId());
        svc.setMobile(phone);
        svc.setModule(module);
        svc.setValidCode(code);
        //当前时间+60秒
        svc.setResendTime(new Date(new Date().getTime() + Constant.CODE_LIMITED_TIME));
        //当前时间+30分钟
        svc.setExpireTime(new Date(new Date().getTime() + Constant.CODE_EXPIRE_TIME));
        svc.setCreatedBy(Constant.SYS_USER);
        svc.setLastUpdatedBy(Constant.SYS_USER);
        smsValidCodeDao.insert(svc);
        return content;
    }

    /**
     * 注册；控制10分钟内重复发送验证码3次以上（不包含3次），该手机号锁定4小时不能够注册发送验证码
     *
     * @param phone
     * @param module
     * @return
     */
    private Boolean checkRegisterSendLimited(String phone, String module) {
        if (!SMSTemplate.REGISTER.getModule().equals(module) || !registerSendSMSSwich) return false;
        List<SmsValidCode> smsValidCodes = smsValidCodeDao.selectRegisterRecordByMobile(phone, Constant.REGISTER_SMS_LIMITED_THREE_TIMES);
        if (smsValidCodes != null && smsValidCodes.size() == 3 &&
                (smsValidCodes.get(0).getCreated().getTime() - smsValidCodes.get(2).getCreated().getTime() <= Constant.REGISTER_SMS_LIMITED_TIME) &&
                (new Date().getTime() - smsValidCodes.get(0).getCreated().getTime() <= Constant.REGISTER_SMS_LOCKED_TIME)) {
            return true;
        }
        return false;
    }

    /**
     * 发送短信
     *
     * @param phone   手机号码
     * @param content 发送内容
     * @return
     */
    @Override
    public Boolean send(String phone, String content) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(MessageTemplate.PHONE_EMPTY.getCode(), MessageTemplate.PHONE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(MessageTemplate.SMS_CONTENT_EMPTY.getCode(), MessageTemplate.SMS_CONTENT_EMPTY.getMsg());
        }
        //发短信开关
        if (!smsServiceSwitch) return true;
        //发送短信
        HashMap<String, String> params = new HashMap<>();
        String timestamp = DateUtil.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
        params.put("timestamp", timestamp);
        content = content.replaceAll(" ", "");
        params.put("content", content);
        String from = Constant.FROM;
        params.put("from", from);
        params.put("phone", phone);
        String token;
        try {
            token = StringUtil.getSignature(params, Constant.SECRET);
        } catch (IOException e) {
            logger.error("发送短信时生成token失败");
            throw new BusinessException(MessageTemplate.SMS_GEN_TOKEN_FAIL.getCode(), MessageTemplate.SMS_GEN_TOKEN_FAIL.getMsg() + e.getMessage());
        }

        String smsResult;
        RestResult result;
        try {
            smsResult = restCommonService.send(timestamp, token, phone, content, from);
        } catch (Exception e) {
            logger.error("发送短信ESB连接出错!");
            throw new BusinessException(MessageTemplate.ESB_SEND_SMS_ERROR.getCode(), MessageTemplate.ESB_SEND_SMS_ERROR.getMsg());
        }
        try {
            result = gson.fromJson(smsResult, RestResult.class);
        } catch (Exception e) {
            logger.error("发送短信json转换出错!");
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }

        if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {

            return true;
        } else {
            logger.info("短信返回内容：{}", smsResult);
            throw new BusinessException(MessageTemplate.SMS_SEND_ERROR.getCode(), MessageTemplate.SMS_SEND_ERROR.getMsg());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 上传用户附件公用方法（保存到临时目录）
     * @Date: 2016年4月28日
     */
    @Override
    public String uploadFile(MultipartFile file, String cookieId, String domain) {
        checkFile(file);

        User user = getCurrentUser();
        String uniqueId = user == null ? cookieId : user.getGuid();

        String basePath = Constant.FILESAVETEMPPATH + uniqueId
                + File.separator + new Date().getTime() + File.separator; // 按日期归类存放
        String tempPath = file.getOriginalFilename().replaceAll(",", "").replaceAll(" ", "");
        String savePath = basePath + tempPath;
        String url = "";
        try {
            url = fileService.saveFile(file.getInputStream(), savePath);
            if (StringUtils.isEmpty(url)) {
                throw new BusinessException(MessageTemplate.UPLOAD_FILE_ERROR.getCode(), MessageTemplate.UPLOAD_FILE_ERROR.getMsg());
            }
        } catch (Exception ex) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_ERROR.getCode(), MessageTemplate.UPLOAD_FILE_ERROR.getMsg() + ex.getMessage());
        }
        return domain + Constant.FILE_URL_PREFIX + url;
    }

    @Override
    public String uploadFileAll(MultipartFile file, String domain) {
        checkFile2(file);
        String basePath = Constant.FILESAVETEMPPATH + new Date().getTime() + File.separator; // 按日期归类存放
        String tempPath = file.getOriginalFilename().replaceAll(",", "").replaceAll(" ", "");
        String savePath = basePath + tempPath;
        String url;
        try {
            url = fileService.saveFile(file.getInputStream(), savePath);
            if (StringUtils.isEmpty(url)) {
                throw new BusinessException(MessageTemplate.UPLOAD_FILE_ERROR.getCode(), MessageTemplate.UPLOAD_FILE_ERROR.getMsg());
            }
        } catch (Exception ex) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_ERROR.getCode(), MessageTemplate.UPLOAD_FILE_ERROR.getMsg() + ex.getMessage());
        }
        return domain + Constant.FILE_URL_PREFIX + url;
    }

    /**
     * @Author: Tiny
     * @Description: 检查文件是否符合规定
     * @Date: 2016年4月28日
     */
    private void checkFile(MultipartFile file) {
        if (file == null) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_NOT_FOUND.getCode(), MessageTemplate.UPLOAD_FILE_NOT_FOUND.getMsg());
        }

        String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

        if (file.getSize() <= 0) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_ILLEGAL.getCode(),
                    MessageTemplate.UPLOAD_FILE_ILLEGAL.getMsg());
        }
        if (suffix == null || (!Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase()) && !Constant.FILE_SUFFIX.contains(suffix.toLowerCase()))) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_INCORRECT_FORMAT.getCode(),
                    MessageTemplate.UPLOAD_FILE_INCORRECT_FORMAT.getMsg());
        }
        if (file.getSize() > Constant.M_SIZE * Constant.MAX_IMG_SIZE) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_INCORRECT_SIZE.getCode(),
                    MessageTemplate.UPLOAD_FILE_INCORRECT_SIZE.getMsg());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 检查文件是否符合规定
     * @Date: 2016年4月28日
     */
    private void checkFile2(MultipartFile file) {
        if (file == null) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_NOT_FOUND.getCode(), MessageTemplate.UPLOAD_FILE_NOT_FOUND.getMsg());
        }
        if (file.getSize() <= 0) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_ILLEGAL.getCode(),
                    MessageTemplate.UPLOAD_FILE_ILLEGAL.getMsg());
        }
        if (file.getSize() > Constant.M_SIZE * Constant.MAX_IMG_SIZE) {
            throw new BusinessException(MessageTemplate.UPLOAD_FILE_INCORRECT_SIZE.getCode(),
                    MessageTemplate.UPLOAD_FILE_INCORRECT_SIZE.getMsg());
        }
    }

    @Override
    public void login(String mobile) {
        User user = new User();
        user.setGuid(mobile);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @Override
    public User getCurrentUser() {
        String userPhone = UserUtils.getPrincipal();
        User user;
        if (StringUtils.isNotBlank(userPhone) && (user = userDao.selectByPhone(userPhone)) != null) {
            return user;
        }
        return null;
    }

    /**
     * @Author: Tiny
     * @Description: 获取推送给分拣的需求单类型
     * @Date: 2016年05月26日
     */
    @Override
    public String getASSRequirementType(String type) {
        if (RequirementType.RECEIPT.getCode().equals(type)) {
            return ASSRequirementType.RECEIPT.getCode();
        }
        if (RequirementType.IMAGE.getCode().equals(type)) {
            return ASSRequirementType.IMAGE.getCode();
        }
        if (RequirementType.ONEMORE.getCode().equals(type) || RequirementType.CART.getCode().equals(type)) {
            return ASSRequirementType.FORM.getCode();
        }
        if (RequirementType.HELP.getCode().equals(type)) {
            return ASSRequirementType.MKT_FILE.getCode();
        }
        return null;
    }

    /**
     * @ClassName: getFriendshipLink
     * @Description: 获取友情链接列表
     * @Author Tiny
     * @Date 2016年06月08日
     */
    @Override
    public RestResult getFriendshipLink() {
        RestResult result = (RestResult) cacheService.get(Constant.FRIENDSHIP_LINK_CACHE_KEY);
        if (result == null) {
            //通过ESB去cbms获取友情链接列条
            String cbmsData;
            try {
                cbmsData = restCbmsService.getFriendshipLink(Constant.FRIENDSHIP_LINK_SIZE, Constant.FRIENDSHIP_LINK_TYPE);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.CBMS_SERVER_ERROR.getCode(), MessageTemplate.CBMS_SERVER_ERROR.getMsg());
            }

            try {
                result = gson.fromJson(cbmsData, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                    result.setData(newGson.fromJson(newGson.toJson(result.getData()), new TypeToken<List<RequirementDto>>() {
                    }.getType()));
                    cacheService.set(Constant.FRIENDSHIP_LINK_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, result);
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }

        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 获取掌柜指数
     * @Date: 2016年07月07日
     */
    @Override
    public RestResult getPriceIndexes(String city) {
        if (StringUtils.isBlank(city)) {
            city = "0";
        }
        city = city.replaceAll("市", "");
        //从缓存中取
        RestResult result = (RestResult) cacheService.get(Constant.PRICE_INDEXS_LINK_CACHE_KEY_PRXFIX + city);
        if (result == null) {
            //从行情中心获取掌柜指数
            String mcData;
            try {
                mcData = restMarketCenterService.getPriceIndexes(city, Constant.PRICE_INDEXS_SIZE);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.MC_SERVER_ERROR.getCode(), MessageTemplate.MC_SERVER_ERROR.getMsg());
            }

            try {
                result = gson.fromJson(mcData, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    cacheService.set(Constant.PRICE_INDEXS_LINK_CACHE_KEY_PRXFIX + city, Constant.HOME_PAGE_CACHE_EXPIRED, result);
                } else {
                    return new RestResult(MessageTemplate.OPERATE_SUCCESS.getCode(), new ArrayList<>());
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 获取广告
     * @Date: 2016年07月07日
     */
    @Override
    public RestResult getAd() {
        RestResult result = (RestResult) cacheService.get(Constant.MARKET_AD_CACHE_KEY);
        if (result == null) {

            String adData;
            try {
                adData = restMarketCenterService.getAd(Constant.MARKET_AD_POSID);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.ESB_GET_AD_ERROR.getCode(), MessageTemplate.ESB_GET_AD_ERROR.getMsg());
            }

            try {
                result = gson.fromJson(adData, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    cacheService.set(Constant.MARKET_AD_CACHE_KEY, Constant.HOME_PAGE_CACHE_EXPIRED, result);
                } else {
                    return new RestResult(MessageTemplate.OPERATE_SUCCESS.getCode(), new ArrayList<>());
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return result;
    }

    /**
     * @Author: Rabbit
     * @Description: 获取悬浮广告
     */
    @Override
    public RestResult getFloatAd() {
        RestResult result = (RestResult) cacheService.get(Constant.MARKET_FLOAT_AD_CACHE_KEY);
        if (result == null) {

            String adData;
            try {
                adData = restMarketCenterService.getFloatAd(Constant.FLOAT_AD_POSID);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.ESB_GET_AD_ERROR.getCode(), MessageTemplate.ESB_GET_AD_ERROR.getMsg());
            }

            try {
                result = gson.fromJson(adData, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    List list = ((List) result.getData());
                    if (list.size() > 0) {
                        result.setData(list.get(0));
                        cacheService.set(Constant.MARKET_FLOAT_AD_CACHE_KEY, Constant.HOME_PAGE_CACHE_EXPIRED, result);
                    } else {
                        return new RestResult(MessageTemplate.OPERATE_SUCCESS.getCode(), new ArrayList<>());
                    }
                } else {
                    return new RestResult(MessageTemplate.OPERATE_SUCCESS.getCode(), new ArrayList<>());
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 获取会员活动
     * @Date: 2016年07月07日
     */
    @Override
    public RestResult getActivities() {
        RestResult result = (RestResult) cacheService.get(Constant.ACTIVITIES_CACHE_KEY);
        if (result == null) {
            String mcData;
            try {
                mcData = restMarketCenterService.getActivities(Constant.ACTIVITIES_SIZE);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.MC_SERVER_ERROR.getCode(), MessageTemplate.MC_SERVER_ERROR.getMsg());
            }

            try {
                result = gson.fromJson(mcData, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    cacheService.set(Constant.ACTIVITIES_CACHE_KEY, Constant.HOME_PAGE_CACHE_EXPIRED, result);
                } else {
                    return new RestResult(MessageTemplate.OPERATE_SUCCESS.getCode(), new ArrayList<>());
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 获取热门行情资讯
     * @Date: 2016年07月13日
     */
    @Override
    public RestResult getHotMarket() {
        RestResult result = (RestResult) cacheService.get(Constant.HOT_MARKET_CACHE_KEY);
        if (result == null) {
            String mcData;
            try {
                mcData = restMarketCenterService.getHotMarket(Constant.HOT_MARKET_SIZE);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.MC_SERVER_ERROR.getCode(), MessageTemplate.MC_SERVER_ERROR.getMsg());
            }

            try {
                result = gson.fromJson(mcData, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                    result.setData(newGson.fromJson(newGson.toJson(result.getData()), new TypeToken<List<RequirementDto>>() {
                    }.getType()));
                    cacheService.set(Constant.HOT_MARKET_CACHE_KEY, Constant.HOME_PAGE_CACHE_EXPIRED, result);
                } else {
                    return new RestResult(MessageTemplate.OPERATE_SUCCESS.getCode(), new ArrayList<>());
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }

        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 获取钢架汇总统计
     * @Date: 2016年07月13日
     */
    @Override
    public RestResult getSteelStatistics() {
        RestResult result = (RestResult) cacheService.get(Constant.STEEL_STATISTICS_CACHE_KEY);
        if (result == null) {
            String mcData;
            try {
                mcData = restMarketCenterService.getSteelStatistics(Constant.HOT_RESOURCE_SIZE);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.MC_SERVER_ERROR.getCode(), MessageTemplate.MC_SERVER_ERROR.getMsg());
            }
            try {
                result = gson.fromJson(mcData, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                    result.setData(newGson.fromJson(newGson.toJson(result.getData()), new TypeToken<List<RequirementDto>>() {
                    }.getType()));
                    cacheService.set(Constant.STEEL_STATISTICS_CACHE_KEY, Constant.HOME_PAGE_CACHE_EXPIRED, result);
                } else {
                    return new RestResult(MessageTemplate.OPERATE_SUCCESS.getCode(), new ArrayList<>());
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 推送消息到mq；记录日志到busi_mq_log
     * @Date: 2016年07月14日
     */
    @Override
    public void sendMqAndInsertLog(String des, Object o, MqLog mqLog) {
        try {
            activeMQService.send(des, o);
            genericDaoService.insert(mqLog);
        } catch (JmsException e) {
            mqLog.setIsSuccess("N");
            mqLog.setErrorMsg(e.getMessage());
            genericDaoService.insert(mqLog);
        } catch (Exception e) {
            logger.error("插busi_mq_log表失败:" + gson.toJson(o));
        }
    }

    /**
     * @Author: Tiny
     * @Description: 给搜索引擎使用--品名列表
     * @Date: 2016年09月12日
     */
    @Override
    public List<CategoryCacheDto.CategoryClass.Nsort> land2() {
        List<CategoryCacheDto.CategoryClass.Nsort> list = (List<CategoryCacheDto.CategoryClass.Nsort>) cacheService.get(Constant.LAND2_CACHE_KEY);
        if (list == null) {
            SmartVo smartVoSort = marketService.getSortAndNsort();
            List<CategoryCacheDto> categoryCacheDtoList = null;
            try {
                categoryCacheDtoList = new Gson().fromJson(new Gson().toJson(smartVoSort.getData()), new TypeToken<List<CategoryCacheDto>>() {
                }.getType());
            } catch (BusinessException e) {
            }
            List<CategoryCacheDto.CategoryClass.Nsort> nsortList = new ArrayList<>();
            if (categoryCacheDtoList != null) {
                categoryCacheDtoList.stream().forEach(e -> {
                    List<CategoryCacheDto.CategoryClass> categoryClasses = e.getClassInfo();
                    categoryClasses.stream().forEach(a -> {
                        List<CategoryCacheDto.CategoryClass.Nsort> nsorts = a.getNsort();
                        nsorts.stream().forEach(b -> nsortList.add(b));
                    });
                });
            }

            if (nsortList != null && !nsortList.isEmpty()) {
                cacheService.set(Constant.LAND2_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, nsortList);
            }
            return nsortList;
        }
        return list;
    }

    /**
     * @Author: Tiny
     * @Description: 给搜索引擎使用--品名单个厂家列表
     * @Date: 2016年09月12日
     */
    @Override
    public List<RequirementItemDto> land3() {
        List<RequirementItemDto> resultList = (List<RequirementItemDto>) cacheService.get(Constant.LAND3_CACHE_KEY);
        if (resultList != null) return resultList;
        List<CategoryCacheDto.CategoryClass.Nsort> nsortList = land2();
        if (nsortList == null || nsortList.isEmpty()) {
            return new ArrayList<>();
        }
        List<RequirementItemDto> result = new ArrayList<>();
        nsortList.stream().forEach(e -> {
            SmartVo smartVo = marketService.getFactory2(e.getNsortID());
            List<Map<String, BaseDataDto>> factoryList = gson.fromJson(gson.toJson(smartVo.getData()), new TypeToken<List<Map<String, BaseDataDto>>>() {
            }.getType());
            if (factoryList != null && !factoryList.isEmpty()) {
                factoryList.stream().forEach(a -> {
                    RequirementItemDto requirementItemDto = new RequirementItemDto();
                    requirementItemDto.setCategoryName(e.getNsortName());
                    requirementItemDto.setCategoryUuid(e.getNsortID());
                    requirementItemDto.setFactoryName(a.get("factory").getName());
                    requirementItemDto.setFactoryId(Long.valueOf(a.get("factory").getUuid()));
                    result.add(requirementItemDto);
                });
            }
        });
        if (result != null && !result.isEmpty()) {
            cacheService.set(Constant.LAND3_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, result);
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 给搜索引擎使用--品名单个城市列表
     * @Date: 2016年09月12日
     */
    @Override
    public List<RequirementItemDto> land4() {
        List<RequirementItemDto> resultList = (List<RequirementItemDto>) cacheService.get(Constant.LAND4_CACHE_KEY);
        if (resultList != null) return resultList;
        List<CategoryCacheDto.CategoryClass.Nsort> nsortList = land2();
        if (nsortList == null || nsortList.isEmpty()) {
            return new ArrayList<>();
        }
        List<RequirementItemDto> result = new ArrayList<>();
        nsortList.stream().forEach(e -> {
            CitysDto citysDto = marketService.getAllCitys();
            List<CitysDto.Area.City> cityList = new ArrayList<>();
            if (citysDto != null) {
                citysDto.getData().stream().forEach(a -> a.getCitys().stream().forEach(b -> cityList.add(b)));
            }
            if (cityList != null && !cityList.isEmpty()) {
                cityList.stream().forEach(c -> {
                    RequirementItemDto requirementItemDto = new RequirementItemDto();
                    requirementItemDto.setCategoryName(e.getNsortName());
                    requirementItemDto.setCategoryUuid(e.getNsortID());
                    requirementItemDto.setCityName(c.getName());
                    requirementItemDto.setCityId(c.getId());
                    result.add(requirementItemDto);
                });
            }
        });
        if (result != null && !result.isEmpty()) {
            cacheService.set(Constant.LAND4_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, result);
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 根据时间获取当前时间是否是工作日
     * @Date: 2016年09月21日
     */
    @Override
    public String getHoliday() {
        String data;
        RestResult result;
        try {
            data = restCbmsService.getHoliday(DateUtil.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS));
        } catch (Exception e) {
            logger.error("工作日判断，调用CBMS系统失败：" + e.getMessage());
            return "0";
        }
        try {
            result = gson.fromJson(data, RestResult.class);
        } catch (Exception e) {
            logger.error("工作日判断，json转换出错：" + e.getMessage());
            return "0";
        }
        return result.getStatus();
    }
}
 