package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.RemoteDataSource;
import com.prcsteel.ec.core.enums.RequirementStatus;
import com.prcsteel.ec.core.enums.RequirementType;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.DateUtil;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.model.domain.ec.AppUser;
import com.prcsteel.ec.model.domain.ec.Requirement;
import com.prcsteel.ec.model.model.AddCbmsContact;
import com.prcsteel.ec.model.model.AppUpgrade;
import com.prcsteel.ec.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.tools.config.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Auther:Green.Ge
 * @Description:app接口
 * @Date:2016-05-23
 */
@RestController
@RequestMapping("/api/app/")
public class APPController {
    @Resource
    private CacheService cacheService;
    @Resource
    private UserService userService;
    @Resource
    private RequirementService requirementService;
    @Resource
    private MemberService memberService;
    @Resource
    private AppPushService appPushService;

    //登陆
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation("登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceNo", value = "设备号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result login(String deviceNo, String deviceType, String mobile, String code) {
        Result result = new Result();
        try {
            result.setData(userService.APPUserLogin(deviceNo, deviceType, mobile, code));
            result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    //登出
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ApiOperation("登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "token", dataType = "string", paramType = "query")
    })
    public Result logout(String authToken) {
        Result result = new Result();
        try {
            userService.APPUserLogout(authToken);
            result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    //验证验证码
    //提交回执
    @RequestMapping(value = "req/callme", method = RequestMethod.POST)
    @ApiOperation("提交回执")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token信息", dataType = "string", paramType = "query")
    })
    public Result callme(String authToken) {
        Requirement requirement = new Requirement();
        requirement.setSource(RemoteDataSource.APP.getCode());
        requirement.setType(RequirementType.RECEIPT.getCode());
        requirement.setStageStatus(RequirementStatus.NEW.getCode());
        //新增回执需求
        return insertRequirement(requirement, authToken);
    }
    //上传图片 /common/uploadfile

    //提交图片需求
    @RequestMapping(value = "req/img", method = RequestMethod.POST)
    @ApiOperation("提交图片需求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token信息", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "imgList", value = "图片地址列表，逗号分隔", dataType = "string", paramType = "query")
    })
    public Result addImageRequirement(String authToken, String imgList) {
        if (StringUtils.isBlank(imgList)) {
            return new Result(MessageTemplate.REQUIREMENT_IMGLIST_EMPTY.getCode());
        }
        //新增图片需求
        Requirement requirement = new Requirement();
        requirement.setSource(RemoteDataSource.APP.getCode());
        requirement.setType(RequirementType.IMAGE.getCode());
        requirement.setStageStatus(RequirementStatus.NEW.getCode());
        requirement.setFileUrl(imgList);
        //新增回执需求
        return insertRequirement(requirement, authToken);
    }

    private Result insertRequirement(Requirement requirement, String token) {
        Result result = new Result();
        try {
            requirementService.submitRequirment(requirement, token, null); //mock.
            result.setCode(MessageTemplate.REQUIREMENT_SUBMIT_SUCCESS.getCode());
        } catch (BusinessException be) {
            result.setCode(be.getCode());
        } catch (Exception e) {
            result.setCode(e.getMessage());
        }
        return result;
    }

    //获取再来一单列表
    @RequestMapping(value = "req/oncemorelist", method = RequestMethod.GET)
    @ApiOperation("获取某个时间点之后的再来一单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token信息", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lastDateTime", value = "时间点，格式： YYYY-MM-DD HH:mm:ss", dataType = "string", paramType = "query")
    })
    public Result oncemorelist(String authToken, String lastDateTime) {
        Result result = new Result();
        try {
            result.setData(memberService.getAppOncemorelist(lastDateTime, authToken));
            result.setStatus(DateUtil.dateToStr(new Date(), Constant.DATE_TIME_FORMAT));
            result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    //提交再来一单
    @RequestMapping(value = "req/oncemore", method = RequestMethod.POST)
    @ApiOperation("提交再来一单需求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token信息", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "list", value = "提交内容:格式[{'categoryUuid':'111','materialUuid':'222','factoryId':'333','spec':'1*2*3'}]", dataType = "string", paramType = "query")
    })
    public Result oncemore(String authToken, String list) {
        Result result = new Result();
        //再来一单
        try {
            requirementService.submitAppOnceMore(list, authToken);
            result.setCode(MessageTemplate.REQUIREMENT_SUBMIT_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    //获取消息列表
    @RequestMapping(value = "msg/list", method = RequestMethod.GET)
    @ApiOperation("获取某个时间点之后的消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token信息", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lastDateTime", value = "时间点，格式： YYYY-MM-DD HH:mm:ss", dataType = "string", paramType = "query")
    })
    public Result msgList(String authToken, String lastDateTime) {
        Result result = new Result();
        //消息列表
        try {
            result.setData(memberService.getTodoListAPP(lastDateTime, authToken, false));
            result.setCode(MessageTemplate.REQUIREMENT_SUBMIT_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }
    //获取行情列表
    //获取订单列表
    //获取订单详情

    // 3.5	安卓检查更新
    @RequestMapping(value = "/upgradeandroid", method = RequestMethod.POST)
    @ApiOperation("安卓检查更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "versionCode", value = "APP客户端版本号", dataType = "string", paramType = "query")
    })
    @ResponseBody
    public Result upgradeAndroid(String versionCode) {
        Result result = new Result();
        if(StringUtils.isEmpty(versionCode)){
            result.setCode(MessageTemplate.VERSION_GET_FAIL.getCode());
            return result;
        }
        AppUpgrade upgrade = new AppUpgrade();
        int iVersionCode = Integer.parseInt(versionCode);
        if(iVersionCode<upgrade.getVersionCodeAndroid()){
            upgrade.setUpgrade(true);
        }
        result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        result.setData(upgrade);
        return result;
    }

    // 3.5	IOS检查更新
    @RequestMapping(value = "/upgradeios", method = RequestMethod.POST)
    @ApiOperation("IOS检查更新")
    @ResponseBody
    public Result upgradeIos() {
        Result result = new Result();

        AppUpgrade upgrade = new AppUpgrade();

        result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        result.setData(upgrade);
        return result;
    }

    //获取当前时间
    @RequestMapping(value = "nowTime", method = RequestMethod.GET)
    @ApiOperation("获取当前时间")
    @ResponseBody
    public Result nowTime() {
        Result result = new Result();
        result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        result.setStatus(MessageTemplate.OPERATE_SUCCESS.getCode());
        result.setData(DateUtil.dateToStr(new Date(), Constant.DATE_TIME_FORMAT));
        return result;
    }
}
