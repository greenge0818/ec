package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.OpType;
import com.prcsteel.ec.core.enums.ResultMsgType;
import com.prcsteel.ec.core.enums.SMSTemplate;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.oplog.OpLog;
import com.prcsteel.ec.core.oplog.OpParam;
import com.prcsteel.ec.core.util.CookieUtil;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.service.CommonService;
import com.prcsteel.ec.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rolyer on 2016/4/24.
 */
@RestController
@RequestMapping("/api/user/")
public class UserRestController {

    @Resource
    private UserService userService;

    @Resource
    private CommonService commonService;

    /**
     * 验证手机号是否存在
     *
     * @param phone
     * @return
     * @author peanut
     * @date 2016/04/29
     */
    @ResponseBody
    @RequestMapping(value = "phone/check", method = RequestMethod.POST)
    @ApiOperation("验证手机号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query")
    })
    public Result checkPhoneExists(@RequestParam("phone") String phone) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            userService.checkPhoneValid(phone);
            result.setCode(MessageTemplate.PHONE_AVAILABLE.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 用户注册(密码必填)
     *
     * @param user
     * @return
     * @author peanut
     * @date 2016/04/29
     */
    @OpLog(OpType.USER_REGISTER)
    @OpParam("user")
    @ResponseBody
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册(密码必填)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result userRegister(User user) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            userService.userRegisterPwdRequired(user);
            //注册成功
            result.setCode(MessageTemplate.USER_REGISTER_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 用户注册(无密码)
     *
     * @param user
     * @return
     * @author Tiny
     * @date 2016/05/12
     */
    @OpLog(OpType.USER_REGISTER)
    @OpParam("user")
    @ResponseBody
    @RequestMapping(value = "/registnopwd", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册(无密码)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result userRegisterPwdNotRequired(User user) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            result.setData(userService.userRegisterPwdNotRequired(user));
            result.setCode(MessageTemplate.USER_REGISTER_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 用户密码登陆
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return
     * @author peanut
     * @date 2016/04/29

     @OpLog(OpType.USER_LOGIN)
     @OpParam("phone")
     @OpParam("pwd")
     @ResponseBody
     @RequestMapping(value = "/pwd/login", method = RequestMethod.POST)
     @ApiOperation(value = "用户密码登陆")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query"),
     @ApiImplicitParam(name = "pwd", value = "密码", dataType = "string", paramType = "query")
     })
     @Deprecated public Result userPwdLogin(String phone, String pwd, HttpServletRequest request) {
     Result result = new Result(ResultMsgType.BUSINESS);
     try {
     boolean res = userService.userLogin(phone, pwd, LoginType.PHONE_PASSWORD.toString(), CookieUtil.getCookieId(request));
     result.setCode(res ? MessageTemplate.LOGIN_SUCCESS.getCode() : MessageTemplate.LOGIN_ERROR.getCode());
     } catch (BusinessException e) {
     result.setCode(e.getCode());
     }
     return result;
     }
     */

    /**
     * 用户验证码登陆
     *
     * @param phone 手机号
     * @param code  手机验证码
     * @return
     * @author peanut
     * @date 2016/04/29
     * @see com.prcsteel.ec.core.enums.LoginType

     @OpLog(OpType.USER_LOGIN)
     @OpParam("phone")
     @OpParam("code")
     @ResponseBody
     @RequestMapping(value = "/code/login", method = RequestMethod.POST)
     @ApiOperation(value = "用户验证码登陆")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query"),
     @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query"),
     })
     @Deprecated public Result userCodeLogin(String phone, String code, HttpServletRequest request) {
     Result result = new Result(ResultMsgType.BUSINESS);
     try {
     boolean res = userService.userLogin(phone, code, LoginType.PHONE_CODE.toString(), CookieUtil.getCookieId(request));
     result.setCode(res ? MessageTemplate.LOGIN_SUCCESS.getCode() : MessageTemplate.LOGIN_ERROR.getCode());
     } catch (BusinessException e) {
     result.setCode(e.getCode());
     }
     return result;
     }
     */

    /**
     * 用户登陆后的处理
     *
     * @return
     * @author peanut
     * @date 2016/05/13
     */
    @ResponseBody
    @RequestMapping(value = "/login/after", method = RequestMethod.POST)
    @ApiOperation(value = "登陆后的处理")
    public Result afterLogin(HttpServletRequest request) {
        return new Result(userService.afterLogin(CookieUtil.getCookieId(request)));
    }

    /**
     * 检查忘记密码验证码正确性__未登陆
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    @RequestMapping(value = "/pwd/code/check_unlogin", method = RequestMethod.POST)
    @ApiOperation(value = "检查忘记密码验证码正确性__未登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result checkCodeForResetPassword(String phone, String code) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            userService.checkCodeForResetPassword(phone, code);
            result.setCode(MessageTemplate.SMS_CODE_AVAILABLE.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 检查重置密码验证码正确性__登陆后
     *
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/pwd/code/check_afterlogin", method = RequestMethod.POST)
    @ApiOperation(value = "检查重置密码验证码正确性__登陆后")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result checkCodeForResetPassword(String code) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            userService.checkCodeForResetPassword(code);
            result.setCode(MessageTemplate.SMS_CODE_AVAILABLE.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 确定忘记密码__未登陆
     *
     * @param phone 手机号
     * @param pwd   密码
     * @param code  验证码
     * @return
     */
    @OpLog(OpType.USER_RESET_PWD)
    @OpParam("phone")
    @OpParam("pwd")
    @OpParam("code")
    @RequestMapping(value = "/pwd/resetpassword_unlogin", method = RequestMethod.POST)
    @ApiOperation(value = "确定忘记密码__未登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result resetPassword(String phone, String pwd, String code) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            userService.resetPassword(phone, pwd, code, SMSTemplate.FORGOT_PASSWORD.getModule());
            result.setCode(MessageTemplate.USER_PWD_RESET_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 确定重置密码__登陆后
     *
     * @param pwd  密码
     * @param code 验证码
     * @return
     */
    @OpLog(OpType.USER_MODIFY_PWD)
    @OpParam("pwd")
    @OpParam("code")
    @RequestMapping(value = "/pwd/resetpassword_afterlogin", method = RequestMethod.POST)
    @ApiOperation(value = "确定重置密码__登陆后")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pwd", value = "密码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result resetPassword(String pwd, String code) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            String userPhone = UserUtils.getPrincipal();
            if (StringUtils.isNotBlank(userPhone)) {
                userService.resetPassword(userPhone, pwd, code, SMSTemplate.RESET_PASSWORD.getModule());
                result.setCode(MessageTemplate.USER_PWD_RESET_SUCCESS.getCode());
            } else {
                result.setCode(MessageTemplate.USER_NOT_SIGN_IN.getCode());
            }
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 检查重置手机号验证码正确性_旧手机号
     *
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/phone/code/check", method = RequestMethod.POST)
    @ApiOperation(value = "检查重置手机号验证码正确性_旧手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result checkCodeForResetPhone(String code) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            userService.checkCodeForResetPhone(code);
            result.setCode(MessageTemplate.SMS_CODE_AVAILABLE.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 修改手机号
     *
     * @param newPhone 新手机号
     * @param newCode  新验证码
     * @return
     */
    @OpLog(OpType.USER_MODIFY_PHONE)
    @OpParam("newPhone")
    @OpParam("newCode")
    @RequestMapping(value = "/phone/resetphone", method = RequestMethod.POST)
    @ApiOperation(value = "确定修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newPhone", value = "新手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "newCode", value = "新验证码", dataType = "string", paramType = "query")
    })
    public Result resetPhone(String newPhone, String newCode) {
        Result result = new Result(ResultMsgType.BUSINESS);
        try {
            userService.resetPhone(newPhone, newCode);
            result.setCode(MessageTemplate.PHONE_RESET_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 根据手机获取用户信息
     *
     * @param mobile 手机号
     * @return
     */
    @RequestMapping(value = "/mobile/{mobile}", method = RequestMethod.GET)
    @ApiOperation(value = "根据手机获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string", paramType = "path")
    })
    public Result getUserInfoByPhone(@PathVariable("mobile") String mobile) {
        Result result = new Result();
        try {
            Map userInfoMap = new HashMap<>();
            User user = userService.getUserInfo(mobile);
            if (user != null) {
                userInfoMap.put("uid", user.getId());
                userInfoMap.put("username", user.getName());
                result.setData(userInfoMap);
                result.setStatus(MessageTemplate.PHP_SUCCESS.getCode());
            }else{
                result.setStatus(MessageTemplate.USER_EMPTY.getCode());
            }
        } catch (BusinessException e) {
            result.setStatus(e.getCode());
        }
        return result;
    }

    /**
     * 根据用户ID获取手机
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", paramType = "path")
    })
    public Result getUserInfoById(@PathVariable("id") Integer id) {
        Result result = new Result();
        try {
            Map userInfoMap = new HashMap<>();
            User user = userService.queryActiveUserById(id);
            if (user != null) {
                userInfoMap.put("phone", user.getMobile());
                result.setData(userInfoMap);
                result.setStatus(MessageTemplate.PHP_SUCCESS.getCode());
            }else{
                result.setStatus(MessageTemplate.USER_EMPTY.getCode());
            }
        } catch (BusinessException e) {
            result.setStatus(e.getCode());
        }
        return result;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @RequestMapping(value = "/getloginuser", method = RequestMethod.POST)
    @ApiOperation(value = "获取当前登录用户信息")
    public Result getLoginUser() {
        User user = commonService.getCurrentUser();
        return new Result(user != null ? MessageTemplate.LOGIN_SUCCESS.getCode() : MessageTemplate.USER_NOT_SIGN_IN.getCode(), user, ResultMsgType.BUSINESS);
    }

}
