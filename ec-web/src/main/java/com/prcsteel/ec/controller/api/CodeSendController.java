package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.ResultMsgType;
import com.prcsteel.ec.core.enums.SMSTemplate;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author peanut
 * @description 发送验证码Controller
 * @date 2016/4/29 14:52
 */
@RestController()
@RequestMapping("/api/code/send")
public class CodeSendController {

    @Resource
    private UserService userService;

    @Resource
    private CacheService cacheService;
    /**
     * 注册发送验证码
     *
     * @param phone 手机号
     * @return
     * @author peanut
     * @date 2016/04/29
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation("注册发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "vcode", value = "图形验证码", dataType = "string", paramType = "query")
    })
    public Result sendCodeForRegister(@RequestParam("phone") String phone,@RequestParam("vcode") String vcode, HttpSession session) {
        if(StringUtils.isBlank(vcode)){
            return new Result(MessageTemplate.VCODE_EMPTY_ERROR.getCode(),null,ResultMsgType.BUSINESS);
        }
        String cacheVcode = (String) cacheService.get(Constant.VALIDATE_CODE_CACHE_KEY+session.getId());
        if(!vcode.equalsIgnoreCase(cacheVcode)){
            return new Result(MessageTemplate.VCODE_INCORRECT_ERROR.getCode(),null,ResultMsgType.BUSINESS);
        }
        return doSendCode(phone, SMSTemplate.REGISTER.getModule());
    }

    /**
     * 登陆发送验证码
     *
     * @param phone 手机号
     * @return
     * @author peanut
     * @date 2016/04/29
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation("登陆发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query")
    })
    public Result sendCodeForLogin(@RequestParam("phone") String phone) {
        return doSendCode(phone, SMSTemplate.LOGIN.getModule());
    }

    /**
     * 忘记密码发送验证码__未登陆
     *
     * @param phone 手机号
     * @return
     * @author peanut
     * @date 2016/05/04
     */
    @ResponseBody
    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
    @ApiOperation("忘记密码发送验证码__未登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query")
    })
    public Result sendCodeForResetPassword(String phone) {

        return doSendCode(phone, SMSTemplate.FORGOT_PASSWORD.getModule());
    }

    /**
     * 重置密码发送验证码__登陆后
     *
     * @return
     * @author peanut
     * @date 2016/05/011
     */
    @ResponseBody
    @RequestMapping(value = "/resetpassword_afterlogin", method = RequestMethod.POST)
    @ApiOperation("重置密码发送验证码__登陆后")
    public Result sendCodeForResetPasswordAfterLogin() {
        String userPhone = UserUtils.getPrincipal();
        if (StringUtils.isNotBlank(userPhone)) {
            return doSendCode(userPhone, SMSTemplate.RESET_PASSWORD.getModule());
        } else {
            return new Result(MessageTemplate.USER_NOT_SIGN_IN.getCode());
        }
    }

    /**
     * 修改绑定手机号发送验证码_旧手机号 (只能登陆后操作)
     *
     * @return
     * @author peanut
     * @date 2016/05/04
     */
    @ResponseBody
    @RequestMapping(value = "/resetphone_old", method = RequestMethod.POST)
    @ApiOperation("修改绑定手机号发送验证码_旧手机号 (只能登陆后操作)")
    public Result sendCodeForResetPhone_old() {
        String userPhone = UserUtils.getPrincipal();
        if (StringUtils.isNotBlank(userPhone)) {
            return doSendCode(userPhone, SMSTemplate.RESET_PHONE_OLD.getModule());
        } else {
            return new Result(MessageTemplate.USER_NOT_SIGN_IN.getCode());
        }
    }

    /**
     * 修改绑定手机号发送验证码_新手机号 (只能登陆后操作)
     *
     * @param phone 手机号
     * @return
     * @author peanut
     * @date 2016/05/05
     */
    @ResponseBody
    @RequestMapping(value = "/resetphone_new", method = RequestMethod.POST)
    @ApiOperation("修改绑定手机号发送验证码_新手机号 (只能登陆后操作)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query")
    })
    public Result sendCodeForResetPhone(@RequestParam("phone") String phone) {
        if (StringUtils.isBlank(UserUtils.getPrincipal())) {
            return new Result(MessageTemplate.USER_NOT_SIGN_IN.getCode());
        }
        return doSendCode(phone, SMSTemplate.RESET_PHONE_NEW.getModule());
    }

    /**
     * 通用方法
     *
     * @param phone  手机号
     * @param module 发送验证码模块
     * @return
     */
    private Result doSendCode(String phone, String module) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            boolean bol = userService.sendPhoneCode(phone, module);
            result.setCode(bol ? MessageTemplate.SMS_SEND_SUCCESS.getCode() : MessageTemplate.SMS_SEND_ERROR.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
            if (MessageTemplate.ESB_SEND_SMS_ERROR.getCode().equals(e.getCode()) || MessageTemplate.JSON_PARSE_ERROR.getCode().equals(e.getCode())) {
                result.setCode(MessageTemplate.SMS_SEND_ERROR.getCode());
            }
        }
        return result;
    }

    /**
     * 注册/登陆 发送验证码
     *
     * @param phone 手机号
     * @return
     * @author Tiny
     * @date 2016/05/03
     */
    @ResponseBody
    @RequestMapping(value = "/registerorlogin", method = RequestMethod.POST)
    @ApiOperation("注册/登陆发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query")
    })
    public Result sendCodeForRegisterOrLogin(@RequestParam("phone") String phone) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            boolean bol = userService.sendPhoneCodeWithoutModel(phone);
            result.setCode(bol ? MessageTemplate.SMS_SEND_SUCCESS.getCode() : MessageTemplate.SMS_SEND_ERROR.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
            if (MessageTemplate.ESB_SEND_SMS_ERROR.getCode().equals(e.getCode()) || MessageTemplate.JSON_PARSE_ERROR.getCode().equals(e.getCode())) {
                result.setCode(MessageTemplate.SMS_SEND_ERROR.getCode());
            }
        }
        return result;
    }

}
