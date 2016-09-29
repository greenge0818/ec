package com.prcsteel.ec.controller;

import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.core.util.UserUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author peanut
 * @description
 * @date 2016/5/3 12:51
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    /**
     * 注册中转
     *
     * @return
     * @author peanut
     * @date 2016/05/03
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ApiOperation("注册中转")
    public String register() {
        return "/user/register";
    }

    /**
     * 登陆中转
     *
     * @return
     * @author peanut
     * @date 2016/05/03
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiOperation("登陆中转")
    public String login() {
        return "/user/login";
    }

    /**
     * 注册成功中转
     *
     * @return
     * @author peanut
     * @date 2016/05/03
     */
    @RequestMapping(value = "/regresult", method = RequestMethod.GET)
    @ApiOperation("注册成功中转")
    public String regresult() {
        return "/user/regresult";
    }

    /**
     * 登陆页面忘记密码(会员中心修改密码)中转至重置密码
     *
     * @return
     * @author peanut
     * @date 2016/05/03
     */
    @RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
    @ApiOperation("忘记密码中转至重置密码")
    public String resetPassword(ModelMap out) {
        String userPhone = UserUtils.getPrincipal();
        out.put("userPhone", (StringUtils.isNotBlank(userPhone) && StringUtil.isPhoneNumberCheck(userPhone)) ?
                StringUtil.truncatePhone(userPhone) : null);
        return "/user/resetpassword";
    }

}
