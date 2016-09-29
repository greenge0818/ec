package com.prcsteel.ec.service;

import com.prcsteel.ec.core.model.RestResult;
import com.prcsteel.ec.model.domain.ec.MqLog;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.model.dto.CategoryCacheDto;
import com.prcsteel.ec.model.dto.RequirementItemDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author peanut
 * @description: 通用Service
 * @date 2016/4/28 10:14
 */
public interface CommonService {

    /**
     * 向用户推送消息
     * @param mobile 手机号
     */
    public void sendNotification(String mobile);

    /**
     * 处理发送手机验证码逻辑
     *
     * @param phone 手机号
     * @param model 发送手机号的模块
     * @return
     * @author peanut
     * @date 2016/04/28
     */
    boolean doSendSMS(String phone, String model);

    /**
     * 发送短信
     *
     * @param phone   手机号码
     * @param content 发送内容
     * @return
     * @author peanut
     * @date 2016/04/28
     */
    Boolean send(String phone, String content);

    /**
     * @Author: Tiny
     * @Description: 上传用户附件公用方法
     * @Date: 2016年4月28日
     */
    String uploadFile(MultipartFile file, String cookieId, String domain);

    /**
     * @Author: Tiny
     * @Description: 上传用户附件公用方法
     * @Date: 2016年4月28日
     */
    String uploadFileAll(MultipartFile file, String domain);

    void login(String mobile);

    /**
     * 生成短信记录
     *
     * @param phone  手机号码
     * @param module 模板
     * @return
     * @author Tiny
     * @date 2016/05/12
     */
    String smsRecord(String phone, String module, String code);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    User getCurrentUser();

    /**
     * @Author: Tiny
     * @Description: 获取推送给分拣的需求单类型
     * @Date: 2016年05月26日
     */
    String getASSRequirementType(String type);

    /**
     * @ClassName: getFriendshipLink
     * @Description: 获取友情链接列表
     * @Author Tiny
     * @Date 2016年06月08日
     */
    RestResult getFriendshipLink();

    /**
     * @Author: Tiny
     * @Description: 获取掌柜指数
     * @Date: 2016年07月07日
     */
    RestResult getPriceIndexes(String city);

    /**
     * @Author: Tiny
     * @Description: 获取广告
     * @Date: 2016年07月07日
     */
    RestResult getAd();

    /**
     * @Author: Rabbit
     * @Description: 获取悬浮广告
     */
    RestResult getFloatAd();

    /**
     * @Author: Tiny
     * @Description: 获取会员活动
     * @Date: 2016年07月07日
     */
    RestResult getActivities();

    /**
     * @Author: Tiny
     * @Description: 获取热门行情资讯
     * @Date: 2016年07月13日
     */
    RestResult getHotMarket();

    /**
     * @Author: Tiny
     * @Description: 获取钢架汇总统计
     * @Date: 2016年07月13日
     */
    RestResult getSteelStatistics();

    /**
     * @Author: Tiny
     * @Description: 推送消息到mq；记录日志到busi_mq_log
     * @Date: 2016年07月14日
     */
    void sendMqAndInsertLog(String des, Object o, MqLog mqLog);

    /**
     * @Author: Tiny
     * @Description: 给搜索引擎使用--品名列表
     * @Date: 2016年09月12日
     */
    List<CategoryCacheDto.CategoryClass.Nsort> land2();

    /**
     * @Author: Tiny
     * @Description: 给搜索引擎使用--品名单个厂家列表
     * @Date: 2016年09月12日
     */
    List<RequirementItemDto> land3();

    /**
     * @Author: Tiny
     * @Description: 给搜索引擎使用--品名单个城市列表
     * @Date: 2016年09月12日
     */
    List<RequirementItemDto> land4();

    /**
     * @Author: Tiny
     * @Description: 根据时间获取当前时间是否是工作日
     * @Date: 2016年09月21日
     */
    String getHoliday();
}


